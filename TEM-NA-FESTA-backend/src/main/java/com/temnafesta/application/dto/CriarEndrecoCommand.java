package com.temnafesta.application.dto;

public record CriarEndrecoCommand(
    String cep,
    String logradouro,
    String numero,
    String complemento,
    String bairro,
    String cidade,
    String estado
){}
