package com.temnafesta.domain.model;

import com.temnafesta.domain.vo.StatusPagamentoEnum;
import com.temnafesta.domain.vo.TipoPagamentoEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PagamentoTest {

    private static Pagamento pagamento(StatusPagamentoEnum status) {
        return new Pagamento(1L, new BigDecimal("55.00"), LocalDateTime.now(),
                TipoPagamentoEnum.SINAL, status, 2L, 3L);
    }

    @Test
    void deveConsiderarConfirmadoSomenteQuandoStatusForConfirmado() {
        assertTrue(pagamento(StatusPagamentoEnum.CONFIRMADO).isConfirmado());
    }

    @ParameterizedTest
    @EnumSource(value = StatusPagamentoEnum.class, names = {"PENDENTE", "CANCELADO"})
    void naoDeveConsiderarConfirmadoParaDemaisStatus(StatusPagamentoEnum status) {
        assertFalse(pagamento(status).isConfirmado());
    }

    @Test
    void deveExporTodosOsCampos() {
        Pagamento pagamento = pagamento(StatusPagamentoEnum.CONFIRMADO);

        assertEquals(1L, pagamento.getId());
        assertTrue(pagamento.getValor().compareTo(new BigDecimal("55.00")) == 0);
        assertEquals(TipoPagamentoEnum.SINAL, pagamento.getTipoPagamento());
        assertEquals(StatusPagamentoEnum.CONFIRMADO, pagamento.getStatusPagamento());
        assertEquals(2L, pagamento.getMetodoPagamentoId());
        assertEquals(3L, pagamento.getUsuarioId());
    }
}
