package com.temnafesta.presentation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@PeloMenosUmContato
public record AtualizarClienteRequestDto(
        @NotBlank(message = "O nome do cliente é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        @Pattern(regexp = "^\\d{10,15}$", message = "O telefone deve conter apenas números e ter entre 10 e 15 dígitos")
        String telefone,

        @Pattern(regexp = "^\\d{10,15}$", message = "O whatsapp deve conter apenas números e ter entre 10 e 15 dígitos")
        String whatsapp,

        @Size(max = 50, message = "O Instagram deve ter no máximo 50 caracteres")
        String instagram,
        String anotacoes,

        @Valid
        EnderecoDto endereco
)
        implements DadosContatoDto {
}
