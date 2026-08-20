package com.temnafesta.domain.model;

import java.math.BigDecimal;

public class ItemPedido {
    private Long id;
    private Long produtoId;
    private Integer quantidade;
    private BigDecimal precoUnitario; // Preço congelado no momento da venda
    private String observacaoItem;     // Personalizações do item

    public ItemPedido(Long id, Long produtoId, Integer quantidade, BigDecimal precoUnitario, String observacaoItem) {
        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade do item deve ser maior que zero.");
        }
        if (precoUnitario == null || precoUnitario.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O preço unitário não pode ser negativo.");
        }
        this.id = id;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.observacaoItem = observacaoItem;
    }

    public BigDecimal calcularSubtotal() {
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    // Getters
    public Long getId() { return id; }
    public Long getProdutoId() { return produtoId; }
    public Integer getQuantidade() { return quantidade; }
    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public String getObservacaoItem() { return observacaoItem; }
}