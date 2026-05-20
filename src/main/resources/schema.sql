CREATE DATABASE IF NOT EXISTS cybervoid CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE cybervoid;

CREATE TABLE IF NOT EXISTS usuarios (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(120) NOT NULL,
  email VARCHAR(160) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  rol VARCHAR(30) NOT NULL,
  telefono VARCHAR(30),
  direccion VARCHAR(255),
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  creado_en DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS clientes (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(120) NOT NULL,
  email VARCHAR(160) NOT NULL UNIQUE,
  telefono VARCHAR(30),
  direccion VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS proveedores (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(120) NOT NULL,
  email VARCHAR(160) NOT NULL,
  telefono VARCHAR(30),
  direccion VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS categorias (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(80) NOT NULL UNIQUE,
  slug VARCHAR(80) NOT NULL UNIQUE,
  tipo VARCHAR(40) NOT NULL
);

CREATE TABLE IF NOT EXISTS productos (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  sku VARCHAR(80) NOT NULL UNIQUE,
  nombre VARCHAR(160) NOT NULL,
  descripcion VARCHAR(500),
  talla VARCHAR(30),
  color VARCHAR(60),
  precio DECIMAL(10,2) NOT NULL,
  precio_anterior DECIMAL(10,2),
  stock INT NOT NULL,
  imagen_url VARCHAR(255),
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  proveedor_id BIGINT,
  categoria_id BIGINT,
  CONSTRAINT fk_productos_proveedores FOREIGN KEY (proveedor_id) REFERENCES proveedores(id),
  CONSTRAINT fk_productos_categorias FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);

CREATE TABLE IF NOT EXISTS carritos (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  usuario_id BIGINT NOT NULL UNIQUE,
  actualizado_en DATETIME NOT NULL,
  CONSTRAINT fk_carritos_usuarios FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE TABLE IF NOT EXISTS lineas_carrito (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  carrito_id BIGINT NOT NULL,
  producto_id BIGINT NOT NULL,
  cantidad INT NOT NULL,
  CONSTRAINT fk_lineas_carrito_carritos FOREIGN KEY (carrito_id) REFERENCES carritos(id),
  CONSTRAINT fk_lineas_carrito_productos FOREIGN KEY (producto_id) REFERENCES productos(id)
);

CREATE TABLE IF NOT EXISTS pedidos (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  codigo VARCHAR(40) NOT NULL UNIQUE,
  usuario_id BIGINT NOT NULL,
  fecha DATETIME NOT NULL,
  estado VARCHAR(40) NOT NULL,
  total DECIMAL(10,2) NOT NULL,
  direccion_envio VARCHAR(255),
  CONSTRAINT fk_pedidos_usuarios FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE TABLE IF NOT EXISTS lineas_pedido (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  pedido_id BIGINT NOT NULL,
  producto_id BIGINT NOT NULL,
  nombre_producto VARCHAR(160) NOT NULL,
  precio_unitario DECIMAL(10,2) NOT NULL,
  cantidad INT NOT NULL,
  subtotal DECIMAL(10,2) NOT NULL,
  CONSTRAINT fk_lineas_pedido_pedidos FOREIGN KEY (pedido_id) REFERENCES pedidos(id),
  CONSTRAINT fk_lineas_pedido_productos FOREIGN KEY (producto_id) REFERENCES productos(id)
);

CREATE TABLE IF NOT EXISTS ventas (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  pedido_id BIGINT NOT NULL UNIQUE,
  fecha DATETIME NOT NULL,
  total DECIMAL(10,2) NOT NULL,
  metodo_pago VARCHAR(60) NOT NULL,
  CONSTRAINT fk_ventas_pedidos FOREIGN KEY (pedido_id) REFERENCES pedidos(id)
);

CREATE TABLE IF NOT EXISTS informes (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  titulo VARCHAR(160) NOT NULL,
  tipo VARCHAR(60) NOT NULL,
  generado_en DATETIME NOT NULL,
  contenido TEXT NOT NULL
);
