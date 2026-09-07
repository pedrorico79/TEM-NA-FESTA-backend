package com.temnafesta.application.dto;

import java.time.LocalDate;

public record CriarLembreteCommand(
        String descricao,
        LocalDate dataLimite,
        Long usuarioId
) {}