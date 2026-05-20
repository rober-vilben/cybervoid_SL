package com.cybervoid.controller;

import com.cybervoid.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final ProductoService productoService;

    public HomeController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("ofertas", productoService.listarOfertas().stream().limit(4).toList());
        return "index";
    }
}
