package pe.edu.upeu.fsocietysys.model;

import lombok.*;

/**
 * Entidad CategoriaTech representativa del nuevo dominio TechStore.
 * Aplica herencia respecto a Categoria para especializar categorías de productos tecnológicos
 * (Laptops, Computadoras de Escritorio, Componentes, Periféricos, etc.).
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaTech extends Categoria {

    private String descripcion;

    public CategoriaTech(Long idCategoria, String nombre, String descripcion) {
        super(idCategoria, nombre);
        this.descripcion = descripcion;
    }

    public CategoriaTech(Long idCategoria, String nombre) {
        super(idCategoria, nombre);
        this.descripcion = "Categoría tecnológica general";
    }
}
