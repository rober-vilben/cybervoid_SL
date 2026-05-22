package com.cybervoid.service;

import com.cybervoid.dto.RegistroForm;
import com.cybervoid.model.Role;
import com.cybervoid.model.Usuario;
import com.cybervoid.repository.UsuarioRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void deberiaBuscarUsuarioNormalizandoEmailAMinusculas() {
        Usuario usuario = new Usuario();
        usuario.setEmail("cliente@cybervoid.local");
        when(usuarioRepository.findByEmail("cliente@cybervoid.local")).thenReturn(Optional.of(usuario));

        Usuario encontrado = usuarioService.buscarPorEmail("CLIENTE@CYBERVOID.LOCAL");

        assertThat(encontrado).isSameAs(usuario);
        verify(usuarioRepository).findByEmail("cliente@cybervoid.local");
    }

    @Test
    void deberiaLanzarExcepcionCuandoUsuarioNoExiste() {
        when(usuarioRepository.findByEmail("nadie@cybervoid.local")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.buscarPorEmail("NADIE@CYBERVOID.LOCAL"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Usuario no encontrado");
    }

    @Test
    void deberiaRegistrarUsuarioConRolUserPasswordCodificadaYEmailNormalizado() {
        RegistroForm form = registroForm();
        when(usuarioRepository.existsByEmail("nuevo@cybervoid.local")).thenReturn(false);
        when(passwordEncoder.encode("secreto123")).thenReturn("hash");
        when(usuarioRepository.save(org.mockito.ArgumentMatchers.any(Usuario.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Usuario registrado = usuarioService.registrar(form);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        assertThat(registrado).isSameAs(captor.getValue());
        assertThat(registrado.getNombre()).isEqualTo("Nuevo Cliente");
        assertThat(registrado.getEmail()).isEqualTo("nuevo@cybervoid.local");
        assertThat(registrado.getPassword()).isEqualTo("hash");
        assertThat(registrado.getTelefono()).isEqualTo("+34 600 111 222");
        assertThat(registrado.getDireccion()).isEqualTo("Calle Neon 7");
        assertThat(registrado.getRol()).isEqualTo(Role.ROLE_USER);
    }

    @Test
    void deberiaRechazarRegistroCuandoEmailYaExiste() {
        RegistroForm form = registroForm();
        when(usuarioRepository.existsByEmail("nuevo@cybervoid.local")).thenReturn(true);

        assertThatThrownBy(() -> usuarioService.registrar(form))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Ya existe una cuenta con ese email");
        verify(usuarioRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    private RegistroForm registroForm() {
        RegistroForm form = new RegistroForm();
        form.setNombre("Nuevo Cliente");
        form.setEmail("NUEVO@CYBERVOID.LOCAL");
        form.setPassword("secreto123");
        form.setTelefono("+34 600 111 222");
        form.setDireccion("Calle Neon 7");
        return form;
    }
}
