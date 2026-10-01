package pe.edu.upeu.fsocietysys.repository;

import pe.edu.upeu.fsocietysys.model.Proveedor;

public class ProveedorRepository extends AbstractJpaRepository<Proveedor, Long> {
    private long sequence = 1;

    @Override
    protected Long getId(Proveedor entity) {
        return entity.getIdProveedor();
    }

    @Override
    protected void setId(Proveedor entity, Long id) {
        entity.setIdProveedor(id);
    }

    @Override
    protected Long generateId() {
        return sequence++;
    }

    public void seedData() {
        if (findAll().isEmpty()) {
            save(Proveedor.builder().dniruc("20555666777").nombresRaso("TechMayor SAC")
                    .tipoDoc("RUC").celular("951111111").email("ventas@techmayor.pe")
                    .direccion("Parque industrial, Juliaca").build());
        }
    }
}
