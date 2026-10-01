package pe.edu.upeu.fsocietysys.service.impl;

import pe.edu.upeu.fsocietysys.dto.ComboBoxOption;
import pe.edu.upeu.fsocietysys.model.Proveedor;
import pe.edu.upeu.fsocietysys.repository.ICrudGenericoRepository;
import pe.edu.upeu.fsocietysys.repository.ProveedorRepository;
import pe.edu.upeu.fsocietysys.service.IProveedorService;

import java.util.ArrayList;
import java.util.List;

public class ProveedorServiceImp extends CrudGenericoServiceImp<Proveedor, Long> implements IProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorServiceImp(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    @Override
    protected ICrudGenericoRepository<Proveedor, Long> getRepo() {
        return proveedorRepository;
    }

    @Override
    public List<ComboBoxOption> listarCombobox() {
        List<ComboBoxOption> listar = new ArrayList<>();
        for (Proveedor p : proveedorRepository.findAll()) {
            ComboBoxOption cb = new ComboBoxOption();
            cb.setKey(String.valueOf(p.getIdProveedor()));
            cb.setValue(p.getDniruc() + " - " + p.getNombresRaso());
            listar.add(cb);
        }
        return listar;
    }
}
