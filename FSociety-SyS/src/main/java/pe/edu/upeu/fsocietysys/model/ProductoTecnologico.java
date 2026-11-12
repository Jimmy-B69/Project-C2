package pe.edu.upeu.fsocietysys.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pe.edu.upeu.fsocietysys.enums.TipoProducto;

/**
 * Entidad ProductoTecnologico representativa del nuevo dominio TechStore.
 * Demuestra el principio de Herencia y Polimorfismo en POO, especializando
 * a la entidad base Producto con atributos técnicos propios del rubro
 * tecnológico (especificaciones técnicas y meses de garantía).
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductoTecnologico extends Producto {

    private String especificaciones;
    private Integer garantiaMeses;

    public ProductoTecnologico(Long idProducto, String nombre, TipoProducto tipoProducto,
                               Double pu, Double puold, Double utilidad,
                               Double stock, Double stockold,
                               Categoria idCategoria, Marca idMarca, UnidMedida idUnidad,
                               String especificaciones, Integer garantiaMeses) {
        super(idProducto, nombre, tipoProducto, pu, puold, utilidad, stock, stockold, idCategoria, idMarca, idUnidad);
        this.especificaciones = especificaciones;
        this.garantiaMeses = garantiaMeses;
    }

    public static ProductoTecnologicoBuilder techBuilder() {
        return new ProductoTecnologicoBuilder();
    }

    public static class ProductoTecnologicoBuilder {
        private Long idProducto;
        private String nombre;
        private TipoProducto tipoProducto;
        private Double pu;
        private Double puold;
        private Double utilidad;
        private Double stock;
        private Double stockold;
        private Categoria idCategoria;
        private Marca idMarca;
        private UnidMedida idUnidad;
        private String especificaciones;
        private Integer garantiaMeses;

        public ProductoTecnologicoBuilder idProducto(Long idProducto) { this.idProducto = idProducto; return this; }
        public ProductoTecnologicoBuilder nombre(String nombre) { this.nombre = nombre; return this; }
        public ProductoTecnologicoBuilder tipoProducto(TipoProducto tipoProducto) { this.tipoProducto = tipoProducto; return this; }
        public ProductoTecnologicoBuilder pu(Double pu) { this.pu = pu; return this; }
        public ProductoTecnologicoBuilder puold(Double puold) { this.puold = puold; return this; }
        public ProductoTecnologicoBuilder utilidad(Double utilidad) { this.utilidad = utilidad; return this; }
        public ProductoTecnologicoBuilder stock(Double stock) { this.stock = stock; return this; }
        public ProductoTecnologicoBuilder stockold(Double stockold) { this.stockold = stockold; return this; }
        public ProductoTecnologicoBuilder idCategoria(Categoria idCategoria) { this.idCategoria = idCategoria; return this; }
        public ProductoTecnologicoBuilder idMarca(Marca idMarca) { this.idMarca = idMarca; return this; }
        public ProductoTecnologicoBuilder idUnidad(UnidMedida idUnidad) { this.idUnidad = idUnidad; return this; }
        public ProductoTecnologicoBuilder especificaciones(String especificaciones) { this.especificaciones = especificaciones; return this; }
        public ProductoTecnologicoBuilder garantiaMeses(Integer garantiaMeses) { this.garantiaMeses = garantiaMeses; return this; }

        public ProductoTecnologico build() {
            return new ProductoTecnologico(idProducto, nombre, tipoProducto, pu, puold, utilidad, stock, stockold, idCategoria, idMarca, idUnidad, especificaciones, garantiaMeses);
        }
    }
}
