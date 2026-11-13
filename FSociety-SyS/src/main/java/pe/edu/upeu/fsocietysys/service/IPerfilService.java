package pe.edu.upeu.fsocietysys.service;

import pe.edu.upeu.fsocietysys.dto.ComboBoxOption;
import pe.edu.upeu.fsocietysys.model.Perfil;

import java.util.List;

public interface IPerfilService extends ICrudGenericoService<Perfil, Long> {
    List<ComboBoxOption> listarCombobox();
}
