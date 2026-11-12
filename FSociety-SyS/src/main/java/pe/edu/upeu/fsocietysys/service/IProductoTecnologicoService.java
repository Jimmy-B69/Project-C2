package pe.edu.upeu.fsocietysys.service;

import pe.edu.upeu.fsocietysys.dto.ComboBoxOption;
import pe.edu.upeu.fsocietysys.model.ProductoTecnologico;

import java.util.List;

/**
 * Interfaz de servicio para la gestión de productos tecnológicos en TechStore.
 * Define el contrato de operaciones de negocio y consultas especializadas.
 */
public interface IProductoTecnologicoService extends ICrudGenericoService<ProductoTecnologico, Long> {
    List<ComboBoxOption> listarCombobox();
    List<ComboBoxOption> listarTipoProducto();
}
