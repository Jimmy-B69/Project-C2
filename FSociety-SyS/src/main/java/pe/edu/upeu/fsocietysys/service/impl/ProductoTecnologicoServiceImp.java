package pe.edu.upeu.fsocietysys.service.impl;

import pe.edu.upeu.fsocietysys.dto.ComboBoxOption;
import pe.edu.upeu.fsocietysys.enums.TipoProducto;
import pe.edu.upeu.fsocietysys.model.ProductoTecnologico;
import pe.edu.upeu.fsocietysys.repository.ICrudGenericoRepository;
import pe.edu.upeu.fsocietysys.repository.ProductoTecnologicoRepository;
import pe.edu.upeu.fsocietysys.service.IProductoTecnologicoService;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de servicio para ProductoTecnologico.
 * Hereda operaciones CRUD genéricas de CrudGenericoServiceImp e implementa IProductoTecnologicoService.
 */
public class ProductoTecnologicoServiceImp extends CrudGenericoServiceImp<ProductoTecnologico, Long> implements IProductoTecnologicoService {

    private final ProductoTecnologicoRepository productoTecnologicoRepository;

    public ProductoTecnologicoServiceImp(ProductoTecnologicoRepository productoTecnologicoRepository) {
        this.productoTecnologicoRepository = productoTecnologicoRepository;
    }

    @Override
    protected ICrudGenericoRepository<ProductoTecnologico, Long> getRepo() {
        return productoTecnologicoRepository;
    }

    @Override
    public List<ComboBoxOption> listarTipoProducto() {
        List<ComboBoxOption> listar = new ArrayList<>();
        for (TipoProducto tp : TipoProducto.values()) {
            ComboBoxOption cb = new ComboBoxOption();
            cb.setKey(tp.name());
            cb.setValue(tp.getDescripcion());
            listar.add(cb);
        }
        return listar;
    }

    @Override
    public List<ComboBoxOption> listarCombobox() {
        List<ComboBoxOption> listar = new ArrayList<>();
        for (ProductoTecnologico p : productoTecnologicoRepository.findAll()) {
            ComboBoxOption cb = new ComboBoxOption();
            cb.setKey(String.valueOf(p.getIdProducto()));
            cb.setValue(p.getNombre() + " (S/ " + p.getPu() + " - Stock: " + p.getStock() + ")");
            listar.add(cb);
        }
        return listar;
    }
}
