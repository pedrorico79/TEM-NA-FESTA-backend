package com.temnafesta.presentation.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CriarProdutoRequestDtoTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void deveAceitarAtivoNaoInformado() {
        CriarProdutoRequestDto request = criarRequest("Bolo de Chocolate", new BigDecimal("49.90"));

        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void naoDeveAceitarNomeEmBranco() {
        CriarProdutoRequestDto request = criarRequest(" ", new BigDecimal("49.90"));

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void naoDeveAceitarPrecoIgualAZero() {
        CriarProdutoRequestDto request = criarRequest("Bolo de Chocolate", BigDecimal.ZERO);

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void naoDeveAceitarPrecoComMaisDeDuasCasasDecimais() {
        CriarProdutoRequestDto request = criarRequest("Bolo de Chocolate", new BigDecimal("49.999"));

        assertFalse(validator.validate(request).isEmpty());
    }

    private CriarProdutoRequestDto criarRequest(String nome, BigDecimal precoVenda) {
        return new CriarProdutoRequestDto(nome, null, precoVenda, null);
    }
}