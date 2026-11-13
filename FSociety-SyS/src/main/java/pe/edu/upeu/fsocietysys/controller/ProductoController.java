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
import pe.edu.upeu.fsocietysys.components.*;
import pe.edu.upeu.fsocietysys.dto.ComboBoxOption;
import pe.edu.upeu.fsocietysys.enums.TipoProducto;
import pe.edu.upeu.fsocietysys.model.Producto;
import pe.edu.upeu.fsocietysys.service.ICategoriaService;
import pe.edu.upeu.fsocietysys.service.IMarcaService;
import pe.edu.upeu.fsocietysys.service.IProductoService;
import pe.edu.upeu.fsocietysys.service.IUnidadMedidaService;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ProductoController {

    @FXML TextField txtNombreProducto, txtPUnit, txtPUnitOld, txtUtilidad, txtStock, txtStockOld, txtFiltroDato;
    @FXML ComboBox<ComboBoxOption> cbxTipoProducto;
    @FXML ComboBox<ComboBoxOption> cbxMarca;
    @FXML ComboBox<ComboBoxOption> cbxCategoria;
    @FXML ComboBox<ComboBoxOption> cbxUnidMedida;
    @FXML private TableView<Producto> tableView;
    @FXML Label lbnMsg, idPrueba;
    @FXML private AnchorPane miContenedor;

    private Stage stage;
    private final IMarcaService ms;
    private final ICategoriaService cs;
    private final IProductoService ps;
    private final IUnidadMedidaService ums;

    public ProductoController(IMarcaService ms, ICategoriaService cs,
                              IProductoService ps, IUnidadMedidaService ums) {
        this.ms = ms;
        this.cs = cs;
        this.ps = ps;
        this.ums = ums;
    }

    private Validator validator;
    private ObservableList<Producto> listarProducto;
    private Producto formulario;
    private Long idProductoCE = 0L;
    private final ToltipCustom ttc = new ToltipCustom();

    @FXML
    public void initialize() {
        miContenedor.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage s) {
                stage = s;
            }
        });

        cbxTipoProducto.getItems().addAll(ps.listarTipoProducto());
        new ComboBoxAutoComplete<>(cbxTipoProducto);

        cbxMarca.getItems().addAll(ms.listarCombobox());
        new ComboBoxAutoComplete<>(cbxMarca);

        cbxCategoria.getItems().addAll(cs.listarCombobox());
        new ComboBoxAutoComplete<>(cbxCategoria);

        cbxUnidMedida.getItems().addAll(ums.listarCombobox());
        new ComboBoxAutoComplete<>(cbxUnidMedida);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        TableViewHelper<Producto> tableViewHelper = new TableViewHelper<>();

        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("ID Pro.", new ColumnInfo("idProducto", 60.0));
        columns.put("Tipo Producto", new ColumnInfo("tipoProducto", 130.0));
        columns.put("Nombre Producto", new ColumnInfo("nombre", 200.0));
        columns.put("P. Unitario", new ColumnInfo("pu", 100.0));
        columns.put("Stock", new ColumnInfo("stock", 80.0));
        columns.put("Utilidad", new ColumnInfo("utilidad", 90.0));
        columns.put("Marca", new ColumnInfo("idMarca.nombre", 140.0));
        columns.put("Categoria", new ColumnInfo("idCategoria.nombre", 140.0));

        Consumer<Producto> updateAction = this::editForm;
        Consumer<Producto> deleteAction = producto -> {
            ps.delete(producto.getIdProducto());
            mostrarToast("¡Se eliminó el producto correctamente!");
            listar();
        };

        tableViewHelper.addColumnsInOrderWithSize(tableView, columns, updateAction, deleteAction);
        tableView.setTableMenuButtonVisible(true);
        listar();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void listar() {
        try {
            tableView.getItems().clear();
            listarProducto = FXCollections.observableArrayList(ps.findAll());
            tableView.getItems().addAll(listarProducto);
            if (txtFiltroDato != null) {
                txtFiltroDato.textProperty().addListener((obs, o, n) -> filtrarProductos(n));
            }
        } catch (Exception e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }
    }

    private void filtrarProductos(String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) {
            tableView.getItems().setAll(listarProducto);
        } else {
            String f = filtro.toLowerCase().trim();
            List<Producto> filtrados = listarProducto.stream()
                    .filter(p -> (p.getNombre() != null && p.getNombre().toLowerCase().contains(f))
                            || String.valueOf(p.getPu()).contains(f)
                            || String.valueOf(p.getUtilidad()).contains(f)
                            || (p.getIdMarca() != null && p.getIdMarca().getNombre().toLowerCase().contains(f))
                            || (p.getIdCategoria() != null && p.getIdCategoria().getNombre().toLowerCase().contains(f)))
                    .collect(Collectors.toList());
            tableView.getItems().setAll(filtrados);
        }
    }

    @FXML
    public void validarFormulario() {
        formulario = new Producto();
        formulario.setNombre(txtNombreProducto.getText());
        formulario.setPu(parseDoubleSafe(txtPUnit.getText()));
        formulario.setPuold(parseDoubleSafe(txtPUnitOld.getText()));
        formulario.setUtilidad(parseDoubleSafe(txtUtilidad.getText()));
        formulario.setStock(parseDoubleSafe(txtStock.getText()));
        formulario.setStockold(parseDoubleSafe(txtStockOld.getText()));

        String idxTP = cbxTipoProducto.getSelectionModel().getSelectedItem() == null ? ""
                : cbxTipoProducto.getSelectionModel().getSelectedItem().getKey();
        formulario.setTipoProducto(idxTP.isEmpty() ? null : TipoProducto.valueOf(idxTP));

        String idxM = cbxMarca.getSelectionModel().getSelectedItem() == null ? "0"
                : cbxMarca.getSelectionModel().getSelectedItem().getKey();
        formulario.setIdMarca("0".equals(idxM) ? null : ms.findById(Long.parseLong(idxM)));

        String idxC = cbxCategoria.getSelectionModel().getSelectedItem() == null ? "0"
                : cbxCategoria.getSelectionModel().getSelectedItem().getKey();
        formulario.setIdCategoria("0".equals(idxC) ? null : cs.findById(Long.parseLong(idxC)));

        String idxUM = cbxUnidMedida.getSelectionModel().getSelectedItem() == null ? "0"
                : cbxUnidMedida.getSelectionModel().getSelectedItem().getKey();
        formulario.setIdUnidad("0".equals(idxUM) ? null : ums.findById(Long.parseLong(idxUM)));

        Set<ConstraintViolation<Producto>> violaciones = validator.validate(formulario);
        List<ConstraintViolation<Producto>> violacionesOrdenadas = violaciones.stream()
                .sorted(Comparator.comparing(v -> v.getPropertyPath().toString())).toList();

        if (violacionesOrdenadas.isEmpty()) {
            procesarFormulario();
        } else {
            mostrarErroresValidacion(violacionesOrdenadas);
        }
    }

    private double parseDoubleSafe(String value) {
        if (value == null || value.trim().isEmpty()) return 0.0;
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private void mostrarErroresValidacion(List<ConstraintViolation<Producto>> violaciones) {
        limpiarError();
        Map<String, Control> campos = new LinkedHashMap<>();
        campos.put("nombre", txtNombreProducto);
        campos.put("tipoProducto", cbxTipoProducto);
        campos.put("pu", txtPUnit);
        campos.put("puold", txtPUnitOld);
        campos.put("utilidad", txtUtilidad);
        campos.put("stock", txtStock);
        campos.put("stockold", txtStockOld);
        campos.put("idMarca", cbxMarca);
        campos.put("idCategoria", cbxCategoria);
        campos.put("idUnidad", cbxUnidMedida);

        LinkedHashMap<String, String> erroresOrdenados = new LinkedHashMap<>();
        final Control[] primerCtrl = {null};
        for (String campo : campos.keySet()) {
            violaciones.stream()
                    .filter(v -> v.getPropertyPath().toString().equals(campo))
                    .findFirst().ifPresent(v -> {
                        erroresOrdenados.put(campo, v.getMessage());
                        Control c = campos.get(campo);
                        if (c != null) {
                            ttc.marcarError(c, v.getMessage().trim());
                        }
                        if (primerCtrl[0] == null) primerCtrl[0] = c;
                    });
        }
        if (!erroresOrdenados.isEmpty()) {
            lbnMsg.setText(erroresOrdenados.entrySet().iterator().next().getValue());
            lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
            if (primerCtrl[0] != null) Platform.runLater(primerCtrl[0]::requestFocus);
        }
    }

    private void procesarFormulario() {
        lbnMsg.setText("Formulario válido");
        lbnMsg.setStyle("-fx-text-fill: green; -fx-font-size: 14px;");
        limpiarError();

        if (idProductoCE > 0L) {
            formulario.setIdProducto(idProductoCE);
            ps.update(idProductoCE, formulario);
            mostrarToast("¡Se actualizó el producto correctamente!");
        } else {
            ps.save(formulario);
            mostrarToast("¡Se guardó el producto correctamente!");
        }
        clearForm();
        listar();
    }

    private void mostrarToast(String msg) {
        if (stage != null) {
            double w = stage.getWidth() / 1.5;
            double h = stage.getHeight() / 2;
            Toast.showToast(stage, msg, 2000, w, h);
        }
    }

    public void limpiarError() {
        List.of(txtNombreProducto, cbxTipoProducto, txtPUnit, txtPUnitOld, txtUtilidad,
                txtStock, txtStockOld, cbxMarca, cbxCategoria, cbxUnidMedida)
                .forEach(c -> {
                    c.getStyleClass().remove("text-field-error");
                    ttc.limpiarCampo(c);
                });
    }

    @FXML
    public void clearForm() {
        txtNombreProducto.clear();
        cbxTipoProducto.getSelectionModel().clearSelection();
        txtPUnit.clear();
        txtPUnitOld.clear();
        txtUtilidad.clear();
        txtStock.clear();
        txtStockOld.clear();
        cbxMarca.getSelectionModel().clearSelection();
        cbxCategoria.getSelectionModel().clearSelection();
        cbxUnidMedida.getSelectionModel().clearSelection();
        idProductoCE = 0L;
        if (lbnMsg != null) lbnMsg.setText("");
        limpiarError();
    }

    public void editForm(Producto producto) {
        txtNombreProducto.setText(producto.getNombre());
        txtPUnit.setText(String.valueOf(producto.getPu()));
        txtPUnitOld.setText(producto.getPuold() != null ? String.valueOf(producto.getPuold()) : "0");
        txtUtilidad.setText(producto.getUtilidad() != null ? String.valueOf(producto.getUtilidad()) : "0");
        txtStock.setText(String.valueOf(producto.getStock()));
        txtStockOld.setText(producto.getStockold() != null ? String.valueOf(producto.getStockold()) : "0");

        if (producto.getTipoProducto() != null) {
            cbxTipoProducto.getSelectionModel().select(
                    cbxTipoProducto.getItems().stream()
                            .filter(m -> Objects.equals(m.getKey(), producto.getTipoProducto().name()))
                            .findFirst().orElse(null));
        }

        if (producto.getIdMarca() != null) {
            cbxMarca.getSelectionModel().select(
                    cbxMarca.getItems().stream()
                            .filter(m -> Objects.equals(m.getKey(), String.valueOf(producto.getIdMarca().getIdMarca())))
                            .findFirst().orElse(null));
        }

        if (producto.getIdCategoria() != null) {
            cbxCategoria.getSelectionModel().select(
                    cbxCategoria.getItems().stream()
                            .filter(c -> Objects.equals(c.getKey(), String.valueOf(producto.getIdCategoria().getIdCategoria())))
                            .findFirst().orElse(null));
        }

        if (producto.getIdUnidad() != null) {
            cbxUnidMedida.getSelectionModel().select(
                    cbxUnidMedida.getItems().stream()
                            .filter(u -> Objects.equals(u.getKey(), String.valueOf(producto.getIdUnidad().getIdUnidad())))
                            .findFirst().orElse(null));
        }

        idProductoCE = producto.getIdProducto();
        limpiarError();
    }
}
