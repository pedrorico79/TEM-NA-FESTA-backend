package com.temnafesta.dto.pedido;

import com.temnafesta.model.Endereco;
import com.temnafesta.model.Perfil;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PedidoResponseDto {

    private Integer id;
    private LocalDate dataPedido;
    private LocalDate dataEntrega;
    private Double valorTotal;
    private String observacao;

    private ClientePedidoDto cliente;
    private UsuarioPedidoDto usuario;
    private StatusPedidoDto statusProducao;



    public static class ClientePedidoDto {
        private Integer id;

        private String nome;

        private String telefone;

        private String whatsapp;

        private String instagram;

        private LocalDate dataCadastro;

        private String anotacoes;

        private Endereco endereco;

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

        public Endereco getEndereco() {
            return endereco;
        }

        public void setEndereco(Endereco endereco) {
            this.endereco = endereco;
        }
    }

    public static class UsuarioPedidoDto {
        private Integer id;

        private String nome;

        private String email;

        private Boolean isAtivo;

        private LocalDateTime dataCriacao;

        private Perfil perfil;

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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Boolean getAtivo() {
            return isAtivo;
        }

        public void setAtivo(Boolean ativo) {
            isAtivo = ativo;
        }

        public LocalDateTime getDataCriacao() {
            return dataCriacao;
        }

        public void setDataCriacao(LocalDateTime dataCriacao) {
            this.dataCriacao = dataCriacao;
        }

        public Perfil getPerfil() {
            return perfil;
        }

        public void setPerfil(Perfil perfil) {
            this.perfil = perfil;
        }
    }

    public static class StatusPedidoDto {
        private Integer id;
        private String nome;

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
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
    }

    public LocalDate getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDate dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public ClientePedidoDto getCliente() {
        return cliente;
    }

    public void setCliente(ClientePedidoDto cliente) {
        this.cliente = cliente;
    }

    public UsuarioPedidoDto getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioPedidoDto usuario) {
        this.usuario = usuario;
    }

    public StatusPedidoDto getStatusProducao() {
        return statusProducao;
    }

    public void setStatusProducao(StatusPedidoDto statusProducao) {
        this.statusProducao = statusProducao;
    }
}