package pe.edu.upeu.fsocietysys.repository;

import pe.edu.upeu.fsocietysys.enums.TipoProducto;
import pe.edu.upeu.fsocietysys.model.Categoria;
import pe.edu.upeu.fsocietysys.model.Marca;
import pe.edu.upeu.fsocietysys.model.Producto;
import pe.edu.upeu.fsocietysys.model.UnidMedida;

public class ProductoRepository extends AbstractJpaRepository<Producto, Long> {
    private long sequence = 1;

    @Override
    protected Long getId(Producto entity) {
        return entity.getIdProducto();
    }

    @Override
    protected void setId(Producto entity, Long id) {
        entity.setIdProducto(id);
    }

    @Override
    protected Long generateId() {
        return sequence++;
    }

    public void seedData(Marca asus, Marca msi, Marca lenovo, Marca hp, Marca corsair,
                         Marca kingston, Marca intel, Marca amd, Marca nvidia,
                         Categoria laptops, Categoria computadoras, Categoria componentes,
                         Categoria perifericos, UnidMedida unidad, UnidMedida kit) {
        if (findAll().isEmpty()) {
            // LAPTOPS
            save(Producto.builder().nombre("Laptop ASUS ROG Strix G16 (i9/32GB/RTX 4070)").tipoProducto(TipoProducto.HARDWARE)
                    .pu(6899.0).puold(7299.0).utilidad(800.0).stock(10.0).stockold(12.0)
                    .idMarca(asus).idCategoria(laptops).idUnidad(unidad).build());
            save(Producto.builder().nombre("Laptop Lenovo Legion Pro 5 (Ryzen 7/16GB/RTX 4060)").tipoProducto(TipoProducto.HARDWARE)
                    .pu(5499.0).puold(5899.0).utilidad(650.0).stock(14.0).stockold(15.0)
                    .idMarca(lenovo).idCategoria(laptops).idUnidad(unidad).build());
            save(Producto.builder().nombre("Laptop HP Victus 15 (Core i5/16GB/RTX 3050)").tipoProducto(TipoProducto.HARDWARE)
                    .pu(3299.0).puold(3599.0).utilidad(400.0).stock(18.0).stockold(20.0)
                    .idMarca(hp).idCategoria(laptops).idUnidad(unidad).build());

            // COMPUTADORAS
            save(Producto.builder().nombre("PC Gamer Armada MSI Infinite (R7-7800X3D/RTX 4080)").tipoProducto(TipoProducto.HARDWARE)
                    .pu(8799.0).puold(9299.0).utilidad(1100.0).stock(5.0).stockold(6.0)
                    .idMarca(msi).idCategoria(computadoras).idUnidad(unidad).build());
            save(Producto.builder().nombre("Workstation Pro ASUS Creator (Core i9-14900K/64GB/RTX 4090)").tipoProducto(TipoProducto.HARDWARE)
                    .pu(14999.0).puold(15800.0).utilidad(1800.0).stock(3.0).stockold(4.0)
                    .idMarca(asus).idCategoria(computadoras).idUnidad(unidad).build());

            // COMPONENTES
            save(Producto.builder().nombre("Tarjeta de Video NVIDIA GeForce RTX 4070 Ti 12GB").tipoProducto(TipoProducto.HARDWARE)
                    .pu(3899.0).puold(4199.0).utilidad(450.0).stock(15.0).stockold(20.0)
                    .idMarca(nvidia != null ? nvidia : asus).idCategoria(componentes).idUnidad(unidad).build());
            save(Producto.builder().nombre("Procesador AMD Ryzen 7 7800X3D (8C/16T, 5.0GHz)").tipoProducto(TipoProducto.HARDWARE)
                    .pu(1899.0).puold(2050.0).utilidad(220.0).stock(20.0).stockold(25.0)
                    .idMarca(amd).idCategoria(componentes).idUnidad(unidad).build());
            save(Producto.builder().nombre("Procesador Intel Core i7-14700K (20C/28T, 5.6GHz)").tipoProducto(TipoProducto.HARDWARE)
                    .pu(1799.0).puold(1950.0).utilidad(200.0).stock(16.0).stockold(20.0)
                    .idMarca(intel).idCategoria(componentes).idUnidad(unidad).build());
            save(Producto.builder().nombre("Memoria RAM Kingston Fury Beast 32GB (2x16GB) DDR5").tipoProducto(TipoProducto.HARDWARE)
                    .pu(549.0).puold(599.0).utilidad(70.0).stock(30.0).stockold(35.0)
                    .idMarca(kingston).idCategoria(componentes).idUnidad(kit != null ? kit : unidad).build());

            // PERIFÉRICOS
            save(Producto.builder().nombre("Teclado Mecánico Corsair K70 RGB Pro").tipoProducto(TipoProducto.ACCESORIO)
                    .pu(599.0).puold(649.0).utilidad(80.0).stock(25.0).stockold(30.0)
                    .idMarca(corsair).idCategoria(perifericos).idUnidad(unidad).build());
        }
    }
}
