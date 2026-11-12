package pe.edu.upeu.fsocietysys.repository;

import pe.edu.upeu.fsocietysys.enums.TipoDocumento;
import pe.edu.upeu.fsocietysys.model.Cliente;

public class ClienteRepository extends AbstractJpaRepository<Cliente, String> {
    @Override
    protected String getId(Cliente entity) {
        return entity.getDniruc();
    }

    @Override
    protected void setId(Cliente entity, String id) {
        entity.setDniruc(id);
    }

    @Override
    protected String generateId() {
        throw new UnsupportedOperationException("El DNI/RUC lo define el usuario");
    }

    public void seedData() {
        if (findAll().isEmpty()) {
            save(Cliente.builder().dniruc("12345678").nombres("Ana Torres")
                    .tipoDocumento(TipoDocumento.DNI).direccion("Av. El Sol 120, Juliaca").build());
            save(Cliente.builder().dniruc("20123456789").nombres("Colegio San Martín")
                    .tipoDocumento(TipoDocumento.RUC).repLegal("Luis Quispe")
                    .direccion("Jr. Lima 450").build());
        }
    }
}
