package com.temnafesta.application.dto;

import java.time.LocalDate;

public record CriarEventoCommand(
        String nome,
        LocalDate dataInicio,
        LocalDate dataFim
) {
}
