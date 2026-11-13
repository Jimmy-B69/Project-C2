package pe.edu.upeu.fsocietysys.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pe.edu.upeu.fsocietysys.components.ColumnInfo;
import pe.edu.upeu.fsocietysys.components.TableViewHelper;
import pe.edu.upeu.fsocietysys.components.Toast;
import pe.edu.upeu.fsocietysys.components.ToltipCustom;
import pe.edu.upeu.fsocietysys.model.Proveedor;
import pe.edu.upeu.fsocietysys.service.IProveedorService;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ProveedorController {

    @FXML TextField txtDniruc, txtNombresRaso, txtCelular, txtEmail, txtDireccion, txtFiltroDato;
    @FXML ComboBox<String> cbxTipoDoc;
    @FXML private TableView<Proveedor> tableView;
    @FXML Label lbnMsg;
    @FXML private AnchorPane miContenedor;

    private Stage stage;
    private final IProveedorService ps;
    private Validator validator;
    private ObservableList<Proveedor> listarProveedores;
    private Long idProveedorCE = 0L;
    private final ToltipCustom ttc = new ToltipCustom();

    public ProveedorController(IProveedorService ps) {
        this.ps = ps;
    }

    @FXML
    public void initialize() {
        miContenedor.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage s) {
                stage = s;
            }
        });

        cbxTipoDoc.getItems().addAll("RUC", "DNI", "OTRO");
        cbxTipoDoc.getSelectionModel().selectFirst();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        TableViewHelper<Proveedor> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("ID", new ColumnInfo("idProveedor", 50.0));
        columns.put("DNI / RUC", new ColumnInfo("dniruc", 120.0));
        columns.put("Razón Social", new ColumnInfo("nombresRaso", 220.0));
        columns.put("Celular", new ColumnInfo("celular", 110.0));
        columns.put("Email", new ColumnInfo("email", 180.0));
        columns.put("Dirección", new ColumnInfo("direccion", 200.0));

        Consumer<Proveedor> updateAction = this::editForm;
        Consumer<Proveedor> deleteAction = proveedor -> {
            ps.delete(proveedor.getIdProveedor());
            mostrarToast("¡Proveedor eliminado correctamente!");
            listar();
        };

        tableViewHelper.addColumnsInOrderWithSize(tableView, columns, updateAction, deleteAction);
        tableView.setTableMenuButtonVisible(true);
        listar();
    }

    public void listar() {
        try {
            tableView.getItems().clear();
            listarProveedores = FXCollections.observableArrayList(ps.findAll());
            tableView.getItems().addAll(listarProveedores);
            if (txtFiltroDato != null) {
                txtFiltroDato.textProperty().addListener((obs, o, n) -> filtrar(n));
            }
        } catch (Exception e) {
            System.err.println("Error al listar proveedores: " + e.getMessage());
        }
    }

    private void filtrar(String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) {
            tableView.getItems().setAll(listarProveedores);
        } else {
            String f = filtro.toLowerCase().trim();
            List<Proveedor> filtrados = listarProveedores.stream()
                    .filter(p -> (p.getDniruc() != null && p.getDniruc().toLowerCase().contains(f))
                            || (p.getNombresRaso() != null && p.getNombresRaso().toLowerCase().contains(f))
                            || (p.getEmail() != null && p.getEmail().toLowerCase().contains(f)))
                    .collect(Collectors.toList());
            tableView.getItems().setAll(filtrados);
        }
    }

    @FXML
    public void validarFormulario() {
        Proveedor form = new Proveedor();
        form.setDniruc(txtDniruc.getText() != null ? txtDniruc.getText().trim() : "");
        form.setNombresRaso(txtNombresRaso.getText() != null ? txtNombresRaso.getText().trim() : "");
        form.setTipoDoc(cbxTipoDoc.getValue());
        form.setCelular(txtCelular.getText());
        form.setEmail(txtEmail.getText());
        form.setDireccion(txtDireccion.getText());

        Set<ConstraintViolation<Proveedor>> violaciones = validator.validate(form);
        if (violaciones.isEmpty()) {
            if (idProveedorCE > 0L) {
                form.setIdProveedor(idProveedorCE);
                ps.update(idProveedorCE, form);
                mostrarToast("¡Proveedor actualizado!");
            } else {
                ps.save(form);
                mostrarToast("¡Proveedor registrado!");
            }
            clearForm();
            listar();
        } else {
            limpiarError();
            for (ConstraintViolation<Proveedor> v : violaciones) {
                if ("dniruc".equals(v.getPropertyPath().toString())) {
                    ttc.marcarError(txtDniruc, v.getMessage());
                } else if ("nombresRaso".equals(v.getPropertyPath().toString())) {
                    ttc.marcarError(txtNombresRaso, v.getMessage());
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

    public void editForm(Proveedor proveedor) {
        txtDniruc.setText(proveedor.getDniruc());
        txtNombresRaso.setText(proveedor.getNombresRaso());
        txtCelular.setText(proveedor.getCelular());
        txtEmail.setText(proveedor.getEmail());
        txtDireccion.setText(proveedor.getDireccion());
        if (proveedor.getTipoDoc() != null) {
            cbxTipoDoc.setValue(proveedor.getTipoDoc());
        }
        idProveedorCE = proveedor.getIdProveedor();
        limpiarError();
    }

    @FXML
    public void clearForm() {
        txtDniruc.clear();
        txtNombresRaso.clear();
        txtCelular.clear();
        txtEmail.clear();
        txtDireccion.clear();
        cbxTipoDoc.getSelectionModel().selectFirst();
        idProveedorCE = 0L;
        if (lbnMsg != null) lbnMsg.setText("");
        limpiarError();
    }

    public void limpiarError() {
        ttc.limpiarCampo(txtDniruc);
        ttc.limpiarCampo(txtNombresRaso);
    }
}
