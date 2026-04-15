package com.temnafesta.dto.pedido;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PedidoResponseDto {

    private Integer id;
    private LocalDateTime dataPedido;
    private LocalDateTime dataEntrega;
    private BigDecimal valorTotal;
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

        private EnderecoClientePedidoDto endereco;

        public static class EnderecoClientePedidoDto{
            private Integer id;
            private String cep;
            private String logradouro;
            private String numero;
            private String cidade;

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

            public String getCidade() {
                return cidade;
            }

            public void setCidade(String cidade) {
                this.cidade = cidade;
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

        public EnderecoClientePedidoDto getEndereco() {
            return endereco;
        }

        public void setEndereco(EnderecoClientePedidoDto endereco) {
            this.endereco = endereco;
        }
    }

    public static class UsuarioPedidoDto {
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

    public static class StatusPedidoDto {
        private String nome;

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

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDateTime dataPedido) {
        this.dataPedido = dataPedido;
    }

    public LocalDateTime getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDateTime dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
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