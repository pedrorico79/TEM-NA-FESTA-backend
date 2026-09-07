package com.temnafesta.domain.model;

import com.temnafesta.domain.vo.StatusProducaoEnum;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HistoricoStatusPedidoTest {

    @Test
    void deveAssumirDataAlteracaoQuandoNula() {
        LocalDateTime antes = LocalDateTime.now();

        HistoricoStatusPedido historico = new HistoricoStatusPedido(
                null, null, "Sinal recebido", StatusProducaoEnum.CONFIRMADO, 1L, 2L);

        assertNotNull(historico.getDataAlteracao());
        assertTrue(!historico.getDataAlteracao().isBefore(antes));
        assertEquals("Sinal recebido", historico.getObservacao());
        assertEquals(StatusProducaoEnum.CONFIRMADO, historico.getStatusProducao());
        assertEquals(1L, historico.getPedidoId());
        assertEquals(2L, historico.getUsuarioId());
    }

    @Test
    void devePreservarDataAlteracaoQuandoInformada() {
        LocalDateTime data = LocalDateTime.of(2026, 1, 10, 12, 0);

        HistoricoStatusPedido historico = new HistoricoStatusPedido(
                5L, data, null, StatusProducaoEnum.CANCELADO, 1L, 2L);

        assertEquals(5L, historico.getId());
        assertEquals(data, historico.getDataAlteracao());
    }
}
