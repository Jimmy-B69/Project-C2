package pe.edu.upeu.fsocietysys.repository;

import pe.edu.upeu.fsocietysys.model.Perfil;

public class PerfilRepository extends AbstractJpaRepository<Perfil, Long> {
    private long sequence = 1;

    @Override
    protected Long getId(Perfil entity) {
        return entity.getIdPerfil();
    }

    @Override
    protected void setId(Perfil entity, Long id) {
        entity.setIdPerfil(id);
    }

    @Override
    protected Long generateId() {
        return sequence++;
    }

    public void seedData() {
        if (findAll().isEmpty()) {
            save(Perfil.builder().idPerfil(generateId()).nombre("Administrador").codigo("ADMIN").build());
            save(Perfil.builder().idPerfil(generateId()).nombre("Vendedor").codigo("VENDEDOR").build());
        }
    }
}
