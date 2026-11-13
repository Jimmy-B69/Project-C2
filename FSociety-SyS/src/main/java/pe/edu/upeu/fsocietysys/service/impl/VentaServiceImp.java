package pe.edu.upeu.fsocietysys.service.impl;

import pe.edu.upeu.fsocietysys.model.*;
import pe.edu.upeu.fsocietysys.repository.ICrudGenericoRepository;
import pe.edu.upeu.fsocietysys.repository.ProductoRepository;
import pe.edu.upeu.fsocietysys.repository.VentaRepository;
import pe.edu.upeu.fsocietysys.service.IVentaService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VentaServiceImp extends CrudGenericoServiceImp<Venta, Long> implements IVentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final List<VentCarrito> carrito = new ArrayList<>();
    private long detalleSecuencia = 1;
    private int correlativo = 1;

    public VentaServiceImp(VentaRepository ventaRepository, ProductoRepository productoRepository) {
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    protected ICrudGenericoRepository<Venta, Long> getRepo() {
        return ventaRepository;
    }

    @Override
    public List<VentCarrito> getCarrito() {
        return new ArrayList<>(carrito);
    }

    @Override
    public void agregarAlCarrito(VentCarrito item) {
        carrito.add(item);
    }

    @Override
    public void eliminarDelCarrito(int index) {
        if (index >= 0 && index < carrito.size()) {
            carrito.remove(index);
        }
    }

    @Override
    public void limpiarCarrito() {
        carrito.clear();
    }

    @Override
    public Venta generarVenta(Cliente cliente, Usuario usuario, String tipoDoc) {
        if (carrito.isEmpty()) {
            throw new IllegalStateException("El carrito de venta está vacío");
        }

        double subtotal = 0.0;
        List<VentaDetalle> detalles = new ArrayList<>();

        for (VentCarrito c : carrito) {
            subtotal += c.getPtotal();

            // Descontar stock del producto
            Producto prod = c.getIdProducto();
            if (prod != null) {
                double nuevoStock = Math.max(0.0, prod.getStock() - c.getCantidad());
                prod.setStockold(prod.getStock());
                prod.setStock(nuevoStock);
                productoRepository.update(prod);
            }

            VentaDetalle vd = VentaDetalle.builder()
                    .idVentaDetalle(detalleSecuencia++)
                    .idProducto(prod)
                    .pu(c.getPunitario())
                    .cantidad(c.getCantidad())
                    .descuento(0.0)
                    .subtotal(c.getPtotal())
                    .build();
            detalles.add(vd);
        }

        double igv = Math.round(subtotal * 0.18 * 100.0) / 100.0;

        Venta venta = Venta.builder()
                .dniruc(cliente)
                .idUsuario(usuario)
                .fechaGener(LocalDateTime.now())
                .detalleVenta(detalles)
                .build();

        venta.setPrecioBase(subtotal);
        venta.setPrecioTotal(subtotal);
        venta.setIgv(igv);
        venta.setTipoDoc(tipoDoc != null ? tipoDoc : "BOLETA");
        venta.setSerie("B001");
        venta.setNumDoc(String.format("%06d", correlativo++));

        for (VentaDetalle vd : detalles) {
            vd.setIdVenta(venta);
        }

        Venta guardada = ventaRepository.save(venta);
        limpiarCarrito();
        return guardada;
    }
}
