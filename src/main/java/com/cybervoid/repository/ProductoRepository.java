package com.cybervoid.repository;

import com.cybervoid.model.Producto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByActivoTrueOrderByNombreAsc();
    List<Producto> findByActivoTrueAndCategoriaSlugOrderByNombreAsc(String slug);
    List<Producto> findByActivoTrueAndPrecioAnteriorIsNotNullOrderByNombreAsc();
    Optional<Producto> findBySku(String sku);
}
