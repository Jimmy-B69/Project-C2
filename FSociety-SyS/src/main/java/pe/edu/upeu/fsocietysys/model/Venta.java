package pe.edu.upeu.fsocietysys.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Venta extends DocumentoComercial {
    private Cliente dniruc;
    private Usuario idUsuario;
    private LocalDateTime fechaGener;
    private List<VentaDetalle> detalleVenta;

    @Override
    public double calcularTotal() {
        return getPrecioTotal() + getIgv();
    }
}
