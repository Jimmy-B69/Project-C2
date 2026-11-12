package pe.edu.upeu.fsocietysys.controller;

import pe.edu.upeu.fsocietysys.service.ICategoriaService;
import pe.edu.upeu.fsocietysys.service.IMarcaService;
import pe.edu.upeu.fsocietysys.service.IProductoService;
import pe.edu.upeu.fsocietysys.service.IProductoTecnologicoService;
import pe.edu.upeu.fsocietysys.service.IUnidadMedidaService;

/**
 * Controlador de Productos Tecnológicos para TechStore.
 * Demuestra el principio de Herencia y Reutilización de Código en POO,
 * extendiendo la funcionalidad base de ProductoController para gestionar
 * el catálogo tecnológico de Laptops, Computadoras y Componentes.
 */
public class ProductoTecnologicoController extends ProductoController {

    private IProductoTecnologicoService pts;

    public ProductoTecnologicoController(IMarcaService ms, ICategoriaService cs,
                                         IProductoService ps, IUnidadMedidaService ums) {
        super(ms, cs, ps, ums);
    }

    public ProductoTecnologicoController(IMarcaService ms, ICategoriaService cs,
                                         IProductoService ps, IUnidadMedidaService ums,
                                         IProductoTecnologicoService pts) {
        super(ms, cs, ps, ums);
        this.pts = pts;
    }

    public IProductoTecnologicoService getPts() {
        return pts;
    }

    public void setPts(IProductoTecnologicoService pts) {
        this.pts = pts;
    }
}
