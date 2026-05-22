package com.cybervoid.controller;

import com.cybervoid.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import com.cybervoid.security.SecurityConfig;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Test
    void deberiaMostrarFormularioLogin() throws Exception {
        mockMvc.perform(get("/login"))
            .andExpect(status().isOk())
            .andExpect(view().name("auth/login"));
    }

    @Test
    void deberiaMostrarFormularioRegistro() throws Exception {
        mockMvc.perform(get("/registro"))
            .andExpect(status().isOk())
            .andExpect(view().name("auth/registro"))
            .andExpect(model().attributeExists("registroForm"));
    }

    @Test
    void deberiaRegistrarUsuarioValidoYRedirigirALogin() throws Exception {
        mockMvc.perform(post("/registro")
                .with(csrf())
                .param("nombre", "Nuevo Cliente")
                .param("email", "nuevo@cybervoid.local")
                .param("password", "secreto123")
                .param("telefono", "+34 600 111 222")
                .param("direccion", "Calle Neon 7"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login"))
            .andExpect(flash().attribute("ok", "Cuenta creada. Ya puedes iniciar sesion."));

        verify(usuarioService).registrar(any());
    }

    @Test
    void deberiaVolverARegistroCuandoFormularioNoEsValido() throws Exception {
        mockMvc.perform(post("/registro")
                .with(csrf())
                .param("nombre", "")
                .param("email", "correo-no-valido")
                .param("password", "123"))
            .andExpect(status().isOk())
            .andExpect(view().name("auth/registro"))
            .andExpect(model().attributeHasFieldErrors("registroForm", "nombre", "email", "password"));

        verifyNoInteractions(usuarioService);
    }

    @Test
    void deberiaMostrarErrorCuandoEmailEstaDuplicado() throws Exception {
        doThrow(new IllegalArgumentException("Ya existe una cuenta con ese email"))
            .when(usuarioService).registrar(any());

        mockMvc.perform(post("/registro")
                .with(csrf())
                .param("nombre", "Nuevo Cliente")
                .param("email", "nuevo@cybervoid.local")
                .param("password", "secreto123"))
            .andExpect(status().isOk())
            .andExpect(view().name("auth/registro"))
            .andExpect(model().attributeHasFieldErrors("registroForm", "email"));
    }
}
