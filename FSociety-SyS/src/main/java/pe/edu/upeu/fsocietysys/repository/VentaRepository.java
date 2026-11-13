package pe.edu.upeu.fsocietysys.repository;

import pe.edu.upeu.fsocietysys.model.Venta;

public class VentaRepository extends AbstractJpaRepository<Venta, Long> {
    private long sequence = 1;

    @Override
    protected Long getId(Venta entity) {
        return entity.getId();
    }

    @Override
    protected void setId(Venta entity, Long id) {
        entity.setId(id);
    }

    @Override
    protected Long generateId() {
        return sequence++;
    }
}
