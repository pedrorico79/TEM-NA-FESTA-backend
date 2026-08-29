package com.temnafesta.application.dto;

import java.time.LocalDate;

public record AtualizarEventoCommand(
        String nome,
        LocalDate dataInicio,
        LocalDate dataFim
) {
}
