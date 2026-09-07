package com.temnafesta.domain.model;

import com.temnafesta.domain.exception.RegraDeNegocioException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LembreteTest {

    @Test
    void naoDeveAceitarDescricaoNula() {
        assertThrows(RegraDeNegocioException.class, () -> new Lembrete(
                1L,
                null,
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                10L));
    }

    @Test
    void naoDeveAceitarDescricaoVazia() {
        assertThrows(RegraDeNegocioException.class, () -> new Lembrete(
                1L,
                "",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                10L));
    }

    @Test
    void naoDeveAceitarDescricaoApenasComEspacos() {
        assertThrows(RegraDeNegocioException.class, () -> new Lembrete(
                1L,
                "   ",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                10L));
    }

    @Test
    void deveDefinirDataCriacaoComoHojeQuandoNaoInformada() {
        LocalDate antes = LocalDate.now();
        Lembrete lembrete = new Lembrete(
                1L,
                "Lembrete válido",
                null,
                LocalDate.now().plusDays(1),
                10L);
        LocalDate depois = LocalDate.now();

        assertNotNull(lembrete.getDataCriacao());
        assertTrue(lembrete.getDataCriacao().isAfter(antes.minusDays(1)));
        assertTrue(lembrete.getDataCriacao().isBefore(depois.plusDays(1)));
    }

    @Test
    void devePreservarDataCriacaoQuandoInformada() {
        LocalDate dataCriacaoEsperada = LocalDate.of(2024, 1, 15);
        Lembrete lembrete = new Lembrete(
                1L,
                "Lembrete válido",
                dataCriacaoEsperada,
                LocalDate.now().plusDays(1),
                10L);

        assertEquals(dataCriacaoEsperada, lembrete.getDataCriacao());
    }

    @Test
    void deveCriarLembreteComTodosOsCampos() {
        LocalDate dataCriacao = LocalDate.of(2024, 1, 15);
        LocalDate dataLimite = LocalDate.of(2024, 2, 1);
        Lembrete lembrete = new Lembrete(
                1L,
                "Comprar ingredientes",
                dataCriacao,
                dataLimite,
                10L);

        assertEquals(1L, lembrete.getId());
        assertEquals("Comprar ingredientes", lembrete.getDescricao());
        assertEquals(dataCriacao, lembrete.getDataCriacao());
        assertEquals(dataLimite, lembrete.getDataLimite());
        assertEquals(10L, lembrete.getUsuarioId());
    }

    @Test
    void deveAceitarDescricaoComEspacosNasExtremidades() {
        Lembrete lembrete = new Lembrete(
                1L,
                "  Lembrete com espaços  ",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                10L);

        assertEquals("  Lembrete com espaços  ", lembrete.getDescricao());
    }
}