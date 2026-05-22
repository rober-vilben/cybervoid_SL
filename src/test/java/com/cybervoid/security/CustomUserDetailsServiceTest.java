package com.cybervoid.security;

import com.cybervoid.model.Role;
import com.cybervoid.model.Usuario;
import com.cybervoid.repository.UsuarioRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @Test
    void deberiaCargarUsuarioConAutoridadYEstadoActivo() {
        Usuario usuario = new Usuario();
        usuario.setEmail("admin@cybervoid.local");
        usuario.setPassword("{noop}admin123");
        usuario.setRol(Role.ROLE_ADMIN);
        usuario.setActivo(true);
        when(usuarioRepository.findByEmail("admin@cybervoid.local")).thenReturn(Optional.of(usuario));

        UserDetails userDetails = userDetailsService.loadUserByUsername("ADMIN@CYBERVOID.LOCAL");

        assertThat(userDetails.getUsername()).isEqualTo("admin@cybervoid.local");
        assertThat(userDetails.getPassword()).isEqualTo("{noop}admin123");
        assertThat(userDetails.isEnabled()).isTrue();
        assertThat(userDetails.getAuthorities())
            .extracting("authority")
            .containsExactly("ROLE_ADMIN");
        verify(usuarioRepository).findByEmail("admin@cybervoid.local");
    }

    @Test
    void deberiaMarcarComoDeshabilitadoCuandoUsuarioNoEstaActivo() {
        Usuario usuario = new Usuario();
        usuario.setEmail("cliente@cybervoid.local");
        usuario.setPassword("{noop}user123");
        usuario.setRol(Role.ROLE_USER);
        usuario.setActivo(false);
        when(usuarioRepository.findByEmail("cliente@cybervoid.local")).thenReturn(Optional.of(usuario));

        UserDetails userDetails = userDetailsService.loadUserByUsername("cliente@cybervoid.local");

        assertThat(userDetails.isEnabled()).isFalse();
    }

    @Test
    void deberiaLanzarUsernameNotFoundCuandoUsuarioNoExiste() {
        when(usuarioRepository.findByEmail("nadie@cybervoid.local")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("NADIE@CYBERVOID.LOCAL"))
            .isInstanceOf(UsernameNotFoundException.class)
            .hasMessage("Usuario no encontrado");
    }
}
