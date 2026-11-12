package pe.edu.upeu.fsocietysys.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Compra extends DocumentoComercial {
    private Proveedor idProveedor;
    private Usuario idUsuario;
    private LocalDate fechaComp;
    private LocalDate fechaReg;
    private List<CompraDetalle> detalleCompra;

    @Override
    public double calcularTotal() {
        return getPrecioTotal() + getIgv();
    }
}
