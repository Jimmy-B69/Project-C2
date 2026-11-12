package pe.edu.upeu.fsocietysys.repository;

import pe.edu.upeu.fsocietysys.model.UnidMedida;

public class UnidadMedidaRepository extends AbstractJpaRepository<UnidMedida, Long> {
    private long sequence = 1;

    @Override
    protected Long getId(UnidMedida entity) {
        return entity.getIdUnidad();
    }

    @Override
    protected void setId(UnidMedida entity, Long id) {
        entity.setIdUnidad(id);
    }

    @Override
    protected Long generateId() {
        return sequence++;
    }

    public void seedData() {
        if (findAll().isEmpty()) {
            save(new UnidMedida(generateId(), "Unidad"));
            save(new UnidMedida(generateId(), "Kit"));
            save(new UnidMedida(generateId(), "Pack"));
            save(new UnidMedida(generateId(), "Licencia"));
        }
    }
}
