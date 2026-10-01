package pe.edu.upeu.fsocietysys;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upeu.fsocietysys.config.AppContext;
import pe.edu.upeu.fsocietysys.controller.*;
import pe.edu.upeu.fsocietysys.model.*;
import pe.edu.upeu.fsocietysys.repository.*;
import pe.edu.upeu.fsocietysys.service.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas Unitarias de Arquitectura y Dominio TechStore")
public class TechStoreTest {

    private static AppContext context;

    @BeforeAll
    public static void setUp() {
        context = AppContext.getInstance();
    }

    @Test
    @DisplayName("Verificar que AppContext contiene todos los Repositorios registrados")
    public void testRepositoriosRegistrados() {
        assertNotNull(context.getBean(CategoriaRepository.class));
        assertNotNull(context.getBean(MarcaRepository.class));
        assertNotNull(context.getBean(UnidadMedidaRepository.class));
        assertNotNull(context.getBean(ProductoRepository.class));
        assertNotNull(context.getBean(ProductoTecnologicoRepository.class));
        assertNotNull(context.getBean(ClienteRepository.class));
        assertNotNull(context.getBean(ProveedorRepository.class));
        assertNotNull(context.getBean(UsuarioRepository.class));
        assertNotNull(context.getBean(VentaRepository.class));
        assertNotNull(context.getBean(CompraRepository.class));
    }

    @Test
    @DisplayName("Verificar que AppContext contiene todos los Servicios registrados")
    public void testServiciosRegistrados() {
        assertNotNull(context.getBean(ICategoriaService.class));
        assertNotNull(context.getBean(IMarcaService.class));
        assertNotNull(context.getBean(IUnidadMedidaService.class));
        assertNotNull(context.getBean(IProductoService.class));
        assertNotNull(context.getBean(IProductoTecnologicoService.class));
        assertNotNull(context.getBean(IClienteService.class));
        assertNotNull(context.getBean(IProveedorService.class));
        assertNotNull(context.getBean(IUsuarioService.class));
        assertNotNull(context.getBean(IVentaService.class));
        assertNotNull(context.getBean(ICompraService.class));
    }

    @Test
    @DisplayName("Verificar que AppContext contiene los Controladores principales registrados")
    public void testControladoresRegistrados() {
        assertNotNull(context.getBean(MainguiController.class));
        assertNotNull(context.getBean(ProductoController.class));
        assertNotNull(context.getBean(ProductoTecnologicoController.class));
        assertNotNull(context.getBean(CategoriaController.class));
        assertNotNull(context.getBean(MarcaController.class));
        assertNotNull(context.getBean(UnidadMedidaController.class));
        assertNotNull(context.getBean(ClienteController.class));
        assertNotNull(context.getBean(VentaController.class));
    }

    @Test
    @DisplayName("Verificar que el catálogo cumple con el dominio TechStore: Laptops, Computadoras y Componentes")
    public void testCatalogoDominioTechStore() {
        IProductoService ps = context.getBean(IProductoService.class);
        List<Producto> productos = ps.findAll();
        assertFalse(productos.isEmpty(), "El catálogo tecnológico no debe estar vacío");

        boolean tieneLaptops = productos.stream().anyMatch(p -> p.getNombre().toLowerCase().contains("laptop"));
        boolean tieneComputadoras = productos.stream().anyMatch(p -> p.getNombre().toLowerCase().contains("pc") || p.getNombre().toLowerCase().contains("workstation"));
        boolean tieneComponentes = productos.stream().anyMatch(p -> p.getNombre().toLowerCase().contains("tarjeta") || p.getNombre().toLowerCase().contains("procesador") || p.getNombre().toLowerCase().contains("ram"));

        assertTrue(tieneLaptops, "Debe contener productos en la categoría Laptops");
        assertTrue(tieneComputadoras, "Debe contener productos en la categoría Computadoras");
        assertTrue(tieneComponentes, "Debe contener productos en la categoría Componentes");
    }

    @Test
    @DisplayName("Verificar herencia y especialización de ProductoTecnologico")
    public void testProductoTecnologicoHerencia() {
        ProductoTecnologico pt = ProductoTecnologico.techBuilder()
                .nombre("Laptop Test POO")
                .pu(3500.0)
                .stock(5.0)
                .especificaciones("Intel i7, 16GB RAM, 1TB SSD")
                .garantiaMeses(24)
                .build();

        assertTrue(pt instanceof Producto, "ProductoTecnologico debe heredar de Producto (Polimorfismo)");
        assertEquals("Intel i7, 16GB RAM, 1TB SSD", pt.getEspecificaciones());
        assertEquals(24, pt.getGarantiaMeses());
    }

    @Test
    @DisplayName("Verificar herencia y especialización de CategoriaTech")
    public void testCategoriaTechHerencia() {
        CategoriaTech ct = new CategoriaTech(100L, "Componentes Gamer", "Categoría especializada en hardware de alto rendimiento");
        assertTrue(ct instanceof Categoria, "CategoriaTech debe heredar de Categoria");
        assertEquals("Componentes Gamer", ct.getNombre());
        assertEquals("Categoría especializada en hardware de alto rendimiento", ct.getDescripcion());
    }
}
