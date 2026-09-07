package com.temnafesta.presentation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@PeloMenosUmContato
public record AtualizarClienteRequestDto(
        @NotBlank(message = "O nome do cliente é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres")
        String telefone,

        @Size(max = 20, message = "O WhatsApp deve ter no máximo 20 caracteres")
        String whatsapp,

        @Size(max = 50, message = "O Instagram deve ter no máximo 50 caracteres")
        String instagram,
        String anotacoes,

        @Valid
        EnderecoDto endereco
)
        implements DadosContatoDto {
}
