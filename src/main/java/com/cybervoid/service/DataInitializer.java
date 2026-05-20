package com.cybervoid.service;

import com.cybervoid.model.*;
import com.cybervoid.repository.*;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final ProveedorRepository proveedorRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, ClienteRepository clienteRepository,
                           ProveedorRepository proveedorRepository, CategoriaRepository categoriaRepository,
                           ProductoRepository productoRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.proveedorRepository = proveedorRepository;
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        crearUsuarios();
        crearClientes();
        Proveedor proveedor = proveedorRepository.findByNombre("Neon Dropshipping")
            .orElseGet(() -> {
                Proveedor nuevo = new Proveedor();
                nuevo.setNombre("Neon Dropshipping");
                nuevo.setEmail("proveedor@cybervoid.local");
                nuevo.setTelefono("+34 910 000 100");
                nuevo.setDireccion("Poligono Logistico 12, Madrid");
                return proveedorRepository.save(nuevo);
            });
        Categoria mujer = categoria("Mujer", "mujer", CategoriaTipo.GENERO);
        Categoria hombre = categoria("Hombre", "hombre", CategoriaTipo.GENERO);
        Categoria ofertas = categoria("Ofertas", "ofertas", CategoriaTipo.OFERTA);
        Categoria primavera = categoria("Primavera", "primavera", CategoriaTipo.TEMPORADA);
        Categoria verano = categoria("Verano", "verano", CategoriaTipo.TEMPORADA);
        Categoria otono = categoria("Otono", "otono", CategoriaTipo.TEMPORADA);
        Categoria invierno = categoria("Invierno", "invierno", CategoriaTipo.TEMPORADA);

        producto("w-shadow-mesh", "Camiseta Shadow Mesh", "Corte entallado con panel tonal.", "S", "Negro", "32.90", null, 25, "assets/img/product-camiseta.svg", mujer, proveedor);
        producto("w-void-pulse", "Sudadera Void Pulse", "Capucha amplia y bajo ajustable.", "M", "Grafito", "64.90", null, 18, "assets/img/product-sudadera.svg", mujer, proveedor);
        producto("w-neo-cargo", "Pantalon Neo Cargo", "Pernera recta con bolsillos tecnicos.", "M", "Negro", "72.90", null, 16, "assets/img/product-pantalon.svg", mujer, proveedor);
        producto("w-mono-shell", "Chaqueta Mono Shell", "Capa ligera resistente al viento.", "L", "Humo", "89.90", null, 11, "assets/img/product-chaqueta.svg", mujer, proveedor);
        producto("w-data-sling", "Bolso Data Sling", "Formato compacto con correa ajustable.", "Unica", "Negro", "34.90", null, 30, "assets/img/product-accesorio.svg", mujer, proveedor);

        producto("m-black-protocol", "Camiseta Black Protocol", "Basico pesado con impresion tonal.", "L", "Negro", "34.90", null, 25, "assets/img/product-camiseta.svg", hombre, proveedor);
        producto("m-core-hood", "Sudadera Core Hood", "Interior suave y patron amplio.", "XL", "Grafito", "67.90", null, 18, "assets/img/product-sudadera.svg", hombre, proveedor);
        producto("m-utility-void", "Pantalon Utility Void", "Cargo funcional con cintura comoda.", "L", "Negro", "76.90", null, 16, "assets/img/product-pantalon.svg", hombre, proveedor);
        producto("m-night-shell", "Chaqueta Night Shell", "Exterior sobrio con cierre frontal.", "L", "Negro", "94.90", null, 11, "assets/img/product-chaqueta.svg", hombre, proveedor);
        producto("m-compact-grid", "Mochila Compact Grid", "Volumen diario con acabado mate.", "Unica", "Negro", "42.90", null, 30, "assets/img/product-accesorio.svg", hombre, proveedor);

        producto("sale-void-layer", "Camiseta Void Layer", "Algodon tecnico con grafica tonal.", "M", "Negro", "24.90", "39.90", 40, "assets/img/product-camiseta.svg", ofertas, proveedor);
        producto("sale-signal-black", "Sudadera Signal Black", "Capucha amplia y bolsillo frontal.", "L", "Grafito", "49.90", "69.90", 20, "assets/img/product-sudadera.svg", ofertas, proveedor);
        producto("spring-light-shell", "Capa Ligera Primavera", "Tejido transpirable para entretiempo.", "M", "Humo", "59.90", null, 14, "assets/img/season-primavera.svg", primavera, proveedor);
        producto("summer-dark-tee", "Camiseta Verano Dark", "Punto ligero para ciudad caliente.", "M", "Negro", "29.90", null, 22, "assets/img/season-verano.svg", verano, proveedor);
        producto("autumn-module-hood", "Sudadera Modular Otono", "Textura densa y bolsillo tecnico.", "L", "Grafito", "69.90", null, 13, "assets/img/season-otono.svg", otono, proveedor);
        producto("winter-night-coat", "Abrigo Tecnico Invierno", "Capa exterior de abrigo urbano.", "L", "Negro", "119.90", null, 8, "assets/img/season-invierno.svg", invierno, proveedor);
    }

    private void crearUsuarios() {
        if (!usuarioRepository.existsByEmail("admin@cybervoid.local")) {
            Usuario admin = new Usuario();
            admin.setNombre("Administrador CyberVoid");
            admin.setEmail("admin@cybervoid.local");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRol(Role.ROLE_ADMIN);
            admin.setDireccion("Sede CyberVoid, Madrid");
            usuarioRepository.save(admin);
        }
        if (!usuarioRepository.existsByEmail("alex.void@example.com")) {
            Usuario user = new Usuario();
            user.setNombre("Alex Void");
            user.setEmail("alex.void@example.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRol(Role.ROLE_USER);
            user.setTelefono("+34 600 000 000");
            user.setDireccion("Calle Neon 42, Madrid");
            usuarioRepository.save(user);
        }
    }

    private void crearClientes() {
        if (clienteRepository.count() == 0) {
            Cliente cliente = new Cliente();
            cliente.setNombre("Alex Void");
            cliente.setEmail("alex.void@example.com");
            cliente.setTelefono("+34 600 000 000");
            cliente.setDireccion("Calle Neon 42, Madrid");
            clienteRepository.save(cliente);
        }
    }

    private Categoria categoria(String nombre, String slug, CategoriaTipo tipo) {
        return categoriaRepository.findBySlug(slug).orElseGet(() -> categoriaRepository.save(new Categoria(nombre, slug, tipo)));
    }

    private void producto(String sku, String nombre, String descripcion, String talla, String color, String precio,
                          String precioAnterior, int stock, String imagen, Categoria categoria, Proveedor proveedor) {
        if (productoRepository.findBySku(sku).isPresent()) {
            return;
        }
        Producto producto = new Producto();
        producto.setSku(sku);
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setTalla(talla);
        producto.setColor(color);
        producto.setPrecio(new BigDecimal(precio));
        if (precioAnterior != null) {
            producto.setPrecioAnterior(new BigDecimal(precioAnterior));
        }
        producto.setStock(stock);
        producto.setImagenUrl(imagen);
        producto.setCategoria(categoria);
        producto.setProveedor(proveedor);
        productoRepository.save(producto);
    }
}
