package com.cybervoid.service;

import com.cybervoid.model.Informe;
import com.cybervoid.repository.InformeRepository;
import com.cybervoid.repository.PedidoRepository;
import com.cybervoid.repository.ProductoRepository;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InformeService {
    private final InformeRepository informeRepository;
    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    public InformeService(InformeRepository informeRepository, PedidoRepository pedidoRepository,
                          ProductoRepository productoRepository) {
        this.informeRepository = informeRepository;
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional
    public Informe generarResumenOperativo() {
        Informe informe = new Informe();
        informe.setTitulo("Resumen operativo " + LocalDate.now());
        informe.setTipo("OPERATIVO");
        informe.setContenido("Pedidos registrados: " + pedidoRepository.count()
            + "\nProductos activos: " + productoRepository.findByActivoTrueOrderByNombreAsc().size());
        return informeRepository.save(informe);
    }
}
