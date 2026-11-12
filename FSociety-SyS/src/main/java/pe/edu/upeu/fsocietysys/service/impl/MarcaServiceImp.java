package pe.edu.upeu.fsocietysys.service.impl;

import pe.edu.upeu.fsocietysys.dto.ComboBoxOption;
import pe.edu.upeu.fsocietysys.model.Marca;
import pe.edu.upeu.fsocietysys.repository.ICrudGenericoRepository;
import pe.edu.upeu.fsocietysys.repository.MarcaRepository;
import pe.edu.upeu.fsocietysys.service.IMarcaService;

import java.util.ArrayList;
import java.util.List;

public class MarcaServiceImp extends CrudGenericoServiceImp<Marca, Long> implements IMarcaService {

    private final MarcaRepository marcaRepository;

    public MarcaServiceImp(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    @Override
    protected ICrudGenericoRepository<Marca, Long> getRepo() {
        return marcaRepository;
    }

    @Override
    public List<ComboBoxOption> listarCombobox() {
        List<ComboBoxOption> listar = new ArrayList<>();
        for (Marca m : marcaRepository.findAll()) {
            ComboBoxOption cb = new ComboBoxOption();
            cb.setKey(String.valueOf(m.getIdMarca()));
            cb.setValue(m.getNombre());
            listar.add(cb);
        }
        return listar;
    }
}
