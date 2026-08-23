package com.temnafesta.presentation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record CriarClienteRequestDto (
    @NotBlank(message = "O nome do cliente é obrigatório")
    String nome,
    @NotBlank(message = "O telefone do cliente é obrigatório")
    String telefone,
    @NotBlank(message = "O WhatsApp do cliente é obrigatório")
    String whatsapp,
    String instagram,
    String anotacoes,

    @Valid // Faz o Spring validar também os campos dentro do endereço
    EnderecoDto endereco
)
{}
