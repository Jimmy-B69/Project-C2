package pe.edu.upeu.sysventas.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.sysventas.model.Producto;
import pe.edu.upeu.sysventas.model.Proveedor;
import pe.edu.upeu.sysventas.model.Usuario;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompCarrito {
    private Long idCompcarrito;
    private Proveedor idProveedor;
    private Producto idProducto;
    private String nombreProducto;
    private Double cantidad;
    private Double punitario;
    private Double ptotal;
    private int estado;
    private Usuario idUsuario;
}