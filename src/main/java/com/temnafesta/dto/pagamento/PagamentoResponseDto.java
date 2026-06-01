package com.temnafesta.dto.pagamento;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Dados do pagamento")
public class PagamentoResponseDto {

    @Schema(description = "ID do pagamento", example = "1")
    private Integer id;

    @Schema(description = "Valor do pagamento", example = "150.00")
    private BigDecimal valor;

    @Schema(description = "Data e hora do pagamento", example = "2024-01-15T14:30:00")
    private LocalDateTime dataPagamento;

    @Schema(description = "Método de pagamento", example = "Pix")
    private MetodoPagamentoDto metodoPagamento;

    @Schema(description = "Dados do pedido vinculado ao pagamento")
    private PedidoDto pedido;

    @Schema(description = "Dados do usuário vinculado ao pagamento")
    private UsuarioDto usuario;


    @Schema(description = "Dados resumidos do pedido")
    public static class PedidoDto {
        @Schema(description = "ID do pedido", example = "1")
        private Integer id;

        @Schema(description = "Valor total do pedido", example = "150.00")
        private BigDecimal valorTotal;

        private Integer statusProducaoId;
        private String statusProducaoNome;

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }

        public BigDecimal getValorTotal() { return valorTotal; }
        public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

        public String getStatusProducaoNome() {
            return statusProducaoNome;
        }

        public void setStatusProducaoNome(String statusProducaoNome) {
            this.statusProducaoNome = statusProducaoNome;
        }

        public Integer getStatusProducaoId() {
            return statusProducaoId;
        }

        public void setStatusProducaoId(Integer statusProducaoId) {
            this.statusProducaoId = statusProducaoId;
        }
    }

    @Schema(description = "Dados resumidos do usuário")
    public static class UsuarioDto {
        @Schema(description = "ID do usuário", example = "1")
        private Integer id;

        @Schema(description = "Nome do usuário", example = "João Silva")
        private String nome;

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
    }

    @Schema(description = "Dados resumidos do método de pagamento")
    public static class MetodoPagamentoDto {
        @Schema(description = "ID do método de pagamento", example = "1")
        private Integer id;

        @Schema(description = "Nome do método de pagamento", example = "Pix")
        private String nome;

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
    }

    @Schema(description = "Dados resumidos do status de produção")
    public static class StatusProducaoDto {
        @Schema(description = "ID do status de produção", example = "2")
        private Integer id;

        @Schema(description = "Nome do status de produção", example = "Em Produção")
        private String nome;

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
    }


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public LocalDateTime getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDateTime dataPagamento) { this.dataPagamento = dataPagamento; }

    public MetodoPagamentoDto getMetodoPagamento() { return metodoPagamento; }
    public void setMetodoPagamento(MetodoPagamentoDto metodoPagamento) { this.metodoPagamento = metodoPagamento; }

    public PedidoDto getPedido() { return pedido; }
    public void setPedido(PedidoDto pedido) { this.pedido = pedido; }

    public UsuarioDto getUsuario() { return usuario; }
    public void setUsuario(UsuarioDto usuario) { this.usuario = usuario; }
}
