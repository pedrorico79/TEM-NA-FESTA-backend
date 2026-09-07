package com.temnafesta.presentation.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlterarAtivoProdutoRequestDtoTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void deveAceitarStatusInformado() {
        AlterarAtivoProdutoRequestDto request = new AlterarAtivoProdutoRequestDto(false);

        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void naoDeveAceitarStatusNaoInformado() {
        AlterarAtivoProdutoRequestDto request = new AlterarAtivoProdutoRequestDto(null);

        assertFalse(validator.validate(request).isEmpty());
    }
}