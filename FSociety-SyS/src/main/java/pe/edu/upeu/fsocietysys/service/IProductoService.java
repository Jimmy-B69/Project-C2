package pe.edu.upeu.fsocietysys.service;

import pe.edu.upeu.fsocietysys.dto.ComboBoxOption;
import pe.edu.upeu.fsocietysys.model.Producto;

import java.util.List;

public interface IProductoService extends ICrudGenericoService<Producto, Long> {
    List<ComboBoxOption> listarTipoProducto();
    List<ComboBoxOption> listarCombobox();
}
