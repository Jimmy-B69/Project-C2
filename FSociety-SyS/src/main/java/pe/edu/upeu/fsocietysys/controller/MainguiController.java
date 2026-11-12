package pe.edu.upeu.fsocietysys.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import pe.edu.upeu.fsocietysys.config.AppContext;

import java.io.IOException;
import java.util.Map;

public class MainguiController {

    @FXML
    BorderPane bp;
    @FXML
    MenuBar menuBar;
    @FXML
    MenuItem miProductos, miCategorias, miMarcas, miUnidades;
    @FXML
    MenuItem miClientes, miProveedores;
    @FXML
    MenuItem miVentas, miCompras;
    @FXML
    MenuItem miUsuarios;
    @FXML
    MenuItem miSalir;
    @FXML
    TabPane tabPane;

    @FXML
    public void initialize() {
        MenuItemListener miL = new MenuItemListener();
        if (miProductos != null) miProductos.setOnAction(miL::handle);
        if (miCategorias != null) miCategorias.setOnAction(miL::handle);
        if (miMarcas != null) miMarcas.setOnAction(miL::handle);
        if (miUnidades != null) miUnidades.setOnAction(miL::handle);
        if (miClientes != null) miClientes.setOnAction(miL::handle);
        if (miProveedores != null) miProveedores.setOnAction(miL::handle);
        if (miVentas != null) miVentas.setOnAction(miL::handle);
        if (miCompras != null) miCompras.setOnAction(miL::handle);
        if (miUsuarios != null) miUsuarios.setOnAction(miL::handle);
        if (miSalir != null) miSalir.setOnAction(miL::handle);

        // Abrir pestaña de Productos por defecto
        miL.abrirTabPaneFXML("/view/main_producto.fxml", "Adm. Productos");
    }

    class MenuItemListener {
        Map<String, String[]> menuConfig = Map.of(
                "miProductos", new String[]{"/view/main_producto.fxml", "Adm. Productos", "T"},
                "miCategorias", new String[]{"/view/main_categoria.fxml", "Categorías", "T"},
                "miMarcas", new String[]{"/view/main_marca.fxml", "Marcas", "T"},
                "miUnidades", new String[]{"/view/main_unidad_medida.fxml", "Unidades de Medida", "T"},
                "miClientes", new String[]{"/view/main_cliente.fxml", "Clientes", "T"},
                "miProveedores", new String[]{"/view/main_proveedor.fxml", "Proveedores", "T"},
                "miVentas", new String[]{"/view/main_venta.fxml", "Punto de Venta", "T"},
                "miCompras", new String[]{"/view/main_compra.fxml", "Compras", "T"},
                "miUsuarios", new String[]{"/view/main_usuario.fxml", "Usuarios", "T"},
                "miSalir", new String[]{"", "Salir", "C"}
        );

        public void handle(ActionEvent e) {
            String id = ((MenuItem) e.getSource()).getId();
            if (menuConfig.containsKey(id)) {
                String[] items = menuConfig.get(id);
                if ("C".equals(items[2])) {
                    Platform.exit();
                    System.exit(0);
                } else {
                    abrirTabPaneFXML(items[0], items[1]);
                }
            }
        }

        public void abrirTabPaneFXML(String fxmlPath, String title) {
            try {
                // Verificar si ya existe un tab con ese título para seleccionarlo
                for (Tab tab : tabPane.getTabs()) {
                    if (tab.getText().equalsIgnoreCase(title)) {
                        tabPane.getSelectionModel().select(tab);
                        return;
                    }
                }

                AppContext context = AppContext.getInstance();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
                fxmlLoader.setControllerFactory(context::getBean);
                Parent root = fxmlLoader.load();

                ScrollPane scrollPane = new ScrollPane(root);
                scrollPane.setFitToWidth(true);
                scrollPane.setFitToHeight(true);
                Tab newTab = new Tab(title, scrollPane);
                newTab.setClosable(true);
                tabPane.getTabs().add(newTab);
                tabPane.getSelectionModel().select(newTab);
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new RuntimeException("Error al cargar la vista: " + fxmlPath, ex);
            }
        }
    }
}
