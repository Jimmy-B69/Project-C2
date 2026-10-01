package pe.edu.upeu.fsocietysys.repository;

import pe.edu.upeu.fsocietysys.model.Categoria;

public class CategoriaRepository extends AbstractJpaRepository<Categoria, Long> {
    private long sequence = 1;

    @Override
    protected Long getId(Categoria entity) {
        return entity.getIdCategoria();
    }

    @Override
    protected void setId(Categoria entity, Long id) {
        entity.setIdCategoria(id);
    }

    @Override
    protected Long generateId() {
        return sequence++;
    }

    public void seedData() {
        if (findAll().isEmpty()) {
            save(new Categoria(generateId(), "Laptops"));
            save(new Categoria(generateId(), "Computadoras"));
            save(new Categoria(generateId(), "Componentes"));
            save(new Categoria(generateId(), "Periféricos"));
            save(new Categoria(generateId(), "Software"));
        }
    }
}
