package pe.edu.upeu.sysventas.repository;

import pe.edu.upeu.sysventas.model.Categoria;
import pe.edu.upeu.sysventas.model.Marca;
import pe.edu.upeu.sysventas.repository.AbstractJpaRepository;

public class CategoriaRepository extends AbstractJpaRepository<Categoria, Long>{
    private long sequence=1;

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
            save(new Categoria(generateId(), "Smartwatch"));
            save(new Categoria(generateId(), "Celulares"));
            save(new Categoria(generateId(), "Auriculares"));
            save(new Categoria(generateId(), "Artefactos Electricos"));

        }
    }
}
