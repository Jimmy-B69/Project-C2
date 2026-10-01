package pe.edu.upeu.sysventas.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.sysventas.model.Perfil;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    private Long idUsuario;
    private String usuario;
    private String clave;
    private Perfil idPerfil;
    private String estado;
}