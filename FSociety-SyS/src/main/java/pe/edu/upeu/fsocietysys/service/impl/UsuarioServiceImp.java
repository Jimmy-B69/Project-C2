package pe.edu.upeu.fsocietysys.service.impl;

import pe.edu.upeu.fsocietysys.model.Usuario;
import pe.edu.upeu.fsocietysys.repository.ICrudGenericoRepository;
import pe.edu.upeu.fsocietysys.repository.UsuarioRepository;
import pe.edu.upeu.fsocietysys.service.IUsuarioService;

public class UsuarioServiceImp extends CrudGenericoServiceImp<Usuario, Long> implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImp(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected ICrudGenericoRepository<Usuario, Long> getRepo() {
        return usuarioRepository;
    }

    @Override
    public Usuario login(String username, String clave) {
        return usuarioRepository.findAll().stream()
                .filter(u -> u.getUsuario().equalsIgnoreCase(username) && u.getClave().equals(clave))
                .findFirst()
                .orElse(null);
    }
}
