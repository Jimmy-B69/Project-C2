package pe.edu.upeu.fsocietysys.service;

import pe.edu.upeu.fsocietysys.model.Cliente;
import pe.edu.upeu.fsocietysys.model.Usuario;
import pe.edu.upeu.fsocietysys.model.VentCarrito;
import pe.edu.upeu.fsocietysys.model.Venta;

import java.util.List;

public interface IVentaService extends ICrudGenericoService<Venta, Long> {
    List<VentCarrito> getCarrito();
    void agregarAlCarrito(VentCarrito item);
    void eliminarDelCarrito(int index);
    void limpiarCarrito();
    Venta generarVenta(Cliente cliente, Usuario usuario, String tipoDoc);
}
