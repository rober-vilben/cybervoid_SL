package com.cybervoid.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "lineas_carrito")
public class LineaCarrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Carrito carrito;

    @ManyToOne(fetch = FetchType.EAGER)
    private Producto producto;

    private int cantidad;

    public Long getId() { return id; }
    public Carrito getCarrito() { return carrito; }
    public void setCarrito(Carrito carrito) { this.carrito = carrito; }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public BigDecimal getSubtotal() {
        return producto.getPrecio().multiply(BigDecimal.valueOf(cantidad));
    }
}
