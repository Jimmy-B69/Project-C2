package pe.edu.upeu.fsocietysys.service.impl;

import pe.edu.upeu.fsocietysys.dto.ComboBoxOption;
import pe.edu.upeu.fsocietysys.enums.TipoProducto;
import pe.edu.upeu.fsocietysys.model.Producto;
import pe.edu.upeu.fsocietysys.repository.ICrudGenericoRepository;
import pe.edu.upeu.fsocietysys.repository.ProductoRepository;
import pe.edu.upeu.fsocietysys.service.IProductoService;

import java.util.ArrayList;
import java.util.List;

public class ProductoServiceImp extends CrudGenericoServiceImp<Producto, Long> implements IProductoService {

    private final ProductoRepository productoRepository;

    public ProductoServiceImp(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    protected ICrudGenericoRepository<Producto, Long> getRepo() {
        return productoRepository;
    }

    @Override
    public List<ComboBoxOption> listarTipoProducto() {
        List<ComboBoxOption> listar = new ArrayList<>();
        for (TipoProducto tp : TipoProducto.values()) {
            ComboBoxOption cb = new ComboBoxOption();
            cb.setKey(String.valueOf(tp.name()));
            cb.setValue(tp.getDescripcion());
            listar.add(cb);
        }
        return listar;
    }

    @Override
    public List<ComboBoxOption> listarCombobox() {
        List<ComboBoxOption> listar = new ArrayList<>();
        for (Producto p : productoRepository.findAll()) {
            ComboBoxOption cb = new ComboBoxOption();
            cb.setKey(String.valueOf(p.getIdProducto()));
            cb.setValue(p.getNombre() + " (S/ " + p.getPu() + " - Stock: " + p.getStock() + ")");
            listar.add(cb);
        }
        return listar;
    }
}
