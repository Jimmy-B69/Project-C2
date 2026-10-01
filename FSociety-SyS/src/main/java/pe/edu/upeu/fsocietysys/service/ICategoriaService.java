package pe.edu.upeu.fsocietysys.service;

import pe.edu.upeu.fsocietysys.dto.ComboBoxOption;
import pe.edu.upeu.fsocietysys.model.Categoria;

import java.util.List;

public interface ICategoriaService extends ICrudGenericoService<Categoria, Long> {
    List<ComboBoxOption> listarCombobox();
}
