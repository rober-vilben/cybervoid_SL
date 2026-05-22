package com.cybervoid.service;

import com.cybervoid.model.Informe;
import com.cybervoid.model.Producto;
import com.cybervoid.repository.InformeRepository;
import com.cybervoid.repository.PedidoRepository;
import com.cybervoid.repository.ProductoRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InformeServiceTest {
    @Mock
    private InformeRepository informeRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private InformeService informeService;

    @Test
    void deberiaGenerarResumenOperativoConPedidosYProductosActivos() {
        when(pedidoRepository.count()).thenReturn(3L);
        when(productoRepository.findByActivoTrueOrderByNombreAsc()).thenReturn(List.of(new Producto(), new Producto()));
        when(informeRepository.save(org.mockito.ArgumentMatchers.any(Informe.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Informe informe = informeService.generarResumenOperativo();

        assertThat(informe.getTitulo()).startsWith("Resumen operativo ");
        assertThat(informe.getTipo()).isEqualTo("OPERATIVO");
        assertThat(informe.getContenido()).contains("Pedidos registrados: 3");
        assertThat(informe.getContenido()).contains("Productos activos: 2");
    }
}
