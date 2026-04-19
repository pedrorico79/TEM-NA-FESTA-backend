package com.temnafesta.dto.pagamento;

import com.temnafesta.model.MetodoPagamento;
import com.temnafesta.model.StatusProducao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PagamentoResponseDto {

    private Integer id;
    private BigDecimal valor;
    private LocalDateTime dataPagamento;
    private MetodoPagamento metodo;
    private PedidoDto pedido;
    private UsuarioDto usuario;

    public static class PedidoDto {
        private Integer id;
        private BigDecimal valorTotal;
        private StatusProducao statusProducao;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public BigDecimal getValorTotal() {
            return valorTotal;
        }

        public void setValorTotal(BigDecimal valorTotal) {
            this.valorTotal = valorTotal;
        }

        public StatusProducao getStatusProducao() {
            return statusProducao;
        }

        public void setStatusProducao(StatusProducao statusProducao) {
            this.statusProducao = statusProducao;
        }
    }

    public static class UsuarioDto {
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

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public MetodoPagamento getMetodo() {
        return metodo;
    }

    public void setMetodo(MetodoPagamento metodo) {
        this.metodo = metodo;
    }

    public PedidoDto getPedido() {
        return pedido;
    }

    public void setPedido(PedidoDto pedido) {
        this.pedido = pedido;
    }

    public UsuarioDto getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDto usuario) {
        this.usuario = usuario;
    }
}