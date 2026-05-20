package com.cybervoid.service;

import com.cybervoid.model.Producto;
import com.cybervoid.repository.ProductoRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listarActivos() {
        return productoRepository.findByActivoTrueOrderByNombreAsc();
    }

    public List<Producto> listarPorCategoria(String slug) {
        return productoRepository.findByActivoTrueAndCategoriaSlugOrderByNombreAsc(slug);
    }

    public List<Producto> listarOfertas() {
        return productoRepository.findByActivoTrueAndPrecioAnteriorIsNotNullOrderByNombreAsc();
    }

    public Producto buscar(Long id) {
        return productoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
    }
}
