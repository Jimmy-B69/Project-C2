package pe.edu.upeu.fsocietysys.repository;

import pe.edu.upeu.fsocietysys.model.Emisor;

public class EmisorRepository extends AbstractJpaRepository<Emisor, Long> {
    private long sequence = 1;

    @Override
    protected Long getId(Emisor entity) {
        return entity.getIdEmisor();
    }

    @Override
    protected void setId(Emisor entity, Long id) {
        entity.setIdEmisor(id);
    }

    @Override
    protected Long generateId() {
        return sequence++;
    }

    public void seedData() {
        if (findAll().isEmpty()) {
            save(Emisor.builder()
                    .idEmisor(generateId())
                    .ruc("20608976541")
                    .nombreComercial("TechStore S.A.C.")
                    .domicilioFiscal("Av. Circunvalación 850")
                    .distrito("San Román")
                    .provincia("Juliaca")
                    .departamento("Puno")
                    .build());
        }
    }
}
