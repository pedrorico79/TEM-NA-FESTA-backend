package com.temnafesta.dto.endereco;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para criação ou atualização do endereço do cliente")
public class EnderecoRequestDto {

    //IP não é necessário pq o bd cria sozinho

    @Schema(description = "CEP do endereço sem máscara", example = "01310000", maxLength = 8)
    @NotBlank
    @Size(max = 8)
    private String cep;

    @Schema(description = "Logradouro do endereço", example = "Avenida Paulista", maxLength = 150)
    @NotBlank
    @Size(max = 150)
    private String logradouro;

    @Schema(description = "Número do endereço", example = "1578", maxLength = 10)
    @NotBlank
    @Size(max = 10)
    private String numero;

    @Schema(description = "Complemento do endereço", example = "Apto 42", maxLength = 100)
    @Size(max = 100)
    private String complemento;

    @Schema(description = "Bairro do endereço", example = "Bela Vista", maxLength = 100)
    @NotBlank
    @Size(max = 100)
    private String bairro;

    @Schema(description = "Cidade do endereço", example = "São Paulo", maxLength = 100)
    @NotBlank
    @Size(max = 100)
    private String cidade;

    @Schema(description = "UF do endereço", example = "SP", maxLength = 2)
    @NotBlank
    @Size(max = 2)
    private String estado;

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
