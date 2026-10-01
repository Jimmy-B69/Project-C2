package pe.edu.upeu.fsocietysys.service;

import pe.edu.upeu.fsocietysys.dto.ComboBoxOption;
import pe.edu.upeu.fsocietysys.model.UnidMedida;

import java.util.List;

public interface IUnidadMedidaService extends ICrudGenericoService<UnidMedida, Long> {
    List<ComboBoxOption> listarCombobox();
}
