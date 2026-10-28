package com.temnafesta.domain.model;

import com.temnafesta.domain.exception.RegraDeNegocioException;

import java.math.BigDecimal;

public class ItemPedido {

    private Long id;
    private Long produtoId;
    private Produto produto;
    private Integer quantidade;
    private BigDecimal precoUnitario; // Preço congelado no momento da venda
    private BigDecimal acrescimo;      // Acréscimo em reais aplicado à linha do item
    private String observacaoItem;    // Personalizações do item

    public ItemPedido(
            Long id,
            Long produtoId,
            Integer quantidade,
            BigDecimal precoUnitario,
            String observacaoItem
    ) {
        if (quantidade == null || quantidade <= 0) {
            throw new RegraDeNegocioException(
                    "A quantidade do item deve ser maior que zero."
            );
        }

        if (precoUnitario == null || precoUnitario.compareTo(BigDecimal.ZERO) < 0) {
            throw new RegraDeNegocioException(
                    "O preço unitário não pode ser negativo."
            );
        }
        this.id = id;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.acrescimo = BigDecimal.ZERO;
        this.observacaoItem = observacaoItem;
    }

    public static ItemPedido comAcrescimo(Long id, Long produtoId, Integer quantidade,
                                          BigDecimal precoUnitario, BigDecimal acrescimo,
                                          String observacaoItem) {
        ItemPedido item = new ItemPedido(id, produtoId, quantidade, precoUnitario, observacaoItem);
        item.definirAcrescimo(acrescimo);
        return item;
    }

    public void definirAcrescimo(BigDecimal acrescimo) {
        if (acrescimo != null && acrescimo.compareTo(BigDecimal.ZERO) < 0) {
            throw new RegraDeNegocioException("O acréscimo do item não pode ser negativo.");
        }
        this.acrescimo = acrescimo != null ? acrescimo : BigDecimal.ZERO;
    }

    public BigDecimal calcularSubtotal() {
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade)).add(acrescimo);
    }

    // Getters
    public Long getId() { return id; }
    public Long getProdutoId() { return produtoId; }
    public Produto getProduto() { return produto; }
    public Integer getQuantidade() { return quantidade; }
    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public BigDecimal getAcrescimo() { return acrescimo; }
    public String getObservacaoItem() { return observacaoItem; }

    // Setter
    public void setProduto(Produto produto) { this.produto = produto; }
}
