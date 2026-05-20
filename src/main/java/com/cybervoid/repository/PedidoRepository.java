package com.cybervoid.repository;

import com.cybervoid.model.Pedido;
import com.cybervoid.model.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    @EntityGraph(attributePaths = {"lineas"})
    List<Pedido> findByUsuarioOrderByFechaDesc(Usuario usuario);
    List<Pedido> findTop10ByOrderByFechaDesc();
}
