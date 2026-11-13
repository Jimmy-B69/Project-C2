package pe.edu.upeu.fsocietysys.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pe.edu.upeu.fsocietysys.components.ColumnInfo;
import pe.edu.upeu.fsocietysys.components.ComboBoxAutoComplete;
import pe.edu.upeu.fsocietysys.components.TableViewHelper;
import pe.edu.upeu.fsocietysys.components.Toast;
import pe.edu.upeu.fsocietysys.components.ToltipCustom;
import pe.edu.upeu.fsocietysys.dto.ComboBoxOption;
import pe.edu.upeu.fsocietysys.enums.TipoDocumento;
import pe.edu.upeu.fsocietysys.model.Cliente;
import pe.edu.upeu.fsocietysys.service.IClienteService;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ClienteController {

    @FXML TextField txtDniruc, txtNombres, txtRepLegal, txtDireccion, txtFiltroDato;
    @FXML ComboBox<ComboBoxOption> cbxTipoDocumento;
    @FXML private TableView<Cliente> tableView;
    @FXML Label lbnMsg;
    @FXML private AnchorPane miContenedor;

    private Stage stage;
    private final IClienteService cs;
    private Validator validator;
    private ObservableList<Cliente> listarClientes;
    private String idClienteCE = null;
    private final ToltipCustom ttc = new ToltipCustom();

    public ClienteController(IClienteService cs) {
        this.cs = cs;
    }

    @FXML
    public void initialize() {
        miContenedor.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage s) {
                stage = s;
            }
        });

        for (TipoDocumento td : TipoDocumento.values()) {
            ComboBoxOption opt = new ComboBoxOption();
            opt.setKey(td.name());
            opt.setValue(td.name());
            cbxTipoDocumento.getItems().add(opt);
        }
        new ComboBoxAutoComplete<>(cbxTipoDocumento);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        TableViewHelper<Cliente> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("DNI / RUC", new ColumnInfo("dniruc", 120.0));
        columns.put("Nombres / Razón", new ColumnInfo("nombres", 220.0));
        columns.put("Tipo Doc.", new ColumnInfo("tipoDocumento", 90.0));
        columns.put("Rep. Legal", new ColumnInfo("repLegal", 180.0));
        columns.put("Dirección", new ColumnInfo("direccion", 220.0));

        Consumer<Cliente> updateAction = this::editForm;
        Consumer<Cliente> deleteAction = cliente -> {
            cs.delete(cliente.getDniruc());
            mostrarToast("¡Cliente eliminado correctamente!");
            listar();
        };

        tableViewHelper.addColumnsInOrderWithSize(tableView, columns, updateAction, deleteAction);
        tableView.setTableMenuButtonVisible(true);
        listar();
    }

    public void listar() {
        try {
            tableView.getItems().clear();
            listarClientes = FXCollections.observableArrayList(cs.findAll());
            tableView.getItems().addAll(listarClientes);
            if (txtFiltroDato != null) {
                txtFiltroDato.textProperty().addListener((obs, o, n) -> filtrarClientes(n));
            }
        } catch (Exception e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
        }
    }

    private void filtrarClientes(String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) {
            tableView.getItems().setAll(listarClientes);
        } else {
            String f = filtro.toLowerCase().trim();
            List<Cliente> filtrados = listarClientes.stream()
                    .filter(c -> (c.getDniruc() != null && c.getDniruc().toLowerCase().contains(f))
                            || (c.getNombres() != null && c.getNombres().toLowerCase().contains(f))
                            || (c.getDireccion() != null && c.getDireccion().toLowerCase().contains(f)))
                    .collect(Collectors.toList());
            tableView.getItems().setAll(filtrados);
        }
    }

    @FXML
    public void validarFormulario() {
        Cliente form = new Cliente();
        form.setDniruc(txtDniruc.getText() != null ? txtDniruc.getText().trim() : "");
        form.setNombres(txtNombres.getText() != null ? txtNombres.getText().trim() : "");
        form.setRepLegal(txtRepLegal.getText());
        form.setDireccion(txtDireccion.getText());

        if (cbxTipoDocumento.getSelectionModel().getSelectedItem() != null) {
            form.setTipoDocumento(TipoDocumento.valueOf(cbxTipoDocumento.getSelectionModel().getSelectedItem().getKey()));
        }

        Set<ConstraintViolation<Cliente>> violaciones = validator.validate(form);
        if (violaciones.isEmpty()) {
            if (idClienteCE != null && !idClienteCE.isEmpty()) {
                cs.update(idClienteCE, form);
                mostrarToast("¡Cliente actualizado!");
            } else {
                cs.save(form);
                mostrarToast("¡Cliente registrado!");
            }
            clearForm();
            listar();
        } else {
            limpiarError();
            for (ConstraintViolation<Cliente> v : violaciones) {
                if ("dniruc".equals(v.getPropertyPath().toString())) {
                    ttc.marcarError(txtDniruc, v.getMessage());
                } else if ("nombres".equals(v.getPropertyPath().toString())) {
                    ttc.marcarError(txtNombres, v.getMessage());
                }
            }
            if (lbnMsg != null) {
                lbnMsg.setText(violaciones.iterator().next().getMessage());
                lbnMsg.setStyle("-fx-text-fill: red;");
            }
        }
    }

    private void mostrarToast(String msg) {
        if (stage != null) {
            Toast.showToast(stage, msg, 2000, stage.getWidth() / 1.5, stage.getHeight() / 2);
        }
    }

    public void editForm(Cliente cliente) {
        txtDniruc.setText(cliente.getDniruc());
        txtDniruc.setDisable(true); // El ID natural no debe cambiar en edición
        txtNombres.setText(cliente.getNombres());
        txtRepLegal.setText(cliente.getRepLegal());
        txtDireccion.setText(cliente.getDireccion());

        if (cliente.getTipoDocumento() != null) {
            cbxTipoDocumento.getSelectionModel().select(
                    cbxTipoDocumento.getItems().stream()
                            .filter(opt -> Objects.equals(opt.getKey(), cliente.getTipoDocumento().name()))
                            .findFirst().orElse(null));
        }

        idClienteCE = cliente.getDniruc();
        limpiarError();
    }

    @FXML
    public void clearForm() {
        txtDniruc.clear();
        txtDniruc.setDisable(false);
        txtNombres.clear();
        txtRepLegal.clear();
        txtDireccion.clear();
        cbxTipoDocumento.getSelectionModel().clearSelection();
        idClienteCE = null;
        if (lbnMsg != null) lbnMsg.setText("");
        limpiarError();
    }

    public void limpiarError() {
        ttc.limpiarCampo(txtDniruc);
        ttc.limpiarCampo(txtNombres);
    }
}
