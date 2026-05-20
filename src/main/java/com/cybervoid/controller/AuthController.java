package com.cybervoid.controller;

import com.cybervoid.dto.RegistroForm;
import com.cybervoid.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {
    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("registroForm", new RegistroForm());
        return "auth/registro";
    }

    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute RegistroForm registroForm, BindingResult result,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "auth/registro";
        }
        try {
            usuarioService.registrar(registroForm);
            redirectAttributes.addFlashAttribute("ok", "Cuenta creada. Ya puedes iniciar sesion.");
            return "redirect:/login";
        } catch (IllegalArgumentException ex) {
            result.rejectValue("email", "duplicado", ex.getMessage());
            return "auth/registro";
        }
    }
}
