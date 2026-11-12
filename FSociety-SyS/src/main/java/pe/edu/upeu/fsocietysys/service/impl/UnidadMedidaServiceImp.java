package pe.edu.upeu.fsocietysys.service.impl;

import pe.edu.upeu.fsocietysys.dto.ComboBoxOption;
import pe.edu.upeu.fsocietysys.model.UnidMedida;
import pe.edu.upeu.fsocietysys.repository.ICrudGenericoRepository;
import pe.edu.upeu.fsocietysys.repository.UnidadMedidaRepository;
import pe.edu.upeu.fsocietysys.service.IUnidadMedidaService;

import java.util.ArrayList;
import java.util.List;

public class UnidadMedidaServiceImp extends CrudGenericoServiceImp<UnidMedida, Long> implements IUnidadMedidaService {

    private final UnidadMedidaRepository unidadMedidaRepository;

    public UnidadMedidaServiceImp(UnidadMedidaRepository unidadMedidaRepository) {
        this.unidadMedidaRepository = unidadMedidaRepository;
    }

    @Override
    protected ICrudGenericoRepository<UnidMedida, Long> getRepo() {
        return unidadMedidaRepository;
    }

    @Override
    public List<ComboBoxOption> listarCombobox() {
        List<ComboBoxOption> listar = new ArrayList<>();
        for (UnidMedida u : unidadMedidaRepository.findAll()) {
            ComboBoxOption cb = new ComboBoxOption();
            cb.setKey(String.valueOf(u.getIdUnidad()));
            cb.setValue(u.getNombreMedida());
            listar.add(cb);
        }
        return listar;
    }
}
