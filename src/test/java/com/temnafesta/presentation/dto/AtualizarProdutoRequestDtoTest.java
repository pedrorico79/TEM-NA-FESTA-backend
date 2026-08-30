package com.temnafesta.presentation.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AtualizarProdutoRequestDtoTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void deveAceitarRequestCompletoValido() {
        AtualizarProdutoRequestDto request = criarRequest("Bolo Atualizado", new BigDecimal("59.90"), true);

        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void naoDeveAceitarAtivoNaoInformado() {
        AtualizarProdutoRequestDto request = criarRequest("Bolo Atualizado", new BigDecimal("59.90"), null);

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void naoDeveAceitarNomeEmBranco() {
        AtualizarProdutoRequestDto request = criarRequest(" ", new BigDecimal("59.90"), true);

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void naoDeveAceitarPrecoIgualAZero() {
        AtualizarProdutoRequestDto request = criarRequest("Bolo Atualizado", BigDecimal.ZERO, true);

        assertFalse(validator.validate(request).isEmpty());
    }

    private AtualizarProdutoRequestDto criarRequest(String nome, BigDecimal precoVenda, Boolean ativo) {
        return new AtualizarProdutoRequestDto(nome, null, precoVenda, ativo);
    }
}