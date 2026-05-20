package com.cybervoid.repository;

import com.cybervoid.model.Categoria;
import com.cybervoid.model.CategoriaTipo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findBySlug(String slug);
    List<Categoria> findByTipo(CategoriaTipo tipo);
}
