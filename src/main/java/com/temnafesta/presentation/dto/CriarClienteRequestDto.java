package com.temnafesta.presentation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@PeloMenosUmContato
public record CriarClienteRequestDto (
    @NotBlank(message = "O nome do cliente é obrigatório")
    String nome,
    String telefone,
    String whatsapp,
    String instagram,
    String anotacoes,

    @Valid // Faz o Spring validar também os campos dentro do endereço
    EnderecoDto endereco
)
{}
