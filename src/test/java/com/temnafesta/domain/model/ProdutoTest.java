package com.temnafesta.domain.model;

import com.temnafesta.domain.exception.RegraDeNegocioException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProdutoTest {

    @Test
    void naoDeveAceitarPrecoIgualAZero() {
        assertThrows(RegraDeNegocioException.class, () -> new Produto(
                null,
                "Bolo de Chocolate",
                null,
                BigDecimal.ZERO,
                true,
                false));
    }

    @Test
    void naoDeveAceitarPrecoNegativo() {
        assertThrows(RegraDeNegocioException.class, () -> new Produto(
                null,
                "Bolo de Chocolate",
                null,
                new BigDecimal("-0.01"),
                true,
                false));
    }

    @Test
    void deveMarcarComoDeletadoEInativoPreservandoOsDemaisDados() {
        Produto produto = new Produto(
                1L,
                "Bolo de Chocolate",
                "Bolo com cobertura de ganache",
                new BigDecimal("49.90"),
                true,
                false);

        produto.deletar();

        assertTrue(produto.isDeletado());
        assertFalse(produto.isAtivo());
        assertEquals(1L, produto.getId());
        assertEquals("Bolo de Chocolate", produto.getNome());
        assertEquals("Bolo com cobertura de ganache", produto.getDescricao());
        assertEquals(new BigDecimal("49.90"), produto.getPrecoVenda());
    }

    @Test
    void deveManterProdutoInativoQuandoForCriadoComoDeletado() {
        Produto produto = new Produto(
                1L,
                "Bolo de Chocolate",
                null,
                new BigDecimal("49.90"),
                true,
                true);

        assertTrue(produto.isDeletado());
        assertFalse(produto.isAtivo());
    }
}