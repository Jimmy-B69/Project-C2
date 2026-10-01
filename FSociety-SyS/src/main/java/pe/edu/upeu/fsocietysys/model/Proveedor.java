package pe.edu.upeu.fsocietysys.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor {
    private Long idProveedor;
    @NotBlank(message = "El DNI/RUC es obligatorio")
    private String dniruc;
    @NotBlank(message = "La razón social es obligatoria")
    private String nombresRaso;
    private String tipoDoc;
    private String celular;
    private String email;
    private String direccion;
}
