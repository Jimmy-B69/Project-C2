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
import pe.edu.upeu.fsocietysys.components.ComboBoxAutoComplete;
import pe.edu.upeu.fsocietysys.components.TableViewHelper;
import pe.edu.upeu.fsocietysys.components.Toast;
import pe.edu.upeu.fsocietysys.components.ToltipCustom;
import pe.edu.upeu.fsocietysys.dto.ComboBoxOption;
import pe.edu.upeu.fsocietysys.model.Perfil;
import pe.edu.upeu.fsocietysys.model.Usuario;
import pe.edu.upeu.fsocietysys.service.IPerfilService;
import pe.edu.upeu.fsocietysys.service.IUsuarioService;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class UsuarioController {

    @FXML TextField txtUsuario, txtFiltroDato;
    @FXML PasswordField txtClave;
    @FXML ComboBox<ComboBoxOption> cbxPerfil;
    @FXML ComboBox<String> cbxEstado;
    @FXML private TableView<Usuario> tableView;
    @FXML Label lbnMsg;
    @FXML private AnchorPane miContenedor;

    private Stage stage;
    private final IUsuarioService us;
    private final IPerfilService ps;
    private Validator validator;
    private ObservableList<Usuario> listarUsuarios;
    private Long idUsuarioCE = 0L;
    private final ToltipCustom ttc = new ToltipCustom();

    public UsuarioController(IUsuarioService us, IPerfilService ps) {
        this.us = us;
        this.ps = ps;
    }

    @FXML
    public void initialize() {
        miContenedor.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage s) {
                stage = s;
            }
        });

        cbxPerfil.getItems().addAll(ps.listarCombobox());
        new ComboBoxAutoComplete<>(cbxPerfil);

        cbxEstado.getItems().addAll("ACTIVO", "INACTIVO");
        cbxEstado.getSelectionModel().selectFirst();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        TableViewHelper<Usuario> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("ID", new ColumnInfo("idUsuario", 60.0));
        columns.put("Usuario", new ColumnInfo("usuario", 180.0));
        columns.put("Perfil", new ColumnInfo("idPerfil.nombre", 180.0));
        columns.put("Estado", new ColumnInfo("estado", 120.0));

        Consumer<Usuario> updateAction = this::editForm;
        Consumer<Usuario> deleteAction = u -> {
            us.delete(u.getIdUsuario());
            mostrarToast("¡Usuario eliminado correctamente!");
            listar();
        };

        tableViewHelper.addColumnsInOrderWithSize(tableView, columns, updateAction, deleteAction);
        tableView.setTableMenuButtonVisible(true);
        listar();
    }

    public void listar() {
        try {
            tableView.getItems().clear();
            listarUsuarios = FXCollections.observableArrayList(us.findAll());
            tableView.getItems().addAll(listarUsuarios);
            if (txtFiltroDato != null) {
                txtFiltroDato.textProperty().addListener((obs, o, n) -> filtrar(n));
            }
        } catch (Exception e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
    }

    private void filtrar(String f) {
        if (f == null || f.trim().isEmpty()) {
            tableView.getItems().setAll(listarUsuarios);
        } else {
            String q = f.toLowerCase().trim();
            List<Usuario> filtrados = listarUsuarios.stream()
                    .filter(u -> (u.getUsuario() != null && u.getUsuario().toLowerCase().contains(q))
                            || (u.getIdPerfil() != null && u.getIdPerfil().getNombre().toLowerCase().contains(q)))
                    .collect(Collectors.toList());
            tableView.getItems().setAll(filtrados);
        }
    }

    @FXML
    public void validarFormulario() {
        Usuario form = new Usuario();
        form.setUsuario(txtUsuario.getText() != null ? txtUsuario.getText().trim() : "");
        form.setClave(txtClave.getText() != null ? txtClave.getText().trim() : "");
        form.setEstado(cbxEstado.getValue());

        String idPerfilStr = cbxPerfil.getSelectionModel().getSelectedItem() != null
                ? cbxPerfil.getSelectionModel().getSelectedItem().getKey() : null;
        if (idPerfilStr != null) {
            Perfil perfil = ps.findById(Long.parseLong(idPerfilStr));
            form.setIdPerfil(perfil);
        }

        Set<ConstraintViolation<Usuario>> violaciones = validator.validate(form);
        if (violaciones.isEmpty()) {
            if (idUsuarioCE > 0L) {
                form.setIdUsuario(idUsuarioCE);
                us.update(idUsuarioCE, form);
                mostrarToast("¡Usuario actualizado!");
            } else {
                us.save(form);
                mostrarToast("¡Usuario registrado!");
            }
            clearForm();
            listar();
        } else {
            limpiarError();
            for (ConstraintViolation<Usuario> v : violaciones) {
                if ("usuario".equals(v.getPropertyPath().toString())) {
                    ttc.marcarError(txtUsuario, v.getMessage());
                } else if ("clave".equals(v.getPropertyPath().toString())) {
                    ttc.marcarError(txtClave, v.getMessage());
                } else if ("idPerfil".equals(v.getPropertyPath().toString())) {
                    ttc.marcarError(cbxPerfil, v.getMessage());
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

    public void editForm(Usuario u) {
        txtUsuario.setText(u.getUsuario());
        txtClave.setText(u.getClave());
        if (u.getEstado() != null) {
            cbxEstado.setValue(u.getEstado());
        }
        if (u.getIdPerfil() != null) {
            cbxPerfil.getSelectionModel().select(
                    cbxPerfil.getItems().stream()
                            .filter(opt -> Objects.equals(opt.getKey(), String.valueOf(u.getIdPerfil().getIdPerfil())))
                            .findFirst().orElse(null));
        }
        idUsuarioCE = u.getIdUsuario();
        limpiarError();
    }

    @FXML
    public void clearForm() {
        txtUsuario.clear();
        txtClave.clear();
        cbxPerfil.getSelectionModel().clearSelection();
        cbxEstado.getSelectionModel().selectFirst();
        idUsuarioCE = 0L;
        if (lbnMsg != null) lbnMsg.setText("");
        limpiarError();
    }

    public void limpiarError() {
        ttc.limpiarCampo(txtUsuario);
        ttc.limpiarCampo(txtClave);
        ttc.limpiarCampo(cbxPerfil);
    }
}
