package com.temnafesta.domain.model;

import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.vo.StatusProducaoEnum;
import com.temnafesta.domain.vo.StatusPagamentoEnum;

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
    private Endereco enderecoEntrega; // Objeto completo para exibição no frontend (carregado via join)
    private List<ItemPedido> itens = new ArrayList<>();
    private List<Pagamento> pagamentos = new ArrayList<>();
    private boolean ativo = true;
    private boolean deletado = false;

    public Pedido(Long id, LocalDateTime dataPedido, LocalDateTime dataEntrega,
                  BigDecimal taxaEntrega, String observacao,
                  StatusProducaoEnum statusProducao,
                  Long clienteId, Long usuarioId, Long eventoId,
                  Endereco enderecoEntrega,
                  List<ItemPedido> itens, List<Pagamento> pagamentos,
                  boolean ativo, boolean deletado) {

        this.id = id;
        this.dataPedido = dataPedido != null ? dataPedido : LocalDateTime.now();
        this.dataEntrega = dataEntrega;
        this.taxaEntrega = taxaEntrega != null ? taxaEntrega : BigDecimal.ZERO;
        this.observacao = observacao;

        this.statusProducao = statusProducao != null
                ? statusProducao
                : StatusProducaoEnum.RASCUNHO;

        this.clienteId = clienteId;
        this.usuarioId = usuarioId;
        this.eventoId = eventoId;
        this.enderecoEntrega = enderecoEntrega;
        this.ativo = ativo;
        this.deletado = deletado;
        this.valorTotal = BigDecimal.ZERO;
        // Garante que a lista interna seja mutável para os métodos adicionarItem e adicionarPagamento
        if (itens != null) {
            this.itens.addAll(itens);
        }

        if (pagamentos != null) {
            this.pagamentos.addAll(pagamentos);
        }
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
        if (pagamento == null) {
            throw new RegraDeNegocioException("O pagamento é obrigatório.");
        }
        if (deletado || StatusProducaoEnum.CANCELADO.equals(statusProducao)) {
            throw new RegraDeNegocioException("Não é possível registrar pagamento em pedido cancelado ou excluído.");
        }
        if (pagamento.getValor() == null || pagamento.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("O valor do pagamento deve ser maior que zero.");
        }
        if (StatusPagamentoEnum.CANCELADO.equals(pagamento.getStatusPagamento())) {
            throw new RegraDeNegocioException("Não é possível registrar um pagamento já cancelado.");
        }
        if (pagamento.getChaveIdempotencia() != null && pagamentos.stream()
                .anyMatch(atual -> pagamento.getChaveIdempotencia().equals(atual.getChaveIdempotencia()))) {
            throw new RegraDeNegocioException("Esta tentativa de pagamento já foi registrada.");
        }
        BigDecimal totalComprometido = pagamentos.stream()
                .filter(atual -> !StatusPagamentoEnum.CANCELADO.equals(atual.getStatusPagamento()))
                .map(Pagamento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (totalComprometido.add(pagamento.getValor()).compareTo(valorTotal) > 0) {
            throw new RegraDeNegocioException("O pagamento ultrapassa o saldo restante do pedido.");
        }
        this.pagamentos.add(pagamento);
    }

    public void confirmarPagamento(Long pagamentoId) {
        if (deletado || StatusProducaoEnum.CANCELADO.equals(statusProducao)) {
            throw new RegraDeNegocioException("Não é possível confirmar pagamento de pedido cancelado ou excluído.");
        }
        encontrarPagamento(pagamentoId).confirmar();
    }

    public void cancelarPagamento(Long pagamentoId) {
        encontrarPagamento(pagamentoId).cancelar();
    }

    public Pagamento buscarPagamentoPorChaveIdempotencia(String chave) {
        if (chave == null) return null;
        return pagamentos.stream()
                .filter(pagamento -> chave.equals(pagamento.getChaveIdempotencia()))
                .findFirst()
                .orElse(null);
    }

    private Pagamento encontrarPagamento(Long pagamentoId) {
        return pagamentos.stream()
                .filter(pagamento -> pagamentoId != null && pagamentoId.equals(pagamento.getId()))
                .findFirst()
                .orElseThrow(() -> new RegraDeNegocioException("Pagamento não encontrado neste pedido."));
    }

    public void removerPagamento(Long pagamentoId) {
        boolean removido = this.pagamentos.removeIf(p -> p.getId() != null && p.getId().equals(pagamentoId));

        if (!removido) {
            throw new RegraDeNegocioException("Pagamento não encontrado com ID: " + pagamentoId + " para este pedido.");
        }
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

    public BigDecimal calcularTotalComprometido() {
        return pagamentos.stream()
                .filter(pagamento -> !StatusPagamentoEnum.CANCELADO.equals(pagamento.getStatusPagamento()))
                .map(Pagamento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isSinalPago() {
        BigDecimal valorMinimoSinal = this.valorTotal.multiply(new BigDecimal("0.50")); // 50%
        return calcularTotalPago().compareTo(valorMinimoSinal) >= 0;
    }

    // --- MÉTODOS DE ATUALIZAÇÃO ---

    public void atualizarDadosBasicos(LocalDateTime dataEntrega, BigDecimal taxaEntrega, String observacao, Endereco enderecoEntrega) {
        if (List.of(StatusProducaoEnum.ENTREGUE, StatusProducaoEnum.CANCELADO).contains(this.statusProducao)) {
            throw new RegraDeNegocioException("Não é permitido alterar dados de um pedido finalizado ou cancelado.");
        }

        this.dataEntrega = dataEntrega;
        this.taxaEntrega = taxaEntrega != null ? taxaEntrega : BigDecimal.ZERO;
        this.observacao = observacao;
        this.enderecoEntrega = enderecoEntrega;

        recalcularValorTotal();
    }

    public void atualizarEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }

    public void substituirItens(List<ItemPedido> novosItens) {

        boolean itensForamAlterados = !itensIguais(this.itens, novosItens);

        if (itensForamAlterados &&
                List.of(
                        StatusProducaoEnum.EM_PRODUCAO,
                        StatusProducaoEnum.PRONTO_PARA_ENTREGA,
                        StatusProducaoEnum.ENTREGUE,
                        StatusProducaoEnum.CANCELADO
                ).contains(this.statusProducao)) {

            throw new RegraDeNegocioException(
                    "Não é permitido alterar os itens de um pedido que já está em produção ou finalizado."
            );
        }

        if (itensForamAlterados) {
            this.itens.clear();
            this.itens.addAll(novosItens);
        }

        recalcularValorTotal();
    }

    private boolean itensIguais(List<ItemPedido> itensAtuais, List<ItemPedido> novosItens) {

        if (itensAtuais.size() != novosItens.size()) {
            return false;
        }

        for (int i = 0; i < itensAtuais.size(); i++) {

            ItemPedido atual = itensAtuais.get(i);
            ItemPedido novo = novosItens.get(i);

            if (!java.util.Objects.equals(atual.getProdutoId(), novo.getProdutoId())) {
                return false;
            }

            if (!java.util.Objects.equals(atual.getQuantidade(), novo.getQuantidade())) {
                return false;
            }

            if (atual.getPrecoUnitario().compareTo(novo.getPrecoUnitario()) != 0) {
                return false;
            }

            if (!java.util.Objects.equals(atual.getObservacaoItem(), novo.getObservacaoItem())) {
                return false;
            }
        }

        return true;
    }

    // --- MÉTODOS DE CICLO DE VIDA ---

    public void excluirLogicamente() {
        if (this.deletado) {
            throw new RegraDeNegocioException("Este pedido já foi deletado.");
        }

        if (StatusProducaoEnum.ENTREGUE.equals(this.statusProducao)) {
            throw new RegraDeNegocioException("Não é possível cancelar ou excluir um pedido que já foi entregue.");
        }

        // Se o pedido ainda não estiver cancelado, força a transição pela Máquina de Estados
        if (!StatusProducaoEnum.CANCELADO.equals(this.statusProducao)) {
            transitarPara(StatusProducaoEnum.CANCELADO);
        }

        this.deletado = true;
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
    public Endereco getEnderecoEntrega() { return enderecoEntrega; }
    public void setEnderecoEntrega(Endereco enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }
    public List<ItemPedido> getItens() { return Collections.unmodifiableList(itens); }
    public List<Pagamento> getPagamentos() { return Collections.unmodifiableList(pagamentos); }
    public boolean isAtivo() { return ativo; }
    public boolean isDeletado() { return deletado; }
}
