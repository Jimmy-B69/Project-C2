package pe.edu.upeu.fsocietysys.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pe.edu.upeu.fsocietysys.components.ColumnInfo;
import pe.edu.upeu.fsocietysys.components.TableViewHelper;
import pe.edu.upeu.fsocietysys.components.Toast;
import pe.edu.upeu.fsocietysys.model.UnidMedida;
import pe.edu.upeu.fsocietysys.service.IUnidadMedidaService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class UnidadMedidaController {

    @FXML TextField txtNombreMedida, txtFiltroDato;
    @FXML private TableView<UnidMedida> tableView;
    @FXML Label lbnMsg;
    @FXML private AnchorPane miContenedor;

    private Stage stage;
    private final IUnidadMedidaService ums;
    private ObservableList<UnidMedida> listarUnidades;
    private Long idUnidadCE = 0L;

    public UnidadMedidaController(IUnidadMedidaService ums) {
        this.ums = ums;
    }

    @FXML
    public void initialize() {
        miContenedor.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage s) {
                stage = s;
            }
        });

        TableViewHelper<UnidMedida> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("ID", new ColumnInfo("idUnidad", 60.0));
        columns.put("Unidad de Medida", new ColumnInfo("nombreMedida", 300.0));

        Consumer<UnidMedida> updateAction = this::editForm;
        Consumer<UnidMedida> deleteAction = u -> {
            ums.delete(u.getIdUnidad());
            mostrarToast("¡Unidad eliminada!");
            listar();
        };

        tableViewHelper.addColumnsInOrderWithSize(tableView, columns, updateAction, deleteAction);
        tableView.setTableMenuButtonVisible(true);
        listar();
    }

    public void listar() {
        try {
            tableView.getItems().clear();
            listarUnidades = FXCollections.observableArrayList(ums.findAll());
            tableView.getItems().addAll(listarUnidades);
            if (txtFiltroDato != null) {
                txtFiltroDato.textProperty().addListener((obs, o, n) -> filtrar(n));
            }
        } catch (Exception e) {
            System.err.println("Error al listar unidades: " + e.getMessage());
        }
    }

    private void filtrar(String f) {
        if (f == null || f.trim().isEmpty()) {
            tableView.getItems().setAll(listarUnidades);
        } else {
            String q = f.toLowerCase().trim();
            List<UnidMedida> filtrados = listarUnidades.stream()
                    .filter(u -> u.getNombreMedida() != null && u.getNombreMedida().toLowerCase().contains(q))
                    .collect(Collectors.toList());
            tableView.getItems().setAll(filtrados);
        }
    }

    @FXML
    public void validarFormulario() {
        String nombre = txtNombreMedida.getText() != null ? txtNombreMedida.getText().trim() : "";
        if (nombre.isEmpty()) {
            if (lbnMsg != null) {
                lbnMsg.setText("El nombre de la unidad es obligatorio");
                lbnMsg.setStyle("-fx-text-fill: red;");
            }
            return;
        }

        if (idUnidadCE > 0L) {
            UnidMedida u = new UnidMedida(idUnidadCE, nombre);
            ums.update(idUnidadCE, u);
            mostrarToast("¡Unidad actualizada!");
        } else {
            UnidMedida u = new UnidMedida(null, nombre);
            ums.save(u);
            mostrarToast("¡Unidad guardada!");
        }
        clearForm();
        listar();
    }

    private void mostrarToast(String msg) {
        if (stage != null) {
            Toast.showToast(stage, msg, 2000, stage.getWidth() / 1.5, stage.getHeight() / 2);
        }
    }

    public void editForm(UnidMedida u) {
        txtNombreMedida.setText(u.getNombreMedida());
        idUnidadCE = u.getIdUnidad();
        if (lbnMsg != null) lbnMsg.setText("");
    }

    @FXML
    public void clearForm() {
        txtNombreMedida.clear();
        idUnidadCE = 0L;
        if (lbnMsg != null) lbnMsg.setText("");
    }
}
