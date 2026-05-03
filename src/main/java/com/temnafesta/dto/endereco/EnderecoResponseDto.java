package com.temnafesta.dto.endereco;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados do endereço")
public class EnderecoResponseDto {

    @Schema(description = "ID do endereço", example = "1")
    private Integer id;

    @Schema(description = "CEP do endereço sem máscara", example = "01310000")
    private String cep;

    @Schema(description = "Logradouro do endereço", example = "Avenida Paulista")
    private String logradouro;

    @Schema(description = "Número do endereço", example = "1578")
    private String numero;

    @Schema(description = "Complemento do endereço", example = "Apto 42")
    private String complemento;

    @Schema(description = "Bairro do endereço", example = "Bela Vista")
    private String bairro;

    @Schema(description = "Cidade do endereço", example = "São Paulo")
    private String cidade;

    @Schema(description = "UF do endereço", example = "SP")
    private String estado;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
