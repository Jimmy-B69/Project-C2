package pe.edu.upeu.fsocietysys.repository;

import pe.edu.upeu.fsocietysys.enums.TipoProducto;
import pe.edu.upeu.fsocietysys.model.Categoria;
import pe.edu.upeu.fsocietysys.model.Marca;
import pe.edu.upeu.fsocietysys.model.ProductoTecnologico;
import pe.edu.upeu.fsocietysys.model.UnidMedida;

/**
 * Repositorio especializado para ProductoTecnologico en TechStore.
 * Implementa ICrudGenericoRepository a través de AbstractJpaRepository.
 */
public class ProductoTecnologicoRepository extends AbstractJpaRepository<ProductoTecnologico, Long> {

    private long sequence = 1;

    @Override
    protected Long getId(ProductoTecnologico entity) {
        return entity.getIdProducto();
    }

    @Override
    protected void setId(ProductoTecnologico entity, Long id) {
        entity.setIdProducto(id);
    }

    @Override
    protected Long generateId() {
        return sequence++;
    }

    public void seedData(Marca asus, Marca msi, Marca lenovo, Categoria laptops, Categoria computadoras, Categoria componentes, UnidMedida unidad) {
        if (findAll().isEmpty()) {
            save(ProductoTecnologico.techBuilder()
                    .nombre("Laptop Asus ROG Strix G16")
                    .tipoProducto(TipoProducto.HARDWARE)
                    .pu(6899.00).puold(7299.00).utilidad(800.00).stock(12.0).stockold(15.0)
                    .idMarca(asus).idCategoria(laptops).idUnidad(unidad)
                    .especificaciones("Intel Core i9-14900HX, 32GB RAM DDR5, 1TB SSD NVMe Gen4, RTX 4070 8GB")
                    .garantiaMeses(24)
                    .build());

            save(ProductoTecnologico.techBuilder()
                    .nombre("PC Gamer Armada MSI Infinite X")
                    .tipoProducto(TipoProducto.HARDWARE)
                    .pu(8499.00).puold(8999.00).utilidad(1000.00).stock(6.0).stockold(8.0)
                    .idMarca(msi).idCategoria(computadoras).idUnidad(unidad)
                    .especificaciones("AMD Ryzen 7 7800X3D, 32GB DDR5 6000MHz, RTX 4080 Super 16GB, Liquid Cooler 360mm")
                    .garantiaMeses(36)
                    .build());

            save(ProductoTecnologico.techBuilder()
                    .nombre("Laptop Lenovo Legion Pro 5")
                    .tipoProducto(TipoProducto.HARDWARE)
                    .pu(5799.00).puold(6199.00).utilidad(650.00).stock(10.0).stockold(12.0)
                    .idMarca(lenovo).idCategoria(laptops).idUnidad(unidad)
                    .especificaciones("AMD Ryzen 7 7745HX, 16GB RAM DDR5, 1TB SSD, RTX 4060 8GB, Pantalla 165Hz WQXGA")
                    .garantiaMeses(24)
                    .build());
        }
    }
}
