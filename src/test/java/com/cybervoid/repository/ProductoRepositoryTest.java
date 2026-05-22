package com.cybervoid.repository;

import com.cybervoid.model.Categoria;
import com.cybervoid.model.CategoriaTipo;
import com.cybervoid.model.Producto;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ProductoRepositoryTest {
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    void deberiaBuscarProductosActivosOrdenadosPorNombre() {
        Categoria categoria = categoriaRepository.save(new Categoria("Mujer", "mujer", CategoriaTipo.GENERO));
        productoRepository.save(producto("sku-z", "Zeta", categoria, true, null));
        productoRepository.save(producto("sku-a", "Alfa", categoria, true, null));
        productoRepository.save(producto("sku-inactivo", "Beta", categoria, false, null));

        List<Producto> productos = productoRepository.findByActivoTrueOrderByNombreAsc();

        assertThat(productos).extracting(Producto::getNombre).containsExactly("Alfa", "Zeta");
    }

    @Test
    void deberiaBuscarProductosActivosPorCategoria() {
        Categoria mujer = categoriaRepository.save(new Categoria("Mujer", "mujer", CategoriaTipo.GENERO));
        Categoria hombre = categoriaRepository.save(new Categoria("Hombre", "hombre", CategoriaTipo.GENERO));
        productoRepository.save(producto("sku-mujer", "Camiseta Mujer", mujer, true, null));
        productoRepository.save(producto("sku-hombre", "Camiseta Hombre", hombre, true, null));

        List<Producto> productos = productoRepository.findByActivoTrueAndCategoriaSlugOrderByNombreAsc("mujer");

        assertThat(productos).extracting(Producto::getNombre).containsExactly("Camiseta Mujer");
    }

    @Test
    void deberiaBuscarOfertasActivasConPrecioAnterior() {
        Categoria ofertas = categoriaRepository.save(new Categoria("Ofertas", "ofertas", CategoriaTipo.OFERTA));
        productoRepository.save(producto("sku-oferta", "Oferta Activa", ofertas, true, "39.90"));
        productoRepository.save(producto("sku-normal", "Normal", ofertas, true, null));
        productoRepository.save(producto("sku-oferta-inactiva", "Oferta Inactiva", ofertas, false, "49.90"));

        List<Producto> productos = productoRepository.findByActivoTrueAndPrecioAnteriorIsNotNullOrderByNombreAsc();

        assertThat(productos).extracting(Producto::getNombre).containsExactly("Oferta Activa");
    }

    private Producto producto(String sku, String nombre, Categoria categoria, boolean activo, String precioAnterior) {
        Producto producto = new Producto();
        producto.setSku(sku);
        producto.setNombre(nombre);
        producto.setPrecio(new BigDecimal("19.90"));
        producto.setPrecioAnterior(precioAnterior == null ? null : new BigDecimal(precioAnterior));
        producto.setStock(10);
        producto.setActivo(activo);
        producto.setCategoria(categoria);
        return producto;
    }
}
