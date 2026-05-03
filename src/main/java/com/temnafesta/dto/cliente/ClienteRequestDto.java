package com.temnafesta.dto.cliente;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Dados para criação ou atualização de cliente")
public class ClienteRequestDto {

    @Schema(description = "Nome do cliente", example = "João Silva", minLength = 3, maxLength = 100)
    @NotBlank
    @Size(min = 3, max = 100)
    private String nome;

    @Schema(description = "Telefone do cliente sem máscara", example = "11999999999")
    @NotBlank
    @Pattern(regexp = "^\\d{10,11}$", message = "Telefone deve ter 10 ou 11 dígitos")
    private String telefone;

    @Schema(description = "WhatsApp do cliente sem máscara", example = "11999999999")
    @NotBlank
    @Pattern(regexp = "^\\d{10,11}$", message = "Telefone deve ter 10 ou 11 dígitos")
    private String whatsapp;

    @Schema(description = "Instagram do cliente", example = "@joaosilva")
    @Pattern(regexp = "^@?[A-Za-z0-9._]{1,30}$", message = "Instagram inválido")
    private String instagram;

    @Schema(description = "Anotações do cliente", example = "Cliente VIP", maxLength = 500)
    @Size(max = 500)
    private String anotacoes;

    @Schema(description = "ID do endereço do cliente", example = "1")
    @NotNull
    @Positive
    private Integer enderecoId;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getAnotacoes() {
        return anotacoes;
    }

    public void setAnotacoes(String anotacoes) {
        this.anotacoes = anotacoes;
    }

    public Integer getEnderecoId() {
        return enderecoId;
    }

    public void setEnderecoId(Integer enderecoId) {
        this.enderecoId = enderecoId;
    }
}
