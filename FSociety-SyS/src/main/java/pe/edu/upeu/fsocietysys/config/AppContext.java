package pe.edu.upeu.fsocietysys.config;

import pe.edu.upeu.fsocietysys.controller.*;
import pe.edu.upeu.fsocietysys.model.*;
import pe.edu.upeu.fsocietysys.repository.*;
import pe.edu.upeu.fsocietysys.service.*;
import pe.edu.upeu.fsocietysys.service.impl.*;

import java.util.HashMap;
import java.util.Map;

public class AppContext {

    private static AppContext instance;

    public static synchronized AppContext getInstance() {
        if (instance == null) {
            instance = new AppContext();
        }
        return instance;
    }

    private final Map<Class<?>, Object> contenedor = new HashMap<>();

    private AppContext() {
        registrarRepositorios();
        registrarServicios();
        registrarControladores();
    }

    // CAPA 1 — REPOSITORIOS
    private void registrarRepositorios() {
        CategoriaRepository cr = new CategoriaRepository();
        MarcaRepository mr = new MarcaRepository();
        UnidadMedidaRepository umr = new UnidadMedidaRepository();
        ProductoRepository pr = new ProductoRepository();
        ProductoTecnologicoRepository ptr = new ProductoTecnologicoRepository();
        ClienteRepository clir = new ClienteRepository();
        ProveedorRepository provr = new ProveedorRepository();
        PerfilRepository perfilr = new PerfilRepository();
        UsuarioRepository usr = new UsuarioRepository();
        VentaRepository vr = new VentaRepository();
        CompraRepository compr = new CompraRepository();
        EmisorRepository emisorr = new EmisorRepository();

        // Cargar semillas de datos iniciales
        cr.seedData();
        mr.seedData();
        umr.seedData();

        Marca asus = mr.findAll().stream().filter(m -> "ASUS".equalsIgnoreCase(m.getNombre())).findFirst().orElse(null);
        Marca msi = mr.findAll().stream().filter(m -> "MSI".equalsIgnoreCase(m.getNombre())).findFirst().orElse(null);
        Marca lenovo = mr.findAll().stream().filter(m -> "Lenovo".equalsIgnoreCase(m.getNombre())).findFirst().orElse(null);
        Marca hp = mr.findAll().stream().filter(m -> "HP".equalsIgnoreCase(m.getNombre())).findFirst().orElse(null);
        Marca corsair = mr.findAll().stream().filter(m -> "Corsair".equalsIgnoreCase(m.getNombre())).findFirst().orElse(null);
        Marca kingston = mr.findAll().stream().filter(m -> "Kingston".equalsIgnoreCase(m.getNombre())).findFirst().orElse(null);
        Marca intel = mr.findAll().stream().filter(m -> "Intel".equalsIgnoreCase(m.getNombre())).findFirst().orElse(null);
        Marca amd = mr.findAll().stream().filter(m -> "AMD".equalsIgnoreCase(m.getNombre())).findFirst().orElse(null);
        Marca nvidia = mr.findAll().stream().filter(m -> "NVIDIA".equalsIgnoreCase(m.getNombre())).findFirst().orElse(null);

        Categoria laptops = cr.findAll().stream().filter(c -> "Laptops".equalsIgnoreCase(c.getNombre())).findFirst().orElse(null);
        Categoria computadoras = cr.findAll().stream().filter(c -> "Computadoras".equalsIgnoreCase(c.getNombre())).findFirst().orElse(null);
        Categoria componentes = cr.findAll().stream().filter(c -> "Componentes".equalsIgnoreCase(c.getNombre())).findFirst().orElse(null);
        Categoria perifericos = cr.findAll().stream().filter(c -> "Periféricos".equalsIgnoreCase(c.getNombre())).findFirst().orElse(null);

        UnidMedida unidad = umr.findAll().stream().filter(u -> "Unidad".equalsIgnoreCase(u.getNombreMedida())).findFirst().orElse(null);
        UnidMedida kit = umr.findAll().stream().filter(u -> "Kit".equalsIgnoreCase(u.getNombreMedida())).findFirst().orElse(unidad);

        pr.seedData(asus, msi, lenovo, hp, corsair, kingston, intel, amd, nvidia,
                laptops, computadoras, componentes, perifericos, unidad, kit);
        ptr.seedData(asus, msi, lenovo, laptops, computadoras, componentes, unidad);

        clir.seedData();
        provr.seedData();
        perfilr.seedData();

        Perfil adminP = perfilr.findAll().stream().filter(p -> "ADMIN".equalsIgnoreCase(p.getCodigo())).findFirst().orElse(null);
        Perfil vendP = perfilr.findAll().stream().filter(p -> "VENDEDOR".equalsIgnoreCase(p.getCodigo())).findFirst().orElse(null);
        usr.seedData(adminP, vendP);
        emisorr.seedData();

        registrar(CategoriaRepository.class, cr);
        registrar(MarcaRepository.class, mr);
        registrar(UnidadMedidaRepository.class, umr);
        registrar(ProductoRepository.class, pr);
        registrar(ProductoTecnologicoRepository.class, ptr);
        registrar(ClienteRepository.class, clir);
        registrar(ProveedorRepository.class, provr);
        registrar(PerfilRepository.class, perfilr);
        registrar(UsuarioRepository.class, usr);
        registrar(VentaRepository.class, vr);
        registrar(CompraRepository.class, compr);
        registrar(EmisorRepository.class, emisorr);
    }

    // CAPA 2 — SERVICIOS
    private void registrarServicios() {
        registrar(ICategoriaService.class, new CategoriaServiceImp(getBean(CategoriaRepository.class)));
        registrar(IMarcaService.class, new MarcaServiceImp(getBean(MarcaRepository.class)));
        registrar(IUnidadMedidaService.class, new UnidadMedidaServiceImp(getBean(UnidadMedidaRepository.class)));
        registrar(IProductoService.class, new ProductoServiceImp(getBean(ProductoRepository.class)));
        registrar(IProductoTecnologicoService.class, new ProductoTecnologicoServiceImp(getBean(ProductoTecnologicoRepository.class)));
        registrar(IClienteService.class, new ClienteServiceImp(getBean(ClienteRepository.class)));
        registrar(IProveedorService.class, new ProveedorServiceImp(getBean(ProveedorRepository.class)));
        registrar(IPerfilService.class, new PerfilServiceImp(getBean(PerfilRepository.class)));
        registrar(IUsuarioService.class, new UsuarioServiceImp(getBean(UsuarioRepository.class)));
        registrar(IVentaService.class, new VentaServiceImp(getBean(VentaRepository.class), getBean(ProductoRepository.class)));
        registrar(ICompraService.class, new CompraServiceImp(getBean(CompraRepository.class), getBean(ProductoRepository.class)));
    }

    // CAPA 3 — CONTROLADORES
    private void registrarControladores() {
        registrar(MainguiController.class, new MainguiController());
        registrar(ProductoController.class, new ProductoController(
                getBean(IMarcaService.class),
                getBean(ICategoriaService.class),
                getBean(IProductoService.class),
                getBean(IUnidadMedidaService.class)));
        registrar(ProductoTecnologicoController.class, new ProductoTecnologicoController(
                getBean(IMarcaService.class),
                getBean(ICategoriaService.class),
                getBean(IProductoService.class),
                getBean(IUnidadMedidaService.class),
                getBean(IProductoTecnologicoService.class)));
        registrar(ClienteController.class, new ClienteController(getBean(IClienteService.class)));
        registrar(ProveedorController.class, new ProveedorController(getBean(IProveedorService.class)));
        registrar(CategoriaController.class, new CategoriaController(getBean(ICategoriaService.class)));
        registrar(MarcaController.class, new MarcaController(getBean(IMarcaService.class)));
        registrar(UnidadMedidaController.class, new UnidadMedidaController(getBean(IUnidadMedidaService.class)));
        registrar(UsuarioController.class, new UsuarioController(
                getBean(IUsuarioService.class),
                getBean(IPerfilService.class)));
        registrar(VentaController.class, new VentaController(
                getBean(IVentaService.class),
                getBean(IClienteService.class),
                getBean(IProductoService.class),
                getBean(IUsuarioService.class)));
        registrar(CompraController.class, new CompraController(
                getBean(ICompraService.class),
                getBean(IProveedorService.class),
                getBean(IProductoService.class),
                getBean(IUsuarioService.class)));
    }

    private void registrar(Class<?> tipo, Object bean) {
        contenedor.put(tipo, bean);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> tipo) {
        Object bean = contenedor.get(tipo);
        if (bean == null) {
            bean = contenedor.values().stream()
                    .filter(b -> tipo.isAssignableFrom(b.getClass()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException(
                            "Bean no encontrado: " + tipo.getName() + "\n→ ¿Lo registraste en AppContext?"));
        }
        return (T) bean;
    }
}
