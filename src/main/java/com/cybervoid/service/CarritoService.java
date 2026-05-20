package com.cybervoid.service;

import com.cybervoid.model.*;
import com.cybervoid.repository.CarritoRepository;
import com.cybervoid.repository.PedidoRepository;
import com.cybervoid.repository.ProductoRepository;
import com.cybervoid.repository.VentaRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CarritoService {
    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;
    private final PedidoRepository pedidoRepository;
    private final VentaRepository ventaRepository;

    public CarritoService(CarritoRepository carritoRepository, ProductoRepository productoRepository,
                          PedidoRepository pedidoRepository, VentaRepository ventaRepository) {
        this.carritoRepository = carritoRepository;
        this.productoRepository = productoRepository;
        this.pedidoRepository = pedidoRepository;
        this.ventaRepository = ventaRepository;
    }

    @Transactional
    public Carrito obtenerOCrear(Usuario usuario) {
        return carritoRepository.findByUsuario(usuario).orElseGet(() -> {
            Carrito carrito = new Carrito();
            carrito.setUsuario(usuario);
            return carritoRepository.save(carrito);
        });
    }

    @Transactional
    public void anadirProducto(Usuario usuario, Long productoId) {
        Carrito carrito = obtenerOCrear(usuario);
        Producto producto = productoRepository.findById(productoId)
            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        if (producto.getStock() <= 0) {
            throw new IllegalStateException("No hay stock disponible");
        }
        LineaCarrito linea = carrito.getLineas().stream()
            .filter(actual -> actual.getProducto().getId().equals(productoId))
            .findFirst()
            .orElseGet(() -> {
                LineaCarrito nueva = new LineaCarrito();
                nueva.setCarrito(carrito);
                nueva.setProducto(producto);
                nueva.setCantidad(0);
                carrito.getLineas().add(nueva);
                return nueva;
            });
        if (linea.getCantidad() + 1 > producto.getStock()) {
            throw new IllegalStateException("Cantidad superior al stock disponible");
        }
        linea.setCantidad(linea.getCantidad() + 1);
        carrito.setActualizadoEn(LocalDateTime.now());
        carritoRepository.save(carrito);
    }

    @Transactional
    public void actualizarCantidad(Usuario usuario, Long lineaId, int cantidad) {
        Carrito carrito = obtenerOCrear(usuario);
        LineaCarrito linea = carrito.getLineas().stream()
            .filter(actual -> actual.getId().equals(lineaId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Linea de carrito no encontrada"));
        int nuevaCantidad = Math.max(1, cantidad);
        if (nuevaCantidad > linea.getProducto().getStock()) {
            throw new IllegalStateException("Cantidad superior al stock disponible");
        }
        linea.setCantidad(nuevaCantidad);
        carrito.setActualizadoEn(LocalDateTime.now());
    }

    @Transactional
    public void eliminarLinea(Usuario usuario, Long lineaId) {
        Carrito carrito = obtenerOCrear(usuario);
        carrito.getLineas().removeIf(linea -> linea.getId().equals(lineaId));
        carrito.setActualizadoEn(LocalDateTime.now());
    }

    @Transactional
    public Pedido confirmarPedido(Usuario usuario) {
        Carrito carrito = obtenerOCrear(usuario);
        if (carrito.getLineas().isEmpty()) {
            throw new IllegalStateException("El carrito está vacío");
        }
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setDireccionEnvio(usuario.getDireccion());
        pedido.setCodigo("CV-" + System.currentTimeMillis());

        BigDecimal total = BigDecimal.ZERO;
        for (LineaCarrito lineaCarrito : carrito.getLineas()) {
            Producto producto = lineaCarrito.getProducto();
            if (producto.getStock() < lineaCarrito.getCantidad()) {
                throw new IllegalStateException("Stock insuficiente para " + producto.getNombre());
            }
            producto.setStock(producto.getStock() - lineaCarrito.getCantidad());

            LineaPedido lineaPedido = new LineaPedido();
            lineaPedido.setPedido(pedido);
            lineaPedido.setProducto(producto);
            lineaPedido.setNombreProducto(producto.getNombre());
            lineaPedido.setPrecioUnitario(producto.getPrecio());
            lineaPedido.setCantidad(lineaCarrito.getCantidad());
            lineaPedido.setSubtotal(lineaCarrito.getSubtotal());
            pedido.getLineas().add(lineaPedido);
            total = total.add(lineaCarrito.getSubtotal());
        }
        pedido.setTotal(total);
        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        Venta venta = new Venta();
        venta.setPedido(pedidoGuardado);
        venta.setTotal(total);
        ventaRepository.save(venta);

        carrito.getLineas().clear();
        carrito.setActualizadoEn(LocalDateTime.now());
        return pedidoGuardado;
    }
}
