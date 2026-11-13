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
import pe.edu.upeu.fsocietysys.model.Marca;
import pe.edu.upeu.fsocietysys.service.IMarcaService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MarcaController {

    @FXML TextField txtNombre, txtFiltroDato;
    @FXML private TableView<Marca> tableView;
    @FXML Label lbnMsg;
    @FXML private AnchorPane miContenedor;

    private Stage stage;
    private final IMarcaService ms;
    private ObservableList<Marca> listarMarcas;
    private Long idMarcaCE = 0L;

    public MarcaController(IMarcaService ms) {
        this.ms = ms;
    }

    @FXML
    public void initialize() {
        miContenedor.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage s) {
                stage = s;
            }
        });

        TableViewHelper<Marca> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("ID", new ColumnInfo("idMarca", 60.0));
        columns.put("Nombre de Marca", new ColumnInfo("nombre", 300.0));

        Consumer<Marca> updateAction = this::editForm;
        Consumer<Marca> deleteAction = m -> {
            ms.delete(m.getIdMarca());
            mostrarToast("¡Marca eliminada!");
            listar();
        };

        tableViewHelper.addColumnsInOrderWithSize(tableView, columns, updateAction, deleteAction);
        tableView.setTableMenuButtonVisible(true);
        listar();
    }

    public void listar() {
        try {
            tableView.getItems().clear();
            listarMarcas = FXCollections.observableArrayList(ms.findAll());
            tableView.getItems().addAll(listarMarcas);
            if (txtFiltroDato != null) {
                txtFiltroDato.textProperty().addListener((obs, o, n) -> filtrar(n));
            }
        } catch (Exception e) {
            System.err.println("Error al listar marcas: " + e.getMessage());
        }
    }

    private void filtrar(String f) {
        if (f == null || f.trim().isEmpty()) {
            tableView.getItems().setAll(listarMarcas);
        } else {
            String q = f.toLowerCase().trim();
            List<Marca> filtrados = listarMarcas.stream()
                    .filter(m -> m.getNombre() != null && m.getNombre().toLowerCase().contains(q))
                    .collect(Collectors.toList());
            tableView.getItems().setAll(filtrados);
        }
    }

    @FXML
    public void validarFormulario() {
        String nombre = txtNombre.getText() != null ? txtNombre.getText().trim() : "";
        if (nombre.isEmpty()) {
            if (lbnMsg != null) {
                lbnMsg.setText("El nombre de la marca es obligatorio");
                lbnMsg.setStyle("-fx-text-fill: red;");
            }
            return;
        }

        if (idMarcaCE > 0L) {
            Marca m = new Marca(idMarcaCE, nombre);
            ms.update(idMarcaCE, m);
            mostrarToast("¡Marca actualizada!");
        } else {
            Marca m = new Marca(null, nombre);
            ms.save(m);
            mostrarToast("¡Marca guardada!");
        }
        clearForm();
        listar();
    }

    private void mostrarToast(String msg) {
        if (stage != null) {
            Toast.showToast(stage, msg, 2000, stage.getWidth() / 1.5, stage.getHeight() / 2);
        }
    }

    public void editForm(Marca m) {
        txtNombre.setText(m.getNombre());
        idMarcaCE = m.getIdMarca();
        if (lbnMsg != null) lbnMsg.setText("");
    }

    @FXML
    public void clearForm() {
        txtNombre.clear();
        idMarcaCE = 0L;
        if (lbnMsg != null) lbnMsg.setText("");
    }
}
