package com.cybervoid.service;

import com.cybervoid.model.Carrito;
import com.cybervoid.model.LineaCarrito;
import com.cybervoid.model.Pedido;
import com.cybervoid.model.PedidoEstado;
import com.cybervoid.model.Producto;
import com.cybervoid.model.Usuario;
import com.cybervoid.model.Venta;
import com.cybervoid.repository.CarritoRepository;
import com.cybervoid.repository.PedidoRepository;
import com.cybervoid.repository.ProductoRepository;
import com.cybervoid.repository.VentaRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarritoServiceTest {
    @Mock
    private CarritoRepository carritoRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private VentaRepository ventaRepository;

    @InjectMocks
    private CarritoService carritoService;

    @Test
    void deberiaCrearCarritoCuandoUsuarioNoTieneUno() {
        Usuario usuario = usuario();
        when(carritoRepository.findByUsuario(usuario)).thenReturn(Optional.empty());
        when(carritoRepository.save(org.mockito.ArgumentMatchers.any(Carrito.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Carrito carrito = carritoService.obtenerOCrear(usuario);

        assertThat(carrito.getUsuario()).isSameAs(usuario);
        verify(carritoRepository).save(carrito);
    }

    @Test
    void deberiaAnadirProductoNuevoAlCarrito() {
        Usuario usuario = usuario();
        Carrito carrito = new Carrito();
        carrito.setUsuario(usuario);
        Producto producto = producto(1L, "Camiseta Shadow Mesh", "32.90", 3);
        when(carritoRepository.findByUsuario(usuario)).thenReturn(Optional.of(carrito));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        carritoService.anadirProducto(usuario, 1L);

        assertThat(carrito.getLineas()).hasSize(1);
        assertThat(carrito.getLineas().get(0).getProducto()).isSameAs(producto);
        assertThat(carrito.getLineas().get(0).getCantidad()).isEqualTo(1);
        verify(carritoRepository).save(carrito);
    }

    @Test
    void deberiaIncrementarCantidadCuandoProductoYaEstaEnCarrito() {
        Usuario usuario = usuario();
        Producto producto = producto(1L, "Camiseta Shadow Mesh", "32.90", 3);
        Carrito carrito = carritoConLinea(usuario, producto, 10L, 1);
        when(carritoRepository.findByUsuario(usuario)).thenReturn(Optional.of(carrito));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        carritoService.anadirProducto(usuario, 1L);

        assertThat(carrito.getLineas().get(0).getCantidad()).isEqualTo(2);
        verify(carritoRepository).save(carrito);
    }

    @Test
    void deberiaRechazarProductoSinStock() {
        Usuario usuario = usuario();
        Producto producto = producto(1L, "Camiseta Shadow Mesh", "32.90", 0);
        when(carritoRepository.findByUsuario(usuario)).thenReturn(Optional.of(new Carrito()));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        assertThatThrownBy(() -> carritoService.anadirProducto(usuario, 1L))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("No hay stock disponible");
        verify(carritoRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void deberiaActualizarCantidadRespetandoMinimoUno() {
        Usuario usuario = usuario();
        Producto producto = producto(1L, "Camiseta Shadow Mesh", "32.90", 5);
        Carrito carrito = carritoConLinea(usuario, producto, 10L, 2);
        when(carritoRepository.findByUsuario(usuario)).thenReturn(Optional.of(carrito));

        carritoService.actualizarCantidad(usuario, 10L, 0);

        assertThat(carrito.getLineas().get(0).getCantidad()).isEqualTo(1);
    }

    @Test
    void deberiaLanzarExcepcionCuandoCantidadSuperaStock() {
        Usuario usuario = usuario();
        Producto producto = producto(1L, "Camiseta Shadow Mesh", "32.90", 2);
        Carrito carrito = carritoConLinea(usuario, producto, 10L, 1);
        when(carritoRepository.findByUsuario(usuario)).thenReturn(Optional.of(carrito));

        assertThatThrownBy(() -> carritoService.actualizarCantidad(usuario, 10L, 3))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Cantidad superior al stock disponible");
    }

    @Test
    void deberiaEliminarLineaDelCarrito() {
        Usuario usuario = usuario();
        Producto producto = producto(1L, "Camiseta Shadow Mesh", "32.90", 2);
        Carrito carrito = carritoConLinea(usuario, producto, 10L, 1);
        when(carritoRepository.findByUsuario(usuario)).thenReturn(Optional.of(carrito));

        carritoService.eliminarLinea(usuario, 10L);

        assertThat(carrito.getLineas()).isEmpty();
    }

    @Test
    void deberiaConfirmarPedidoCalcularTotalCrearVentaYVaciarCarrito() {
        Usuario usuario = usuario();
        Producto camiseta = producto(1L, "Camiseta Shadow Mesh", "32.90", 5);
        Producto sudadera = producto(2L, "Sudadera Void Pulse", "64.90", 4);
        Carrito carrito = new Carrito();
        carrito.setUsuario(usuario);
        carrito.getLineas().add(linea(carrito, camiseta, 10L, 2));
        carrito.getLineas().add(linea(carrito, sudadera, 11L, 1));
        when(carritoRepository.findByUsuario(usuario)).thenReturn(Optional.of(carrito));
        when(pedidoRepository.save(org.mockito.ArgumentMatchers.any(Pedido.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Pedido pedido = carritoService.confirmarPedido(usuario);

        assertThat(pedido.getUsuario()).isSameAs(usuario);
        assertThat(pedido.getDireccionEnvio()).isEqualTo("Calle Neon 42");
        assertThat(pedido.getEstado()).isEqualTo(PedidoEstado.PAGADO);
        assertThat(pedido.getCodigo()).startsWith("CV-");
        assertThat(pedido.getLineas()).hasSize(2);
        assertThat(pedido.getTotal()).isEqualByComparingTo("130.70");
        assertThat(camiseta.getStock()).isEqualTo(3);
        assertThat(sudadera.getStock()).isEqualTo(3);
        assertThat(carrito.getLineas()).isEmpty();

        ArgumentCaptor<Venta> ventaCaptor = ArgumentCaptor.forClass(Venta.class);
        verify(ventaRepository).save(ventaCaptor.capture());
        assertThat(ventaCaptor.getValue().getPedido()).isSameAs(pedido);
        assertThat(ventaCaptor.getValue().getTotal()).isEqualByComparingTo("130.70");
    }

    @Test
    void deberiaRechazarConfirmacionConCarritoVacio() {
        Usuario usuario = usuario();
        Carrito carrito = new Carrito();
        carrito.setUsuario(usuario);
        when(carritoRepository.findByUsuario(usuario)).thenReturn(Optional.of(carrito));

        assertThatThrownBy(() -> carritoService.confirmarPedido(usuario))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("El carrito está vacío");
        verify(pedidoRepository, never()).save(org.mockito.ArgumentMatchers.any());
        verify(ventaRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    private Usuario usuario() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Alex Void");
        usuario.setEmail("alex.void@example.com");
        usuario.setDireccion("Calle Neon 42");
        return usuario;
    }

    private Producto producto(Long id, String nombre, String precio, int stock) {
        Producto producto = new Producto();
        ReflectionTestUtils.setField(producto, "id", id);
        producto.setNombre(nombre);
        producto.setPrecio(new BigDecimal(precio));
        producto.setStock(stock);
        return producto;
    }

    private Carrito carritoConLinea(Usuario usuario, Producto producto, Long lineaId, int cantidad) {
        Carrito carrito = new Carrito();
        carrito.setUsuario(usuario);
        carrito.getLineas().add(linea(carrito, producto, lineaId, cantidad));
        return carrito;
    }

    private LineaCarrito linea(Carrito carrito, Producto producto, Long lineaId, int cantidad) {
        LineaCarrito linea = new LineaCarrito();
        ReflectionTestUtils.setField(linea, "id", lineaId);
        linea.setCarrito(carrito);
        linea.setProducto(producto);
        linea.setCantidad(cantidad);
        return linea;
    }
}
