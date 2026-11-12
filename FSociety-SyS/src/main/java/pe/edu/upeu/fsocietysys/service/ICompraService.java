package pe.edu.upeu.fsocietysys.service;

import pe.edu.upeu.fsocietysys.model.CompCarrito;
import pe.edu.upeu.fsocietysys.model.Compra;
import pe.edu.upeu.fsocietysys.model.Proveedor;
import pe.edu.upeu.fsocietysys.model.Usuario;

import java.util.List;

public interface ICompraService extends ICrudGenericoService<Compra, Long> {
    List<CompCarrito> getCarrito();
    void agregarAlCarrito(CompCarrito item);
    void eliminarDelCarrito(int index);
    void limpiarCarrito();
    Compra generarCompra(Proveedor proveedor, Usuario usuario, String tipoDoc);
}
