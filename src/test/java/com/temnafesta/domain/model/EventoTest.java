package com.temnafesta.domain.model;

import com.temnafesta.domain.exception.RegraDeNegocioException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EventoTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void naoDeveAceitarNomeInvalido(String nome) {
        RegraDeNegocioException ex = assertThrows(RegraDeNegocioException.class,
                () -> new Evento(null, nome, LocalDate.now(), null, true, false));

        assertEquals("O nome do evento é obrigatório.", ex.getMessage());
    }

    @Test
    void naoDeveAceitarDataFimAnteriorADataInicio() {
        RegraDeNegocioException ex = assertThrows(RegraDeNegocioException.class,
                () -> new Evento(null, "Festa Junina",
                        LocalDate.of(2026, 6, 20), LocalDate.of(2026, 6, 10), true, false));

        assertEquals("A data de fim do evento não pode ser anterior à data de início.", ex.getMessage());
    }

    @Test
    void deveAceitarDatasIguaisOuApenasUmaInformada() {
        LocalDate dia = LocalDate.of(2026, 6, 20);

        assertTrue(new Evento(null, "E1", dia, dia, true, false).getDataFim().equals(dia));
        assertTrue(new Evento(null, "E2", dia, null, true, false).getDataInicio().equals(dia));
        assertTrue(new Evento(null, "E3", null, null, true, false).isAtivo());
    }
}
