package pe.edu.upeu.fsocietysys.repository;

import pe.edu.upeu.fsocietysys.model.Marca;

public class MarcaRepository extends AbstractJpaRepository<Marca, Long> {
    private long sequence = 1;

    @Override
    protected Long getId(Marca entity) {
        return entity.getIdMarca();
    }

    @Override
    protected void setId(Marca entity, Long id) {
        entity.setIdMarca(id);
    }

    @Override
    protected Long generateId() {
        return sequence++;
    }

    public void seedData() {
        if (findAll().isEmpty()) {
            save(new Marca(generateId(), "ASUS"));
            save(new Marca(generateId(), "MSI"));
            save(new Marca(generateId(), "Lenovo"));
            save(new Marca(generateId(), "HP"));
            save(new Marca(generateId(), "Corsair"));
            save(new Marca(generateId(), "Kingston"));
            save(new Marca(generateId(), "Intel"));
            save(new Marca(generateId(), "AMD"));
            save(new Marca(generateId(), "NVIDIA"));
        }
    }
}
