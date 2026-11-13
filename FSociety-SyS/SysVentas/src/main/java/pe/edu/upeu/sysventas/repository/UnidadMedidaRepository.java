package pe.edu.upeu.sysventas.repository;

import pe.edu.upeu.sysventas.model.Marca;
import pe.edu.upeu.sysventas.model.UnidMedida;
import pe.edu.upeu.sysventas.repository.AbstractJpaRepository;

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
    protected Long generateId() {return sequence++;}

    public void seedData() {
    if (findAll().isEmpty()) {
        save(new UnidMedida(generateId(), "unidades"));
        save(new UnidMedida(generateId(),"kilos"));
        save(new UnidMedida(generateId(),"litros"));
    }
  }
}
