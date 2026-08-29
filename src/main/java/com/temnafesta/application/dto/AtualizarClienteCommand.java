package com.temnafesta.application.dto;

public record AtualizarClienteCommand(
        String nome,
        String telefone,
        String whatsapp,
        String instagram,
        String anotacoes,
        CriarEndrecoCommand endereco
) {}
