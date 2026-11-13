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
import pe.edu.upeu.fsocietysys.model.Categoria;
import pe.edu.upeu.fsocietysys.service.ICategoriaService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CategoriaController {

    @FXML TextField txtNombre, txtFiltroDato;
    @FXML private TableView<Categoria> tableView;
    @FXML Label lbnMsg;
    @FXML private AnchorPane miContenedor;

    private Stage stage;
    private final ICategoriaService cs;
    private ObservableList<Categoria> listarCategorias;
    private Long idCategoriaCE = 0L;

    public CategoriaController(ICategoriaService cs) {
        this.cs = cs;
    }

    @FXML
    public void initialize() {
        miContenedor.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage s) {
                stage = s;
            }
        });

        TableViewHelper<Categoria> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("ID", new ColumnInfo("idCategoria", 60.0));
        columns.put("Nombre de Categoría", new ColumnInfo("nombre", 300.0));

        Consumer<Categoria> updateAction = this::editForm;
        Consumer<Categoria> deleteAction = cat -> {
            cs.delete(cat.getIdCategoria());
            mostrarToast("¡Categoría eliminada!");
            listar();
        };

        tableViewHelper.addColumnsInOrderWithSize(tableView, columns, updateAction, deleteAction);
        tableView.setTableMenuButtonVisible(true);
        listar();
    }

    public void listar() {
        try {
            tableView.getItems().clear();
            listarCategorias = FXCollections.observableArrayList(cs.findAll());
            tableView.getItems().addAll(listarCategorias);
            if (txtFiltroDato != null) {
                txtFiltroDato.textProperty().addListener((obs, o, n) -> filtrar(n));
            }
        } catch (Exception e) {
            System.err.println("Error al listar categorías: " + e.getMessage());
        }
    }

    private void filtrar(String f) {
        if (f == null || f.trim().isEmpty()) {
            tableView.getItems().setAll(listarCategorias);
        } else {
            String q = f.toLowerCase().trim();
            List<Categoria> filtrados = listarCategorias.stream()
                    .filter(c -> c.getNombre() != null && c.getNombre().toLowerCase().contains(q))
                    .collect(Collectors.toList());
            tableView.getItems().setAll(filtrados);
        }
    }

    @FXML
    public void validarFormulario() {
        String nombre = txtNombre.getText() != null ? txtNombre.getText().trim() : "";
        if (nombre.isEmpty()) {
            if (lbnMsg != null) {
                lbnMsg.setText("El nombre de la categoría es obligatorio");
                lbnMsg.setStyle("-fx-text-fill: red;");
            }
            return;
        }

        if (idCategoriaCE > 0L) {
            Categoria cat = new Categoria(idCategoriaCE, nombre);
            cs.update(idCategoriaCE, cat);
            mostrarToast("¡Categoría actualizada!");
        } else {
            Categoria cat = new Categoria(null, nombre);
            cs.save(cat);
            mostrarToast("¡Categoría guardada!");
        }
        clearForm();
        listar();
    }

    private void mostrarToast(String msg) {
        if (stage != null) {
            Toast.showToast(stage, msg, 2000, stage.getWidth() / 1.5, stage.getHeight() / 2);
        }
    }

    public void editForm(Categoria c) {
        txtNombre.setText(c.getNombre());
        idCategoriaCE = c.getIdCategoria();
        if (lbnMsg != null) lbnMsg.setText("");
    }

    @FXML
    public void clearForm() {
        txtNombre.clear();
        idCategoriaCE = 0L;
        if (lbnMsg != null) lbnMsg.setText("");
    }
}
