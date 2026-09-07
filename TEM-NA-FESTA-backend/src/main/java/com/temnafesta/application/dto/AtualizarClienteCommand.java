package com.temnafesta.application.dto;

public record AtualizarClienteCommand(
        Long id,
        String nome,
        String telefone,
        String whatsapp,
        String instagram,
        String anotacoes,
        CriarEndrecoCommand endereco
) {}
