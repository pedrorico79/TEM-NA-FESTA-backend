package com.temnafesta.domain.model;

import com.temnafesta.domain.exception.RegraDeNegocioException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MetodoPagamentoTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void naoDeveAceitarNomeInvalido(String nome) {
        RegraDeNegocioException ex = assertThrows(RegraDeNegocioException.class,
                () -> new MetodoPagamento(null, nome));

        assertEquals("O nome do método de pagamento é obrigatório.", ex.getMessage());
    }

    @Test
    void deveCriarComNomeValido() {
        MetodoPagamento metodo = new MetodoPagamento(1L, "Pix");

        assertEquals(1L, metodo.getId());
        assertEquals("Pix", metodo.getNome());
    }
}
