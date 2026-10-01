package pe.edu.upeu.fsocietysys.service;

import pe.edu.upeu.fsocietysys.dto.ComboBoxOption;
import pe.edu.upeu.fsocietysys.model.Marca;

import java.util.List;

public interface IMarcaService extends ICrudGenericoService<Marca, Long> {
    List<ComboBoxOption> listarCombobox();
}
