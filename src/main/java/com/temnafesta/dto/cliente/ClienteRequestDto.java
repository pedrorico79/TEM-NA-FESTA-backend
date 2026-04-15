package com.temnafesta.dto.cliente;

import jakarta.validation.constraints.*;

public class ClienteRequestDto {

    @NotBlank
    @Size(min = 3, max = 100)
    private String nome;

    @NotBlank
    @Pattern(regexp = "^\\d{10,11}$", message = "Telefone deve ter 10 ou 11 dígitos")
    private String telefone;

    @NotBlank
    @Pattern(regexp = "^\\d{10,11}$", message = "Telefone deve ter 10 ou 11 dígitos")
    private String whatsapp;

    @Pattern(
            regexp = "^@?[A-Za-z0-9._]{1,30}$",
            message = "Instagram inválido"
    )
    private String instagram;

    @Size(max = 500)
    private String anotacoes;

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
