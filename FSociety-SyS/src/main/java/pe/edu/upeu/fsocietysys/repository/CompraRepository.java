package pe.edu.upeu.fsocietysys.repository;

import pe.edu.upeu.fsocietysys.model.Compra;

public class CompraRepository extends AbstractJpaRepository<Compra, Long> {
    private long sequence = 1;

    @Override
    protected Long getId(Compra entity) {
        return entity.getId();
    }

    @Override
    protected void setId(Compra entity, Long id) {
        entity.setId(id);
    }

    @Override
    protected Long generateId() {
        return sequence++;
    }
}
