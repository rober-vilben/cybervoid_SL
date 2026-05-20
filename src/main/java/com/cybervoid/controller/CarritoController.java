package com.cybervoid.controller;

import com.cybervoid.model.Usuario;
import com.cybervoid.service.CarritoService;
import com.cybervoid.service.UsuarioService;
import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/carrito")
public class CarritoController {
    private final CarritoService carritoService;
    private final UsuarioService usuarioService;

    public CarritoController(CarritoService carritoService, UsuarioService usuarioService) {
        this.carritoService = carritoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String ver(Model model, Principal principal) {
        Usuario usuario = usuarioService.buscarPorEmail(principal.getName());
        model.addAttribute("carrito", carritoService.obtenerOCrear(usuario));
        return "carrito";
    }

    @PostMapping("/anadir/{productoId}")
    public String anadir(@PathVariable Long productoId, Principal principal, RedirectAttributes redirectAttributes,
                         @RequestHeader(value = "Referer", required = false) String referer) {
        try {
            carritoService.anadirProducto(usuarioService.buscarPorEmail(principal.getName()), productoId);
            redirectAttributes.addFlashAttribute("ok", "Producto anadido al carrito");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:" + redireccionSegura(referer);
    }

    @PostMapping("/linea/{lineaId}")
    public String cantidad(@PathVariable Long lineaId, @RequestParam int cantidad, Principal principal,
                           RedirectAttributes redirectAttributes) {
        try {
            carritoService.actualizarCantidad(usuarioService.buscarPorEmail(principal.getName()), lineaId, cantidad);
            redirectAttributes.addFlashAttribute("ok", "Cantidad actualizada");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/carrito";
    }

    @PostMapping("/eliminar/{lineaId}")
    public String eliminar(@PathVariable Long lineaId, Principal principal) {
        carritoService.eliminarLinea(usuarioService.buscarPorEmail(principal.getName()), lineaId);
        return "redirect:/carrito";
    }

    @PostMapping("/confirmar")
    public String confirmar(Principal principal, RedirectAttributes redirectAttributes) {
        try {
            var pedido = carritoService.confirmarPedido(usuarioService.buscarPorEmail(principal.getName()));
            redirectAttributes.addFlashAttribute("ok", "Pedido " + pedido.getCodigo() + " confirmado");
            return "redirect:/usuario";
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/carrito";
        }
    }

    private String redireccionSegura(String referer) {
        if (referer != null && referer.startsWith("/") && !referer.startsWith("//")) {
            return referer;
        }
        return "/carrito";
    }
}
