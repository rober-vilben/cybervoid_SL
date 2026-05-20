package com.cybervoid.controller;

import com.cybervoid.model.Producto;
import com.cybervoid.repository.*;
import com.cybervoid.service.InformeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProveedorRepository proveedorRepository;
    private final ClienteRepository clienteRepository;
    private final PedidoRepository pedidoRepository;
    private final InformeRepository informeRepository;
    private final InformeService informeService;

    public AdminController(ProductoRepository productoRepository, CategoriaRepository categoriaRepository,
                           ProveedorRepository proveedorRepository, ClienteRepository clienteRepository,
                           PedidoRepository pedidoRepository, InformeRepository informeRepository,
                           InformeService informeService) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.proveedorRepository = proveedorRepository;
        this.clienteRepository = clienteRepository;
        this.pedidoRepository = pedidoRepository;
        this.informeRepository = informeRepository;
        this.informeService = informeService;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("productos", productoRepository.findAll());
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("proveedores", proveedorRepository.findAll());
        model.addAttribute("pedidos", pedidoRepository.findTop10ByOrderByFechaDesc());
        model.addAttribute("informes", informeRepository.findAll());
        return "admin/dashboard";
    }

    @GetMapping("/productos/nuevo")
    public String nuevoProducto(Model model) {
        model.addAttribute("producto", new Producto());
        cargarCombos(model);
        return "admin/producto-form";
    }

    @PostMapping("/productos")
    public String guardarProducto(@Valid @ModelAttribute Producto producto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            cargarCombos(model);
            return "admin/producto-form";
        }
        productoRepository.save(producto);
        return "redirect:/admin";
    }

    @PostMapping("/informes/generar")
    public String generarInforme() {
        informeService.generarResumenOperativo();
        return "redirect:/admin";
    }

    private void cargarCombos(Model model) {
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("proveedores", proveedorRepository.findAll());
    }
}
