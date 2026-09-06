package com.temnafesta.domain.model;

import com.temnafesta.domain.exception.RegraDeNegocioException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClienteTest {

    private static Cliente cliente(String nome, Boolean ativo, Boolean deletado) {
        return new Cliente(1L, nome, "11999999999", null, null, null, null, null, ativo, deletado);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void naoDeveAceitarNomeInvalido(String nome) {
        RegraDeNegocioException ex = assertThrows(RegraDeNegocioException.class,
                () -> cliente(nome, true, false));

        assertEquals("O nome do cliente é obrigatório.", ex.getMessage());
    }

    @Test
    void deveAssumirDataCadastroEAtivoComoDefaults() {
        Cliente cliente = cliente("Maria", null, null);

        assertNotNull(cliente.getDataCadastro());
        assertTrue(cliente.isAtivo());
        assertFalse(cliente.isDeletado());
    }

    @Test
    void deveManterInativoQuandoCriadoComoDeletado() {
        Cliente cliente = cliente("Maria", true, true);

        assertTrue(cliente.isDeletado());
        assertFalse(cliente.isAtivo());
    }

    @Test
    void deveAlterarStatusQuandoNaoDeletado() {
        Cliente cliente = cliente("Maria", true, false);

        cliente.alterarStatus(false);

        assertFalse(cliente.isAtivo());
    }

    @Test
    void naoDeveReativarClienteDeletado() {
        Cliente cliente = cliente("Maria", true, false);
        cliente.deletar();

        cliente.alterarStatus(true);

        assertTrue(cliente.isDeletado());
        assertFalse(cliente.isAtivo());
    }

    @Test
    void deveDeletarMarcandoInativo() {
        Cliente cliente = cliente("Maria", true, false);

        cliente.deletar();

        assertTrue(cliente.isDeletado());
        assertFalse(cliente.isAtivo());
    }
}
