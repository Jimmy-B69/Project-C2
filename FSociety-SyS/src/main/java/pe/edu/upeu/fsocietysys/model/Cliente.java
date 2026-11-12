package pe.edu.upeu.fsocietysys.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.fsocietysys.enums.TipoDocumento;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    @NotBlank(message = "El DNI/RUC es obligatorio")
    private String dniruc;
    @NotBlank(message = "El nombre es obligatorio")
    private String nombres;
    private TipoDocumento tipoDocumento;
    private String repLegal;
    private String direccion;
}
