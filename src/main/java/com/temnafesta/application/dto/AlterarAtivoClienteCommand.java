package com.temnafesta.application.dto;

public record AlterarAtivoClienteCommand(
        Long id,
        boolean ativo
) {}