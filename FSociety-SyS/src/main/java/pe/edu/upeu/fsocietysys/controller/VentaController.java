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
import pe.edu.upeu.fsocietysys.service.IClienteService;
import pe.edu.upeu.fsocietysys.service.IProductoService;
import pe.edu.upeu.fsocietysys.service.IUsuarioService;
import pe.edu.upeu.fsocietysys.service.IVentaService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Consumer;

public class VentaController {

    @FXML ComboBox<ComboBoxOption> cbxCliente;
    @FXML ComboBox<ComboBoxOption> cbxProducto;
    @FXML ComboBox<String> cbxTipoDoc;
    @FXML TextField txtPrecioUnit, txtStock, txtCantidad;
    @FXML TableView<VentCarrito> tableCarrito;
    @FXML TableView<Venta> tableHistorial;
    @FXML Label lblSubtotal, lblIgv, lblTotal, lbnMsg;
    @FXML private AnchorPane miContenedor;

    private Stage stage;
    private final IVentaService vs;
    private final IClienteService cs;
    private final IProductoService ps;
    private final IUsuarioService us;

    private ObservableList<VentCarrito> listaCarrito = FXCollections.observableArrayList();
    private ObservableList<Venta> listaHistorial = FXCollections.observableArrayList();

    public VentaController(IVentaService vs, IClienteService cs, IProductoService ps, IUsuarioService us) {
        this.vs = vs;
        this.cs = cs;
        this.ps = ps;
        this.us = us;
    }

    @FXML
    public void initialize() {
        miContenedor.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage s) {
                stage = s;
            }
        });

        // Configurar combos
        cbxCliente.getItems().addAll(cs.listarCombobox());
        new ComboBoxAutoComplete<>(cbxCliente);

        cbxProducto.getItems().addAll(ps.listarCombobox());
        new ComboBoxAutoComplete<>(cbxProducto);

        cbxTipoDoc.getItems().addAll("BOLETA", "FACTURA");
        cbxTipoDoc.getSelectionModel().selectFirst();

        cbxProducto.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                try {
                    Producto prod = ps.findById(Long.parseLong(newV.getKey()));
                    if (prod != null) {
                        txtPrecioUnit.setText(String.valueOf(prod.getPu()));
                        txtStock.setText(String.valueOf(prod.getStock()));
                    }
                } catch (Exception ignored) {}
            }
        });

        // Configurar tabla de carrito
        TableViewHelper<VentCarrito> helperCarrito = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> colsCarrito = new LinkedHashMap<>();
        colsCarrito.put("Producto", new ColumnInfo("nombreProducto", 220.0));
        colsCarrito.put("P. Unitario (S/)", new ColumnInfo("punitario", 120.0));
        colsCarrito.put("Cantidad", new ColumnInfo("cantidad", 90.0));
        colsCarrito.put("Total (S/)", new ColumnInfo("ptotal", 110.0));

        Consumer<VentCarrito> eliminarItemAction = item -> {
            int idx = listaCarrito.indexOf(item);
            if (idx >= 0) {
                vs.eliminarDelCarrito(idx);
                actualizarVistaCarrito();
            }
        };

        helperCarrito.addColumnsInOrderWithSize(tableCarrito, colsCarrito, item -> {}, eliminarItemAction);

        // Configurar tabla de historial
        TableViewHelper<Venta> helperHistorial = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> colsHistorial = new LinkedHashMap<>();
        colsHistorial.put("ID", new ColumnInfo("id", 50.0));
        colsHistorial.put("Tipo", new ColumnInfo("tipoDoc", 80.0));
        colsHistorial.put("Serie", new ColumnInfo("serie", 70.0));
        colsHistorial.put("Número", new ColumnInfo("numDoc", 90.0));
        colsHistorial.put("Cliente", new ColumnInfo("dniruc.nombres", 180.0));
        colsHistorial.put("Total (S/)", new ColumnInfo("precioTotal", 100.0));

        helperHistorial.addColumnsInOrderWithSize(tableHistorial, colsHistorial, v -> {}, v -> {
            vs.delete(v.getId());
            mostrarToast("¡Venta anulada!");
            listarHistorial();
        });

        actualizarVistaCarrito();
        listarHistorial();
    }

    @FXML
    public void agregarAlCarrito() {
        if (cbxProducto.getSelectionModel().getSelectedItem() == null) {
            mostrarMensaje("Seleccione un producto tecnológico");
            return;
        }

        double cantidad;
        try {
            cantidad = Double.parseDouble(txtCantidad.getText().trim());
            if (cantidad <= 0) throw new NumberFormatException();
        } catch (Exception e) {
            mostrarMensaje("Ingrese una cantidad válida mayor a 0");
            return;
        }

        Long idProd = Long.parseLong(cbxProducto.getSelectionModel().getSelectedItem().getKey());
        Producto prod = ps.findById(idProd);
        if (prod == null) {
            mostrarMensaje("Producto no encontrado");
            return;
        }

        if (prod.getStock() < cantidad) {
            mostrarMensaje("Stock insuficiente. Disponible: " + prod.getStock());
            return;
        }

        double pu = prod.getPu();
        double ptotal = Math.round(pu * cantidad * 100.0) / 100.0;

        VentCarrito item = VentCarrito.builder()
                .idProducto(prod)
                .nombreProducto(prod.getNombre())
                .punitario(pu)
                .cantidad(cantidad)
                .ptotal(ptotal)
                .estado(1)
                .build();

        vs.agregarAlCarrito(item);
        txtCantidad.clear();
        mostrarMensaje("");
        actualizarVistaCarrito();
        mostrarToast("Producto agregado al carrito");
    }

    private void actualizarVistaCarrito() {
        tableCarrito.getItems().clear();
        listaCarrito = FXCollections.observableArrayList(vs.getCarrito());
        tableCarrito.getItems().addAll(listaCarrito);

        double subtotal = 0.0;
        for (VentCarrito c : listaCarrito) {
            subtotal += c.getPtotal();
        }
        double igv = Math.round(subtotal * 0.18 * 100.0) / 100.0;
        double total = Math.round((subtotal + igv) * 100.0) / 100.0;

        lblSubtotal.setText(String.format("S/ %.2f", subtotal));
        lblIgv.setText(String.format("S/ %.2f", igv));
        lblTotal.setText(String.format("S/ %.2f", total));
    }

    @FXML
    public void registrarVenta() {
        if (cbxCliente.getSelectionModel().getSelectedItem() == null) {
            mostrarMensaje("Seleccione un cliente para la venta");
            return;
        }

        if (vs.getCarrito().isEmpty()) {
            mostrarMensaje("El carrito de compras está vacío");
            return;
        }

        String idCli = cbxCliente.getSelectionModel().getSelectedItem().getKey();
        Cliente cliente = cs.findById(idCli);

        // Tomar primer usuario activo como emisor de la venta
        Usuario usuario = us.findAll().stream().findFirst().orElse(null);
        String tipoDoc = cbxTipoDoc.getValue();

        try {
            Venta venta = vs.generarVenta(cliente, usuario, tipoDoc);
            mostrarToast("¡Venta " + venta.getSerie() + "-" + venta.getNumDoc() + " generada con éxito!");
            actualizarVistaCarrito();
            listarHistorial();
            cbxProducto.getSelectionModel().clearSelection();
            txtPrecioUnit.clear();
            txtStock.clear();
            txtCantidad.clear();
            mostrarMensaje("");
        } catch (Exception e) {
            mostrarMensaje("Error al procesar la venta: " + e.getMessage());
        }
    }

    private void listarHistorial() {
        tableHistorial.getItems().clear();
        listaHistorial = FXCollections.observableArrayList(vs.findAll());
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
