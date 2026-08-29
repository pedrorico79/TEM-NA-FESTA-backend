package com.temnafesta.domain.model;

import com.temnafesta.domain.exception.RegraDeNegocioException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
}