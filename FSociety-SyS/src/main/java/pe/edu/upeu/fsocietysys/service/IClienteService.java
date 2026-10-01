package pe.edu.upeu.fsocietysys.service;

import pe.edu.upeu.fsocietysys.dto.ComboBoxOption;
import pe.edu.upeu.fsocietysys.model.Cliente;

import java.util.List;

public interface IClienteService extends ICrudGenericoService<Cliente, String> {
    List<ComboBoxOption> listarCombobox();
}
