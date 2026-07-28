package com.temnafesta.dto.cliente;


import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Dados do cliente")
public class ClienteResponseDto {
    @Schema(description = "ID do cliente", example = "1")
    private Integer id;

    @Schema(description = "Nome do cliente", example = "João Silva")
    private String nome;

    @Schema(description = "Telefone do cliente sem máscara", example = "11999999999")
    private String telefone;

    @Schema(description = "WhatsApp do cliente sem máscara", example = "11999999999")
    private String whatsapp;

    @Schema(description = "Instagram do cliente", example = "@joaosilva")
    private String instagram;

    @Schema(description = "Data de cadastro do cliente", example = "2024-01-15")
    private LocalDate dataCadastro;

    @Schema(description = "Anotações do cliente", example = "Cliente VIP")
    private String anotacoes;

    @Schema(description = "Endereço do cliente")
    private EnderecoClienteDto endereco;

    @Schema(description = "Status do cliente", example = "true")
    private Boolean isAtivo;

    @Schema(description = "Se o cliente foi deletado", example = "false")
    private Boolean isDeletado;

    @Schema(description = "Dados resumidos do endereço do cliente")
    public static class EnderecoClienteDto{
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getAnotacoes() {
        return anotacoes;
    }

    public void setAnotacoes(String anotacoes) {
        this.anotacoes = anotacoes;
    }

    public EnderecoClienteDto getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoClienteDto endereco) {
        this.endereco = endereco;
    }

    public Boolean getIsAtivo() {
        return isAtivo;
    }

    public void setIsAtivo(Boolean ativo) {
        isAtivo = ativo;
    }

    public Boolean getIsDeletado() {
        return isDeletado;
    }

    public void setIsDeletado(Boolean deletado) {
        isDeletado = deletado;
    }
}
