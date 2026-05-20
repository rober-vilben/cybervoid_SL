package com.cybervoid.service;

import com.cybervoid.dto.RegistroForm;
import com.cybervoid.model.Role;
import com.cybervoid.model.Usuario;
import com.cybervoid.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email.toLowerCase())
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    @Transactional
    public Usuario registrar(RegistroForm form) {
        String email = form.getEmail().toLowerCase();
        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Ya existe una cuenta con ese email");
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(form.getNombre());
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(form.getPassword()));
        usuario.setTelefono(form.getTelefono());
        usuario.setDireccion(form.getDireccion());
        usuario.setRol(Role.ROLE_USER);
        return usuarioRepository.save(usuario);
    }
}
