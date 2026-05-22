package com.cybervoid.service;

import com.cybervoid.model.Producto;
import com.cybervoid.repository.ProductoRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {
    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void deberiaListarProductosActivosOrdenadosPorNombre() {
        Producto producto = new Producto();
        producto.setNombre("Chaqueta Night Shell");
        when(productoRepository.findByActivoTrueOrderByNombreAsc()).thenReturn(List.of(producto));

        List<Producto> productos = productoService.listarActivos();

        assertThat(productos).containsExactly(producto);
        verify(productoRepository).findByActivoTrueOrderByNombreAsc();
    }

    @Test
    void deberiaListarProductosPorCategoria() {
        Producto producto = new Producto();
        producto.setNombre("Camiseta Shadow Mesh");
        when(productoRepository.findByActivoTrueAndCategoriaSlugOrderByNombreAsc("mujer"))
            .thenReturn(List.of(producto));

        List<Producto> productos = productoService.listarPorCategoria("mujer");

        assertThat(productos).containsExactly(producto);
        verify(productoRepository).findByActivoTrueAndCategoriaSlugOrderByNombreAsc("mujer");
    }

    @Test
    void deberiaListarOfertas() {
        Producto producto = new Producto();
        producto.setNombre("Sudadera Signal Black");
        when(productoRepository.findByActivoTrueAndPrecioAnteriorIsNotNullOrderByNombreAsc())
            .thenReturn(List.of(producto));

        List<Producto> productos = productoService.listarOfertas();

        assertThat(productos).containsExactly(producto);
        verify(productoRepository).findByActivoTrueAndPrecioAnteriorIsNotNullOrderByNombreAsc();
    }

    @Test
    void deberiaBuscarProductoCuandoExiste() {
        Producto producto = new Producto();
        when(productoRepository.findById(10L)).thenReturn(Optional.of(producto));

        Producto encontrado = productoService.buscar(10L);

        assertThat(encontrado).isSameAs(producto);
    }

    @Test
    void deberiaLanzarExcepcionCuandoProductoNoExiste() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productoService.buscar(99L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Producto no encontrado");
    }
}
