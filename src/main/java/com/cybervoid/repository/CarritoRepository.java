package com.cybervoid.repository;

import com.cybervoid.model.Carrito;
import com.cybervoid.model.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    @EntityGraph(attributePaths = {"lineas", "lineas.producto"})
    Optional<Carrito> findByUsuario(Usuario usuario);
}
