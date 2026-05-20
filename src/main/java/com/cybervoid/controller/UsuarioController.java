package com.cybervoid.controller;

import com.cybervoid.model.Usuario;
import com.cybervoid.repository.PedidoRepository;
import com.cybervoid.service.UsuarioService;
import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final PedidoRepository pedidoRepository;

    public UsuarioController(UsuarioService usuarioService, PedidoRepository pedidoRepository) {
        this.usuarioService = usuarioService;
        this.pedidoRepository = pedidoRepository;
    }

    @GetMapping("/usuario")
    public String usuario(Model model, Principal principal) {
        Usuario usuario = usuarioService.buscarPorEmail(principal.getName());
        model.addAttribute("usuario", usuario);
        model.addAttribute("pedidos", pedidoRepository.findByUsuarioOrderByFechaDesc(usuario));
        return "usuario";
    }
}
