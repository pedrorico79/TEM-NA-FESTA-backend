package com.temnafesta.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime dataPedido;

    @Column(nullable = false)
    private LocalDateTime dataEntrega;

    @Column(nullable = false)
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_atual", nullable = false)
    private StatusProducao statusProducao;

    private String observacao;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private Boolean isAtivo = true;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoProduto> produtos = new ArrayList<>();

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoricoStatusPedido> historicoStatus = new ArrayList<>();

    @OneToMany(mappedBy = "pedido", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Pagamento> pagamentos = new ArrayList<>();


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

    public @Positive BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(@Positive BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public StatusProducao getStatusProducao() {
        return statusProducao;
    }

    public void setStatusProducao(StatusProducao statusProducao) {
        this.statusProducao = statusProducao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Boolean getAtivo() {
        return isAtivo;
    }

    public void setAtivo(Boolean ativo) {
        isAtivo = ativo;
    }

    public List<PedidoProduto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<PedidoProduto> produtos) {
        this.produtos = produtos;
    }

    public List<HistoricoStatusPedido> getHistoricoStatus() {
        return historicoStatus;
    }

    public void setHistoricoStatus(List<HistoricoStatusPedido> historicoStatus) {
        this.historicoStatus = historicoStatus;
    }

    public List<Pagamento> getPagamentos() {
        return pagamentos;
    }

    public void setPagamentos(List<Pagamento> pagamentos) {
        this.pagamentos = pagamentos;
    }
}
