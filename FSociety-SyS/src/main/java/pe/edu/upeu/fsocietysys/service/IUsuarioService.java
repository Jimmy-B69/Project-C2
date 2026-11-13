package pe.edu.upeu.fsocietysys.service;

import pe.edu.upeu.fsocietysys.model.Usuario;

public interface IUsuarioService extends ICrudGenericoService<Usuario, Long> {
    Usuario login(String username, String clave);
}
