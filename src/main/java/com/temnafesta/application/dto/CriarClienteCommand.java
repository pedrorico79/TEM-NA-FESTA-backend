package com.temnafesta.application.dto;

public record CriarClienteCommand(
        String nome,
        String telefone,
        String whatsapp,
        String instagram,
        String anotacoes,
        CriarEndrecoCommand endereco
) {}
