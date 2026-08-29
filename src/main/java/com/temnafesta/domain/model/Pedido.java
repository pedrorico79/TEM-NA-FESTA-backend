package com.temnafesta.domain.model;

import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.vo.StatusProducaoEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pedido {
    private Long id;
    private LocalDateTime dataPedido;
    private LocalDateTime dataEntrega;
    private BigDecimal valorTotal;
    private BigDecimal taxaEntrega;
    private String observacao;
    private StatusProducaoEnum statusProducao;
    private Long clienteId;
    private Long usuarioId;
    private Long eventoId;            // NULL se não for pedido de evento sazonal
    private Long enderecoEntregaId;   // NULL se for retirada na confeitaria
    private List<ItemPedido> itens = new ArrayList<>();
    private List<Pagamento> pagamentos = new ArrayList<>();
    private boolean ativo = true;
    private boolean deletado = false;

    public Pedido(Long id, LocalDateTime dataPedido, LocalDateTime dataEntrega, BigDecimal taxaEntrega,
                  String observacao, Long clienteId, Long usuarioId, Long eventoId, Long enderecoEntregaId,
                  List<ItemPedido> itens, List<Pagamento> pagamentos) {
        this.id = id;
        this.dataPedido = dataPedido != null ? dataPedido : LocalDateTime.now();
        this.dataEntrega = dataEntrega;
        this.taxaEntrega = taxaEntrega != null ? taxaEntrega : BigDecimal.ZERO;
        this.observacao = observacao;
        this.statusProducao = StatusProducaoEnum.RASCUNHO;
        this.clienteId = clienteId;
        this.usuarioId = usuarioId;
        this.eventoId = eventoId;
        this.enderecoEntregaId = enderecoEntregaId;
        this.valorTotal = BigDecimal.ZERO;
        // Garante que a lista interna seja mutável para os métodos adicionarItem e adicionarPagamento
        if (itens != null) this.itens.addAll(itens);
        if (pagamentos != null) this.pagamentos.addAll(pagamentos);
        // Recalcula o total caso o pedido já venha do banco com itens
        recalcularValorTotal();
    }

    // --- REGRAS DE NEGÓCIO DA MÁQUINA DE ESTADOS ---

    public void transitarPara(StatusProducaoEnum novoStatus) {
        if (!this.statusProducao.podeTransitarPara(novoStatus)) {
            throw new RegraDeNegocioException(
                    String.format("Não é permitida a transição direta do status %s para %s.",
                            this.statusProducao, novoStatus)
            );
        }

        // Regra de Negócio: Não permite iniciar produção se o sinal (mínimo 50%) não estiver pago
        if (StatusProducaoEnum.EM_PRODUCAO.equals(novoStatus) && !isSinalPago()) {
            throw new RegraDeNegocioException(
                    "Não é possível iniciar a produção. É necessário a confirmação do pagamento do sinal (mínimo 50%)."
            );
        }

        this.statusProducao = novoStatus;
    }

    public void adicionarItem(ItemPedido item) {
        // Impede alterar os itens se o pedido já estiver em produção, pronto ou finalizado
        if (List.of(StatusProducaoEnum.EM_PRODUCAO, StatusProducaoEnum.PRONTO_PARA_ENTREGA,
                StatusProducaoEnum.ENTREGUE, StatusProducaoEnum.CANCELADO).contains(this.statusProducao)) {
            throw new RegraDeNegocioException("Não é permitido alterar os itens de um pedido que já está em produção ou finalizado.");
        }
        this.itens.add(item);
        recalcularValorTotal();
    }

    public void adicionarPagamento(Pagamento pagamento) {
        this.pagamentos.add(pagamento);
    }

    public void recalcularValorTotal() {
        BigDecimal totalItens = itens.stream()
                .map(ItemPedido::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.valorTotal = totalItens.add(this.taxaEntrega);
    }

    public BigDecimal calcularTotalPago() {
        return pagamentos.stream()
                .filter(Pagamento::isConfirmado)
                .map(Pagamento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isSinalPago() {
        BigDecimal valorMinimoSinal = this.valorTotal.multiply(new BigDecimal("0.50")); // 50%
        return calcularTotalPago().compareTo(valorMinimoSinal) >= 0;
    }

    // --- MÉTODOS DE ATUALIZAÇÃO ---

    public void atualizarDadosBasicos(LocalDateTime dataEntrega, BigDecimal taxaEntrega, String observacao, Long enderecoEntregaId) {
        if (List.of(StatusProducaoEnum.ENTREGUE, StatusProducaoEnum.CANCELADO).contains(this.statusProducao)) {
            throw new RegraDeNegocioException("Não é permitido alterar dados de um pedido finalizado ou cancelado.");
        }

        this.dataEntrega = dataEntrega;
        this.taxaEntrega = taxaEntrega != null ? taxaEntrega : BigDecimal.ZERO;
        this.observacao = observacao;
        this.enderecoEntregaId = enderecoEntregaId;

        recalcularValorTotal();
    }

    public void substituirItens(List<ItemPedido> novosItens) {
        if (List.of(StatusProducaoEnum.EM_PRODUCAO, StatusProducaoEnum.PRONTO_PARA_ENTREGA,
                StatusProducaoEnum.ENTREGUE, StatusProducaoEnum.CANCELADO).contains(this.statusProducao)) {
            throw new RegraDeNegocioException("Não é permitido alterar os itens de um pedido que já está em produção ou finalizado.");
        }

        this.itens.clear();
        this.itens.addAll(novosItens);

        recalcularValorTotal();
    }

    // Getters
    public Long getId() { return id; }
    public LocalDateTime getDataPedido() { return dataPedido; }
    public LocalDateTime getDataEntrega() { return dataEntrega; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public BigDecimal getTaxaEntrega() { return taxaEntrega; }
    public String getObservacao() { return observacao; }
    public StatusProducaoEnum getStatusProducao() { return statusProducao; }
    public Long getClienteId() { return clienteId; }
    public Long getUsuarioId() { return usuarioId; }
    public Long getEventoId() { return eventoId; }
    public Long getEnderecoEntregaId() { return enderecoEntregaId; }
    public List<ItemPedido> getItens() { return Collections.unmodifiableList(itens); }
    public List<Pagamento> getPagamentos() { return Collections.unmodifiableList(pagamentos); }
    public boolean isAtivo() { return ativo; }
    public boolean isDeletado() { return deletado; }
}