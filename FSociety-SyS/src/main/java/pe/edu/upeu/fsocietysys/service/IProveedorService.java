package pe.edu.upeu.fsocietysys.service;

import pe.edu.upeu.fsocietysys.dto.ComboBoxOption;
import pe.edu.upeu.fsocietysys.model.Proveedor;

import java.util.List;

public interface IProveedorService extends ICrudGenericoService<Proveedor, Long> {
    List<ComboBoxOption> listarCombobox();
}
