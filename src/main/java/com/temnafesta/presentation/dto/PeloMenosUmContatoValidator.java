package com.temnafesta.presentation.dto;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.stream.Stream;

public class PeloMenosUmContatoValidator
        implements ConstraintValidator<PeloMenosUmContato, DadosContatoDto> {

    @Override
    public boolean isValid(
            DadosContatoDto dto,
            ConstraintValidatorContext context
    ) {
        if (dto == null) {
            return true;
        }

        return Stream.of(
                dto.telefone(),
                dto.whatsapp(),
                dto.instagram()
        ).anyMatch(valor -> valor != null && !valor.isBlank());
    }
}