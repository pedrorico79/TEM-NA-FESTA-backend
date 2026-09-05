package com.temnafesta.application.dto;

import java.time.LocalDate;

public record AtualizarLembreteCommand(
        Long id,
        String descricao,
        LocalDate dataLimite,
        Long usuarioId
) {}