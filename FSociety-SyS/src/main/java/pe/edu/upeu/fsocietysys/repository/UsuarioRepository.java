package pe.edu.upeu.fsocietysys.repository;

import pe.edu.upeu.fsocietysys.model.Perfil;
import pe.edu.upeu.fsocietysys.model.Usuario;

public class UsuarioRepository extends AbstractJpaRepository<Usuario, Long> {
    private long sequence = 1;

    @Override
    protected Long getId(Usuario entity) {
        return entity.getIdUsuario();
    }

    @Override
    protected void setId(Usuario entity, Long id) {
        entity.setIdUsuario(id);
    }

    @Override
    protected Long generateId() {
        return sequence++;
    }

    public void seedData(Perfil admin, Perfil vendedor) {
        if (findAll().isEmpty()) {
            save(Usuario.builder()
                    .idUsuario(generateId())
                    .usuario("admin")
                    .clave("admin123")
                    .idPerfil(admin)
                    .estado("ACTIVO")
                    .build());
            save(Usuario.builder()
                    .idUsuario(generateId())
                    .usuario("vendedor")
                    .clave("123456")
                    .idPerfil(vendedor)
                    .estado("ACTIVO")
                    .build());
        }
    }
}
