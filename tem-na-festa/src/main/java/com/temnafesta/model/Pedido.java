package com.temnafesta.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDate dataPedido;

    @FutureOrPresent
    @Column(nullable = false)
    private LocalDate dataEntrega;

    @Positive
    @Column(nullable = false)
    private Double valorTotal;

    private String observacao;

    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @JoinColumn(name = "status_producao_id", nullable = false)
    private StatusProducao statusProducao;

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

    public StatusProducao getStatusProducao() {
        return statusProducao;
    }

    public void setStatusProducao(StatusProducao statusProducao) {
        this.statusProducao = statusProducao;
    }
}
