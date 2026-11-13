package pe.edu.upeu.fsocietysys.controller;

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
import pe.edu.upeu.fsocietysys.dto.ComboBoxOption;
import pe.edu.upeu.fsocietysys.model.*;
import pe.edu.upeu.fsocietysys.service.ICompraService;
import pe.edu.upeu.fsocietysys.service.IProductoService;
import pe.edu.upeu.fsocietysys.service.IProveedorService;
import pe.edu.upeu.fsocietysys.service.IUsuarioService;

import java.util.LinkedHashMap;
import java.util.function.Consumer;

public class CompraController {

    @FXML ComboBox<ComboBoxOption> cbxProveedor;
    @FXML ComboBox<ComboBoxOption> cbxProducto;
    @FXML ComboBox<String> cbxTipoDoc;
    @FXML TextField txtPrecioUnit, txtCantidad;
    @FXML TableView<CompCarrito> tableCarrito;
    @FXML TableView<Compra> tableHistorial;
    @FXML Label lblSubtotal, lblIgv, lblTotal, lbnMsg;
    @FXML private AnchorPane miContenedor;

    private Stage stage;
    private final ICompraService compS;
    private final IProveedorService provS;
    private final IProductoService prodS;
    private final IUsuarioService uS;

    private ObservableList<CompCarrito> listaCarrito = FXCollections.observableArrayList();
    private ObservableList<Compra> listaHistorial = FXCollections.observableArrayList();

    public CompraController(ICompraService compS, IProveedorService provS, IProductoService prodS, IUsuarioService uS) {
        this.compS = compS;
        this.provS = provS;
        this.prodS = prodS;
        this.uS = uS;
    }

    @FXML
    public void initialize() {
        miContenedor.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage s) {
                stage = s;
            }
        });

        cbxProveedor.getItems().addAll(provS.listarCombobox());
        new ComboBoxAutoComplete<>(cbxProveedor);

        cbxProducto.getItems().addAll(prodS.listarCombobox());
        new ComboBoxAutoComplete<>(cbxProducto);

        cbxTipoDoc.getItems().addAll("FACTURA", "BOLETA", "GUIA");
        cbxTipoDoc.getSelectionModel().selectFirst();

        cbxProducto.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                try {
                    Producto prod = prodS.findById(Long.parseLong(newV.getKey()));
                    if (prod != null) {
                        txtPrecioUnit.setText(String.valueOf(prod.getPu()));
                    }
                } catch (Exception ignored) {}
            }
        });

        TableViewHelper<CompCarrito> helperCarrito = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> colsCarrito = new LinkedHashMap<>();
        colsCarrito.put("Producto", new ColumnInfo("nombreProducto", 220.0));
        colsCarrito.put("P. Compra (S/)", new ColumnInfo("punitario", 120.0));
        colsCarrito.put("Cantidad", new ColumnInfo("cantidad", 90.0));
        colsCarrito.put("Total (S/)", new ColumnInfo("ptotal", 110.0));

        Consumer<CompCarrito> eliminarItemAction = item -> {
            int idx = listaCarrito.indexOf(item);
            if (idx >= 0) {
                compS.eliminarDelCarrito(idx);
                actualizarVistaCarrito();
            }
        };

        helperCarrito.addColumnsInOrderWithSize(tableCarrito, colsCarrito, item -> {}, eliminarItemAction);

        TableViewHelper<Compra> helperHistorial = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> colsHistorial = new LinkedHashMap<>();
        colsHistorial.put("ID", new ColumnInfo("id", 50.0));
        colsHistorial.put("Tipo", new ColumnInfo("tipoDoc", 80.0));
        colsHistorial.put("Serie", new ColumnInfo("serie", 70.0));
        colsHistorial.put("Número", new ColumnInfo("numDoc", 90.0));
        colsHistorial.put("Proveedor", new ColumnInfo("idProveedor.nombresRaso", 180.0));
        colsHistorial.put("Total (S/)", new ColumnInfo("precioTotal", 100.0));

        helperHistorial.addColumnsInOrderWithSize(tableHistorial, colsHistorial, c -> {}, c -> {
            compS.delete(c.getId());
            mostrarToast("¡Compra eliminada!");
            listarHistorial();
        });

        actualizarVistaCarrito();
        listarHistorial();
    }

    @FXML
    public void agregarAlCarrito() {
        if (cbxProducto.getSelectionModel().getSelectedItem() == null) {
            mostrarMensaje("Seleccione un producto para comprar");
            return;
        }

        double cantidad, pu;
        try {
            cantidad = Double.parseDouble(txtCantidad.getText().trim());
            pu = Double.parseDouble(txtPrecioUnit.getText().trim());
            if (cantidad <= 0 || pu <= 0) throw new NumberFormatException();
        } catch (Exception e) {
            mostrarMensaje("Ingrese cantidad y precio de compra válidos");
            return;
        }

        Long idProd = Long.parseLong(cbxProducto.getSelectionModel().getSelectedItem().getKey());
        Producto prod = prodS.findById(idProd);
        if (prod == null) {
            mostrarMensaje("Producto no encontrado");
            return;
        }

        double ptotal = Math.round(pu * cantidad * 100.0) / 100.0;

        CompCarrito item = CompCarrito.builder()
                .idProducto(prod)
                .nombreProducto(prod.getNombre())
                .punitario(pu)
                .cantidad(cantidad)
                .ptotal(ptotal)
                .estado(1)
                .build();

        compS.agregarAlCarrito(item);
        txtCantidad.clear();
        mostrarMensaje("");
        actualizarVistaCarrito();
        mostrarToast("Producto agregado al carrito de compra");
    }

    private void actualizarVistaCarrito() {
        tableCarrito.getItems().clear();
        listaCarrito = FXCollections.observableArrayList(compS.getCarrito());
        tableCarrito.getItems().addAll(listaCarrito);

        double subtotal = 0.0;
        for (CompCarrito c : listaCarrito) {
            subtotal += c.getPtotal();
        }
        double igv = Math.round(subtotal * 0.18 * 100.0) / 100.0;
        double total = Math.round((subtotal + igv) * 100.0) / 100.0;

        lblSubtotal.setText(String.format("S/ %.2f", subtotal));
        lblIgv.setText(String.format("S/ %.2f", igv));
        lblTotal.setText(String.format("S/ %.2f", total));
    }

    @FXML
    public void registrarCompra() {
        if (cbxProveedor.getSelectionModel().getSelectedItem() == null) {
            mostrarMensaje("Seleccione un proveedor para la compra");
            return;
        }

        if (compS.getCarrito().isEmpty()) {
            mostrarMensaje("El carrito de compras está vacío");
            return;
        }

        Long idProv = Long.parseLong(cbxProveedor.getSelectionModel().getSelectedItem().getKey());
        Proveedor proveedor = provS.findById(idProv);
        Usuario usuario = uS.findAll().stream().findFirst().orElse(null);
        String tipoDoc = cbxTipoDoc.getValue();

        try {
            Compra compra = compS.generarCompra(proveedor, usuario, tipoDoc);
            mostrarToast("¡Compra " + compra.getSerie() + "-" + compra.getNumDoc() + " registrada y stock actualizado!");
            actualizarVistaCarrito();
            listarHistorial();
            cbxProducto.getSelectionModel().clearSelection();
            txtPrecioUnit.clear();
            txtCantidad.clear();
            mostrarMensaje("");
        } catch (Exception e) {
            mostrarMensaje("Error al procesar la compra: " + e.getMessage());
        }
    }

    private void listarHistorial() {
        tableHistorial.getItems().clear();
        listaHistorial = FXCollections.observableArrayList(compS.findAll());
        tableHistorial.getItems().addAll(listaHistorial);
    }

    private void mostrarMensaje(String msg) {
        if (lbnMsg != null) {
            lbnMsg.setText(msg);
            lbnMsg.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        }
    }

    private void mostrarToast(String msg) {
        if (stage != null) {
            Toast.showToast(stage, msg, 2000, stage.getWidth() / 1.5, stage.getHeight() / 2);
        }
    }
}
