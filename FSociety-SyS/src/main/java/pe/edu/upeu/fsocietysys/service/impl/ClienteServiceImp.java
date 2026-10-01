package pe.edu.upeu.fsocietysys.service.impl;

import pe.edu.upeu.fsocietysys.dto.ComboBoxOption;
import pe.edu.upeu.fsocietysys.model.Cliente;
import pe.edu.upeu.fsocietysys.repository.ClienteRepository;
import pe.edu.upeu.fsocietysys.repository.ICrudGenericoRepository;
import pe.edu.upeu.fsocietysys.service.IClienteService;

import java.util.ArrayList;
import java.util.List;

public class ClienteServiceImp extends CrudGenericoServiceImp<Cliente, String> implements IClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImp(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    protected ICrudGenericoRepository<Cliente, String> getRepo() {
        return clienteRepository;
    }

    @Override
    public List<ComboBoxOption> listarCombobox() {
        List<ComboBoxOption> listar = new ArrayList<>();
        for (Cliente c : clienteRepository.findAll()) {
            ComboBoxOption cb = new ComboBoxOption();
            cb.setKey(c.getDniruc());
            cb.setValue(c.getDniruc() + " - " + c.getNombres());
            listar.add(cb);
        }
        return listar;
    }
}
