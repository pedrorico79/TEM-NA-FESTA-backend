package com.temnafesta.domain.vo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StatusProducaoEnumTest {

    static Stream<Arguments> matrizDeTransicao() {
        return Stream.of(
                Arguments.of(StatusProducaoEnum.RASCUNHO, StatusProducaoEnum.AGUARDANDO_SINAL, true),
                Arguments.of(StatusProducaoEnum.RASCUNHO, StatusProducaoEnum.CANCELADO, true),
                Arguments.of(StatusProducaoEnum.RASCUNHO, StatusProducaoEnum.CONFIRMADO, false),
                Arguments.of(StatusProducaoEnum.RASCUNHO, StatusProducaoEnum.EM_PRODUCAO, false),
                Arguments.of(StatusProducaoEnum.RASCUNHO, StatusProducaoEnum.PRONTO_PARA_ENTREGA, false),
                Arguments.of(StatusProducaoEnum.RASCUNHO, StatusProducaoEnum.ENTREGUE, false),
                Arguments.of(StatusProducaoEnum.RASCUNHO, StatusProducaoEnum.RASCUNHO, false),

                Arguments.of(StatusProducaoEnum.AGUARDANDO_SINAL, StatusProducaoEnum.CONFIRMADO, true),
                Arguments.of(StatusProducaoEnum.AGUARDANDO_SINAL, StatusProducaoEnum.CANCELADO, true),
                Arguments.of(StatusProducaoEnum.AGUARDANDO_SINAL, StatusProducaoEnum.EM_PRODUCAO, false),

                Arguments.of(StatusProducaoEnum.CONFIRMADO, StatusProducaoEnum.EM_PRODUCAO, true),
                Arguments.of(StatusProducaoEnum.CONFIRMADO, StatusProducaoEnum.CANCELADO, true),
                Arguments.of(StatusProducaoEnum.CONFIRMADO, StatusProducaoEnum.ENTREGUE, false),

                Arguments.of(StatusProducaoEnum.EM_PRODUCAO, StatusProducaoEnum.PRONTO_PARA_ENTREGA, true),
                Arguments.of(StatusProducaoEnum.EM_PRODUCAO, StatusProducaoEnum.CANCELADO, true),
                Arguments.of(StatusProducaoEnum.EM_PRODUCAO, StatusProducaoEnum.ENTREGUE, false),

                Arguments.of(StatusProducaoEnum.PRONTO_PARA_ENTREGA, StatusProducaoEnum.ENTREGUE, true),
                Arguments.of(StatusProducaoEnum.PRONTO_PARA_ENTREGA, StatusProducaoEnum.CANCELADO, true),
                Arguments.of(StatusProducaoEnum.PRONTO_PARA_ENTREGA, StatusProducaoEnum.EM_PRODUCAO, false),

                Arguments.of(StatusProducaoEnum.ENTREGUE, StatusProducaoEnum.CANCELADO, false),
                Arguments.of(StatusProducaoEnum.ENTREGUE, StatusProducaoEnum.RASCUNHO, false),
                Arguments.of(StatusProducaoEnum.CANCELADO, StatusProducaoEnum.RASCUNHO, false),
                Arguments.of(StatusProducaoEnum.CANCELADO, StatusProducaoEnum.ENTREGUE, false)
        );
    }

    @ParameterizedTest(name = "{0} -> {1} = {2}")
    @MethodSource("matrizDeTransicao")
    void deveRespeitarMatrizDeTransicao(StatusProducaoEnum atual, StatusProducaoEnum novo, boolean esperado) {
        assertEquals(esperado, atual.podeTransitarPara(novo));
    }

    @Test
    void estadosTerminaisNaoDevemTransitarParaNenhumEstado() {
        for (StatusProducaoEnum terminal : new StatusProducaoEnum[]{
                StatusProducaoEnum.ENTREGUE, StatusProducaoEnum.CANCELADO}) {
            for (StatusProducaoEnum destino : StatusProducaoEnum.values()) {
                assertFalse(terminal.podeTransitarPara(destino),
                        terminal + " -> " + destino + " deveria ser false");
            }
        }
    }

    @Test
    void deveConterTodosOsEstadosEsperados() {
        assertTrue(StatusProducaoEnum.valueOf("RASCUNHO") != null);
        assertEquals(7, StatusProducaoEnum.values().length);
    }
}
