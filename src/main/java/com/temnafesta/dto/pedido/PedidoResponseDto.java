package com.temnafesta.dto.pedido;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Dados do pedido")
public class PedidoResponseDto {

    @Schema(description = "ID do pedido", example = "1")
    private Integer id;

    @Schema(description = "Data e hora do pedido", example = "2024-01-15T14:30:00")
    private LocalDateTime dataPedido;

    @Schema(description = "Data e hora de entrega do pedido", example = "2024-12-31T18:00:00")
    private LocalDateTime dataEntrega;

    @Schema(description = "Valor total do pedido", example = "150.00")
    private BigDecimal valorTotal;

    @Schema(description = "Observação do pedido", example = "Sem glúten")
    private String observacao;

    @Schema(description = "Dados do cliente vinculado ao pedido")
    private ClientePedidoDto cliente;

    @Schema(description = "Dados do usuário responsável pelo pedido")
    private UsuarioPedidoDto usuario;

    @Schema(description = "Status de produção do pedido")
    private StatusPedidoDto statusProducao;

    @Schema(description = "Dados da campanha vinculada ao pedido")
    private CampanhaPedidoDto campanha;

    @Schema(description = "Valor pago do pedido", example = "150.00")
    private BigDecimal valorPago;

    @Schema(description = "Indica se o pedido foi pago", example = "true")
    private Boolean isPago;

    @Schema(description = "Dados resumidos do cliente")
    public static class ClientePedidoDto {
        @Schema(description = "ID do cliente", example = "1")
        private Integer id;

        @Schema(description = "Nome do cliente", example = "João Silva")
        private String nome;

        @Schema(description = "Telefone do cliente", example = "11999999999")
        private String telefone;

        @Schema(description = "WhatsApp do cliente", example = "11999999999")
        private String whatsapp;

        @Schema(description = "Instagram do cliente", example = "@joaosilva")
        private String instagram;

        @Schema(description = "Data de cadastro do cliente", example = "2024-01-15")
        private LocalDate dataCadastro;

        @Schema(description = "Anotações do cliente", example = "Cliente VIP")
        private String anotacoes;

        @Schema(description = "Endereço do cliente")
        private EnderecoClientePedidoDto endereco;

        @Schema(description = "Dados resumidos do endereço do cliente")
        public static class EnderecoClientePedidoDto{
            @Schema(description = "ID do endereço", example = "1")
            private Integer id;

            @Schema(description = "CEP do endereço sem máscara", example = "01310000")
            private String cep;

            @Schema(description = "Logradouro do endereço", example = "Avenida Paulista")
            private String logradouro;

            @Schema(description = "Número do endereço", example = "1578")
            private String numero;

            @Schema(description = "Cidade do endereço", example = "São Paulo")
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

    @Schema(description = "Dados resumidos do usuário")
    public static class UsuarioPedidoDto {
        @Schema(description = "ID do usuário", example = "1")
        private Integer id;

        @Schema(description = "Nome do usuário", example = "João Silva")
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

    @Schema(description = "Dados resumidos do status de produção")
    public static class StatusPedidoDto {
        @Schema(description = "Nome do status de produção", example = "EM_PRODUCAO")
        private String nome;

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }
    }

    @Schema(description = "Dados resumidos da campanha")
    public static class CampanhaPedidoDto {
        @Schema(description = "ID da campanha", example = "1")
        private Integer id;

        @Schema(description = "Nome da campanha", example = "Páscoa 2024")
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

    public CampanhaPedidoDto getCampanha() {
        return campanha;
    }

    public void setCampanha(CampanhaPedidoDto campanha) {
        this.campanha = campanha;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public Boolean getPago() {
        return isPago;
    }

    public void setIsPago(Boolean pago) {
        isPago = pago;
    }
}