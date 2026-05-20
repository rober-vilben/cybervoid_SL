package com.cybervoid.controller;

import com.cybervoid.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProductoController {
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/mujer")
    public String mujer(Model model) {
        model.addAttribute("titulo", "Mujer");
        model.addAttribute("descripcion", "Prendas de lineas limpias, capas urbanas y accesorios sobrios para looks alternativos.");
        model.addAttribute("productos", productoService.listarPorCategoria("mujer"));
        return "productos/catalogo";
    }

    @GetMapping("/hombre")
    public String hombre(Model model) {
        model.addAttribute("titulo", "Hombre");
        model.addAttribute("descripcion", "Piezas oscuras, comodas y modulares para un armario urbano de inspiracion tecnica.");
        model.addAttribute("productos", productoService.listarPorCategoria("hombre"));
        return "productos/catalogo";
    }

    @GetMapping("/ofertas")
    public String ofertas(Model model) {
        model.addAttribute("titulo", "Ofertas");
        model.addAttribute("descripcion", "Promociones activas cargadas desde MySQL y gestionables desde administracion.");
        model.addAttribute("productos", productoService.listarOfertas());
        return "productos/catalogo";
    }

    @GetMapping("/temporada/{slug}")
    public String temporada(@PathVariable String slug, Model model) {
        model.addAttribute("titulo", slug.substring(0, 1).toUpperCase() + slug.substring(1));
        model.addAttribute("descripcion", "Coleccion de temporada conectada al catalogo persistente.");
        model.addAttribute("productos", productoService.listarPorCategoria(slug));
        return "productos/catalogo";
    }
}
