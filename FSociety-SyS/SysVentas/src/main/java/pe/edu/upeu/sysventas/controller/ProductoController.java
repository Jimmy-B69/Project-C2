package pe.edu.upeu.sysventas.controller;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import pe.edu.upeu.sysventas.components.ColumnInfo;
import pe.edu.upeu.sysventas.components.TableViewHelper;
import pe.edu.upeu.sysventas.dto.ComboBoxOption;
import pe.edu.upeu.sysventas.enums.TipoProducto;
import pe.edu.upeu.sysventas.model.Producto;
import pe.edu.upeu.sysventas.service.ICategoriaService;
import pe.edu.upeu.sysventas.service.IMarcaService;
import pe.edu.upeu.sysventas.service.IProductoService;
import pe.edu.upeu.sysventas.service.IUnidadMedidaService;

import java.util.LinkedHashMap;
import java.util.function.Consumer;


@RequiredArgsConstructor
public class ProductoController {
    private final IMarcaService ms;
    private final ICategoriaService cs;
    private final IProductoService ps;
    private final IUnidadMedidaService us;

    @FXML
    ComboBox<ComboBoxOption>cbxTipoProducto,cbxMarca,cbxCategoria,cbxUnidMedida;

    @FXML
    TextField txtNombreProducto,txtPUnit,txtPUnitOld,txtUtilidad,txtStock,txtStockOld;

    @FXML
    TableView<Producto>tableView;
    ObservableList<Producto>listProducto;

    @FXML public void initialize(){
        cbxTipoProducto.getItems().addAll(ps.listarTipoProducto());
        cbxMarca.getItems().addAll(ms.listarCombobox());
        cbxCategoria.getItems().addAll(cs.listarCombobox());
        cbxUnidMedida.getItems().addAll(us.listarCombobox());
        TableViewHelper<Producto> tableViewHelper=new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns=new LinkedHashMap<>();
        columns.put("ID Pro",new ColumnInfo("idProducto",60.0));
        columns.put("Nombre",new ColumnInfo("nombre",200.0));

        Consumer<Producto>updatAction=producto -> {};
        tableViewHelper.addColumnsInOrderWithSize(tableView,columns,updatAction,updatAction);
        tableView.setTableMenuButtonVisible(true);
        listar();
    }

    public void listar(){

        try {
            tableView.getItems().clear();
            listProducto= FXCollections.observableArrayList(ps.findAll());
            tableView.getItems().addAll(listProducto);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onGuardar(Event e){
        Producto formulario=new Producto();
        formulario.setTipoProducto(TipoProducto.valueOf(cbxTipoProducto.getSelectionModel().getSelectedItem().getKey())
        );

        formulario.setNombre(txtNombreProducto.getText());
        formulario.setPu(Double.parseDouble(String.valueOf(txtNombreProducto.getText())));
        formulario.setPuold(0.0);
        formulario.setUtilidad(100.00);
        formulario.setStock(12.0);
        formulario.setStockold(0.0);
        formulario.setIdCategoria(cs.findById(Long.parseLong(cbxCategoria.getSelectionModel().getSelectedItem().getKey())));
        formulario.setIdMarca(ms.findById(Long.parseLong(cbxMarca.getSelectionModel().getSelectedItem().getKey())));
        formulario.setIdUnidad(us.findById(Long.parseLong(cbxUnidMedida.getSelectionModel().getSelectedItem().getKey())));

        ps.save(formulario);
        listar();
        System.out.println("Boton Guardar");


    }



}
