package com.temnafesta.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EnderecoRequestDto(
        @NotNull(message = "O CEP é obrigatório.")
        @Size(min = 8, max = 8, message = "O CEP deve conter 8 dígitos.")
        String cep,

        @NotNull(message = "O logradouro é obrigatório.")
        @NotBlank(message = "O logradouro é obrigatório.")
        @Size(max = 150, message = "O logradouro deve ter no máximo 150 caracteres.")
        String logradouro,

        @NotNull(message = "O número é obrigatório.")
        @NotBlank(message = "O número é obrigatório.")
        @Size(max = 10, message = "O número deve ter no máximo 10 caracteres.")
        String numero,

        @Size(max = 100, message = "O complemento deve ter no máximo 100 caracteres.")
        String complemento,

        @NotNull(message = "O bairro é obrigatório.")
        @NotBlank(message = "O bairro é obrigatório.")
        @Size(max = 100, message = "O bairro deve ter no máximo 100 caracteres.")
        String bairro,

        @NotNull(message = "A cidade é obrigatória.")
        @NotBlank(message = "A cidade é obrigatória.")
        @Size(max = 100, message = "A cidade deve ter no máximo 100 caracteres.")
        String cidade,

        @NotNull(message = "O estado é obrigatório.")
        @NotBlank(message = "O estado é obrigatório.")
        @Size(min = 2, max = 2, message = "O estado deve ter 2 caracteres (UF).")
        String estado
) {}