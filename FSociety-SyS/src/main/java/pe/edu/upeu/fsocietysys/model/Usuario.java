package pe.edu.upeu.fsocietysys.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    private Long idUsuario;
    @NotBlank(message = "El usuario es obligatorio")
    private String usuario;
    @NotBlank(message = "La clave es obligatoria")
    private String clave;
    @NotNull(message = "El perfil es obligatorio")
    private Perfil idPerfil;
    private String estado;
}
