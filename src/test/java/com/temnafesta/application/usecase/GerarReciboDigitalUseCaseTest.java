package com.temnafesta.application.usecase;

import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.Cliente;
import com.temnafesta.domain.model.ItemPedido;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.ports.repository.ClienteRepositoryPort;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;
import com.temnafesta.domain.vo.StatusProducaoEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GerarReciboDigitalUseCaseTest {

    @Mock
    private PedidoRepositoryPort pedidoRepositoryPort;
    @Mock
    private ClienteRepositoryPort clienteRepositoryPort;

    @InjectMocks
    private GerarReciboDigitalUseCase useCase;

    private static Pedido pedido() {
        return new Pedido(42L, LocalDateTime.now(), LocalDateTime.of(2026, 12, 20, 18, 0),
                new BigDecimal("10.00"), null, StatusProducaoEnum.CONFIRMADO,
                1L, 1L, null, null,
                List.of(new ItemPedido(1L, 1L, 2, new BigDecimal("50.00"), "Sem lactose")), null);
    }

    private static Cliente cliente() {
        return new Cliente(1L, "Maria", null, null, null, null, null, null, true, false);
    }

    @Test
    void deveGerarReciboComDadosDoPedidoECliente() {
        when(pedidoRepositoryPort.buscarPorId(42L)).thenReturn(Optional.of(pedido()));
        when(clienteRepositoryPort.buscarPorIdIncluindoDeletados(1L)).thenReturn(Optional.of(cliente()));

        String recibo = useCase.executar(42L);

        assertTrue(recibo.contains("#42"));
        assertTrue(recibo.contains("Maria"));
        assertTrue(recibo.contains("20/12/2026 18:00"));
        assertTrue(recibo.contains("Sem lactose"));
        assertTrue(recibo.contains("110.00"));
        assertTrue(recibo.contains("pix@temnafesta.com.br"));
    }

    @Test
    void deveOmitirTaxaDeEntregaQuandoZerada() {
        Pedido semTaxa = new Pedido(1L, LocalDateTime.now(), LocalDateTime.of(2026, 12, 20, 18, 0),
                BigDecimal.ZERO, null, StatusProducaoEnum.RASCUNHO,
                1L, 1L, null, null,
                List.of(new ItemPedido(1L, 1L, 1, new BigDecimal("20.00"), null)), null);
        when(pedidoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(semTaxa));
        when(clienteRepositoryPort.buscarPorIdIncluindoDeletados(1L)).thenReturn(Optional.of(cliente()));

        String recibo = useCase.executar(1L);

        assertTrue(!recibo.contains("Taxa de Entrega"));
    }

    @Test
    void naoDeveGerarQuandoPedidoNaoEncontrado() {
        when(pedidoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.empty());

        assertThrows(RegraDeNegocioException.class, () -> useCase.executar(1L));
    }

    @Test
    void naoDeveGerarQuandoClienteNaoEncontrado() {
        when(pedidoRepositoryPort.buscarPorId(42L)).thenReturn(Optional.of(pedido()));
        when(clienteRepositoryPort.buscarPorIdIncluindoDeletados(1L)).thenReturn(Optional.empty());

        assertThrows(RegraDeNegocioException.class, () -> useCase.executar(42L));
    }
}
