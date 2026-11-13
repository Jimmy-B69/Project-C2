package pe.edu.upeu.fsocietysys.service.impl;

import pe.edu.upeu.fsocietysys.dto.ComboBoxOption;
import pe.edu.upeu.fsocietysys.model.Perfil;
import pe.edu.upeu.fsocietysys.repository.ICrudGenericoRepository;
import pe.edu.upeu.fsocietysys.repository.PerfilRepository;
import pe.edu.upeu.fsocietysys.service.IPerfilService;

import java.util.ArrayList;
import java.util.List;

public class PerfilServiceImp extends CrudGenericoServiceImp<Perfil, Long> implements IPerfilService {

    private final PerfilRepository perfilRepository;

    public PerfilServiceImp(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    @Override
    protected ICrudGenericoRepository<Perfil, Long> getRepo() {
        return perfilRepository;
    }

    @Override
    public List<ComboBoxOption> listarCombobox() {
        List<ComboBoxOption> listar = new ArrayList<>();
        for (Perfil p : perfilRepository.findAll()) {
            ComboBoxOption cb = new ComboBoxOption();
            cb.setKey(String.valueOf(p.getIdPerfil()));
            cb.setValue(p.getNombre());
            listar.add(cb);
        }
        return listar;
    }
}
