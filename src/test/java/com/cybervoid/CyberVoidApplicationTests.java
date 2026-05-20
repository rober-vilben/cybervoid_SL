package com.cybervoid;

import com.cybervoid.model.Carrito;
import com.cybervoid.model.Producto;
import com.cybervoid.model.Usuario;
import com.cybervoid.dto.RegistroForm;
import com.cybervoid.repository.ProductoRepository;
import com.cybervoid.repository.UsuarioRepository;
import com.cybervoid.service.CarritoService;
import com.cybervoid.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class CyberVoidApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CarritoService carritoService;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioService usuarioService;

    @Test
    void contextLoads() {
    }

    @Test
    void paginasPublicasRenderizan() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
        mockMvc.perform(get("/mujer")).andExpect(status().isOk());
        mockMvc.perform(get("/hombre")).andExpect(status().isOk());
        mockMvc.perform(get("/ofertas")).andExpect(status().isOk());
        mockMvc.perform(get("/login")).andExpect(status().isOk());
    }

    @Test
    void carritoRequiereAutenticacion() throws Exception {
        mockMvc.perform(get("/carrito")).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "alex.void@example.com", authorities = "ROLE_USER")
    void usuarioAutenticadoPuedeVerCarrito() throws Exception {
        mockMvc.perform(get("/carrito")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin@cybervoid.local", authorities = "ROLE_ADMIN")
    void administradorPuedeVerPanel() throws Exception {
        mockMvc.perform(get("/admin")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "alex.void@example.com", authorities = "ROLE_USER")
    void rutasNoDeclaradasQuedanBloqueadas() throws Exception {
        mockMvc.perform(get("/zona-interna")).andExpect(status().isForbidden());
    }

    @Test
    void noPermiteCantidadSuperiorAlStockEnBackend() {
        Usuario usuario = usuarioRepository.findByEmail("alex.void@example.com").orElseThrow();
        Producto producto = productoRepository.findBySku("w-shadow-mesh").orElseThrow();
        producto.setStock(1);
        productoRepository.save(producto);

        carritoService.anadirProducto(usuario, producto.getId());
        Carrito carrito = carritoService.obtenerOCrear(usuario);
        Long lineaId = carrito.getLineas().get(0).getId();

        assertThrows(IllegalStateException.class,
            () -> carritoService.actualizarCantidad(usuario, lineaId, 2));
    }

    @Test
    void registroControlaDuplicadosAunqueCambieMayusculas() {
        RegistroForm form = new RegistroForm();
        form.setNombre("Admin duplicado");
        form.setEmail("ADMIN@CYBERVOID.LOCAL");
        form.setPassword("admin123");

        assertThrows(IllegalArgumentException.class, () -> usuarioService.registrar(form));
    }
}
