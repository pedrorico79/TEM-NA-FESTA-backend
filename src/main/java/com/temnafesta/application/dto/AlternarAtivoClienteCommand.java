package com.temnafesta.application.dto;

public record AlternarAtivoClienteCommand(
        Long clienteId,
        boolean ativo
) {
}