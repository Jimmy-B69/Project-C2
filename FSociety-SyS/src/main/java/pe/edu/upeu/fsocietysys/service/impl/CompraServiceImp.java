package pe.edu.upeu.fsocietysys.service.impl;

import pe.edu.upeu.fsocietysys.model.*;
import pe.edu.upeu.fsocietysys.repository.CompraRepository;
import pe.edu.upeu.fsocietysys.repository.ICrudGenericoRepository;
import pe.edu.upeu.fsocietysys.repository.ProductoRepository;
import pe.edu.upeu.fsocietysys.service.ICompraService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CompraServiceImp extends CrudGenericoServiceImp<Compra, Long> implements ICompraService {

    private final CompraRepository compraRepository;
    private final ProductoRepository productoRepository;
    private final List<CompCarrito> carrito = new ArrayList<>();
    private long detalleSecuencia = 1;
    private int correlativo = 1;

    public CompraServiceImp(CompraRepository compraRepository, ProductoRepository productoRepository) {
        this.compraRepository = compraRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    protected ICrudGenericoRepository<Compra, Long> getRepo() {
        return compraRepository;
    }

    @Override
    public List<CompCarrito> getCarrito() {
        return new ArrayList<>(carrito);
    }

    @Override
    public void agregarAlCarrito(CompCarrito item) {
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
    public Compra generarCompra(Proveedor proveedor, Usuario usuario, String tipoDoc) {
        if (carrito.isEmpty()) {
            throw new IllegalStateException("El carrito de compra está vacío");
        }

        double subtotal = 0.0;
        List<CompraDetalle> detalles = new ArrayList<>();

        for (CompCarrito c : carrito) {
            subtotal += c.getPtotal();

            // Aumentar stock del producto comprado
            Producto prod = c.getIdProducto();
            if (prod != null) {
                double nuevoStock = prod.getStock() + c.getCantidad();
                prod.setStockold(prod.getStock());
                prod.setStock(nuevoStock);
                prod.setPuold(prod.getPu());
                // El precio unitario de compra puede influir o actualizar precio
                productoRepository.update(prod);
            }

            CompraDetalle cd = CompraDetalle.builder()
                    .idCompraDetalle(detalleSecuencia++)
                    .idProducto(prod)
                    .pu(c.getPunitario())
                    .cantidad(c.getCantidad())
                    .subtotal(c.getPtotal())
                    .build();
            detalles.add(cd);
        }

        double igv = Math.round(subtotal * 0.18 * 100.0) / 100.0;

        Compra compra = Compra.builder()
                .idProveedor(proveedor)
                .idUsuario(usuario)
                .fechaComp(LocalDate.now())
                .fechaReg(LocalDate.now())
                .detalleCompra(detalles)
                .build();

        compra.setPrecioBase(subtotal);
        compra.setPrecioTotal(subtotal);
        compra.setIgv(igv);
        compra.setTipoDoc(tipoDoc != null ? tipoDoc : "FACTURA");
        compra.setSerie("F001");
        compra.setNumDoc(String.format("%06d", correlativo++));

        for (CompraDetalle cd : detalles) {
            cd.setIdCompra(compra);
        }

        Compra guardada = compraRepository.save(compra);
        limpiarCarrito();
        return guardada;
    }
}
