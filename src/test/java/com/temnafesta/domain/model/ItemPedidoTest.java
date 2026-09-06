package com.temnafesta.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ItemPedidoTest {

    @ParameterizedTest
    @NullSource
    @ValueSource(ints = {0, -1, -10})
    void naoDeveAceitarQuantidadeInvalida(Integer quantidade) {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new ItemPedido(null, 1L, quantidade, new BigDecimal("10.00"), null));

        assertEquals("A quantidade do item deve ser maior que zero.", ex.getMessage());
    }

    @Test
    void naoDeveAceitarPrecoNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new ItemPedido(null, 1L, 1, null, null));
    }

    @Test
    void naoDeveAceitarPrecoNegativo() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new ItemPedido(null, 1L, 1, new BigDecimal("-0.01"), null));

        assertEquals("O preço unitário não pode ser negativo.", ex.getMessage());
    }

    @Test
    void deveCalcularSubtotal() {
        ItemPedido item = new ItemPedido(1L, 2L, 3, new BigDecimal("19.90"), "Sem lactose");

        assertTrue(item.calcularSubtotal().compareTo(new BigDecimal("59.70")) == 0);
        assertEquals(1L, item.getId());
        assertEquals(2L, item.getProdutoId());
        assertEquals("Sem lactose", item.getObservacaoItem());
    }

    @Test
    void deveAceitarPrecoZero() {
        ItemPedido item = new ItemPedido(null, 1L, 2, BigDecimal.ZERO, null);

        assertTrue(item.calcularSubtotal().compareTo(BigDecimal.ZERO) == 0);
    }

    @Test
    void deveAssociarProduto() {
        ItemPedido item = new ItemPedido(null, 1L, 1, new BigDecimal("10.00"), null);
        Produto produto = new Produto(1L, "Bolo", null, new BigDecimal("10.00"), true, false);

        item.setProduto(produto);

        assertEquals(produto, item.getProduto());
    }
}
