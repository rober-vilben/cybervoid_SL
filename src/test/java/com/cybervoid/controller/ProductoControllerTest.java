package com.cybervoid.controller;

import com.cybervoid.model.Producto;
import com.cybervoid.security.SecurityConfig;
import com.cybervoid.service.ProductoService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ProductoController.class)
@Import(SecurityConfig.class)
class ProductoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Test
    void deberiaMostrarCatalogoMujer() throws Exception {
        Producto producto = new Producto();
        producto.setNombre("Camiseta Shadow Mesh");
        when(productoService.listarPorCategoria("mujer")).thenReturn(List.of(producto));

        mockMvc.perform(get("/mujer"))
            .andExpect(status().isOk())
            .andExpect(view().name("productos/catalogo"))
            .andExpect(model().attribute("titulo", "Mujer"))
            .andExpect(model().attribute("descripcion", containsString("Prendas")))
            .andExpect(model().attribute("productos", List.of(producto)));

        verify(productoService).listarPorCategoria("mujer");
    }

    @Test
    void deberiaMostrarOfertas() throws Exception {
        Producto producto = new Producto();
        producto.setNombre("Sudadera Signal Black");
        when(productoService.listarOfertas()).thenReturn(List.of(producto));

        mockMvc.perform(get("/ofertas"))
            .andExpect(status().isOk())
            .andExpect(view().name("productos/catalogo"))
            .andExpect(model().attribute("titulo", "Ofertas"))
            .andExpect(model().attribute("productos", List.of(producto)));

        verify(productoService).listarOfertas();
    }

    @Test
    void deberiaMostrarTemporadaConTituloCapitalizado() throws Exception {
        Producto producto = new Producto();
        producto.setNombre("Abrigo Tecnico Invierno");
        when(productoService.listarPorCategoria("invierno")).thenReturn(List.of(producto));

        mockMvc.perform(get("/temporada/invierno"))
            .andExpect(status().isOk())
            .andExpect(view().name("productos/catalogo"))
            .andExpect(model().attribute("titulo", "Invierno"))
            .andExpect(model().attribute("productos", List.of(producto)));

        verify(productoService).listarPorCategoria("invierno");
    }
}
