package pe.edu.upeu.fsocietysys.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.fsocietysys.enums.Menus;
import pe.edu.upeu.fsocietysys.enums.TipoTab;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Acceso {
    String idAcceso;
    String urlAcceso;
    Menus menuNombre;
    String menuItemNombre;
    TipoTab nombreTab;
}
