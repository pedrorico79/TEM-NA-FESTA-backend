package com.temnafesta.application.usecase;

import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.Pagamento;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.ports.repository.MetodoPagamentoRepositoryPort;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;
import com.temnafesta.domain.vo.StatusPagamentoEnum;
import com.temnafesta.domain.vo.StatusProducaoEnum;
import com.temnafesta.domain.vo.TipoPagamentoEnum;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.Objects;
import java.util.UUID;

public class RegistrarPagamentoUseCase {

    private final PedidoRepositoryPort pedidoRepositoryPort;
    private final MetodoPagamentoRepositoryPort metodoPagamentoRepositoryPort;
    private final java.time.Clock relogioNegocio;

    public RegistrarPagamentoUseCase(PedidoRepositoryPort pedidoRepositoryPort,
                                     MetodoPagamentoRepositoryPort metodoPagamentoRepositoryPort) {
        this(pedidoRepositoryPort, metodoPagamentoRepositoryPort,
                com.temnafesta.domain.time.RelogioNegocio.sistema());
    }

    public RegistrarPagamentoUseCase(PedidoRepositoryPort pedidoRepositoryPort,
                                     MetodoPagamentoRepositoryPort metodoPagamentoRepositoryPort,
                                     java.time.Clock relogioNegocio) {
        this.pedidoRepositoryPort = pedidoRepositoryPort;
        this.metodoPagamentoRepositoryPort = metodoPagamentoRepositoryPort;
        this.relogioNegocio = relogioNegocio != null
                ? relogioNegocio : com.temnafesta.domain.time.RelogioNegocio.sistema();
    }

    @Transactional
    public Pedido executar(Long pedidoId, BigDecimal valor, TipoPagamentoEnum tipo,
                           Long metodoPagamentoId, Long usuarioId, StatusPagamentoEnum status,
                           String chaveIdempotencia) {
        validarChaveIdempotencia(chaveIdempotencia);
        validarDados(valor, tipo, metodoPagamentoId, usuarioId);

        StatusPagamentoEnum statusInicial = status == null ? StatusPagamentoEnum.CONFIRMADO : status;
        if (statusInicial == StatusPagamentoEnum.CANCELADO) {
            throw new RegraDeNegocioException("Um pagamento não pode ser criado já cancelado.");
        }
        String assinatura = gerarAssinatura(valor, tipo, metodoPagamentoId, statusInicial);

        Pedido pedidoComChave = pedidoRepositoryPort.buscarPorChaveIdempotencia(chaveIdempotencia).orElse(null);
        if (pedidoComChave != null) {
            return reutilizarOuRejeitar(pedidoComChave, pedidoId, chaveIdempotencia, assinatura);
        }

        Pedido pedido = pedidoRepositoryPort.buscarPorIdParaRegistrarPagamento(pedidoId)
                .orElseThrow(() -> new RegraDeNegocioException("Pedido não encontrado com ID: " + pedidoId));

        Pagamento pagamentoExistente = pedido.buscarPagamentoPorChaveIdempotencia(chaveIdempotencia);
        if (pagamentoExistente != null) {
            if (assinatura.equals(pagamentoExistente.getAssinaturaRequisicao())) return pedido;
            throw new RegraDeNegocioException("A chave de idempotência já foi usada com outros dados.");
        }

        metodoPagamentoRepositoryPort.buscarPorId(metodoPagamentoId)
                .orElseThrow(() -> new RegraDeNegocioException("Método de pagamento não encontrado."));

        Pagamento pagamento = new Pagamento(
                null, valor, LocalDateTime.now(relogioNegocio), tipo, statusInicial, metodoPagamentoId, usuarioId);
        pagamento.setChaveIdempotencia(chaveIdempotencia);
        pagamento.setAssinaturaRequisicao(assinatura);
        pedido.adicionarPagamento(pagamento);

        if (statusInicial == StatusPagamentoEnum.CONFIRMADO
                && StatusProducaoEnum.AGUARDANDO_SINAL.equals(pedido.getStatusProducao())) {
            pedido.transitarPara(StatusProducaoEnum.CONFIRMADO);
        }

        return pedidoRepositoryPort.salvar(pedido);
    }

    private Pedido reutilizarOuRejeitar(Pedido pedido, Long pedidoId, String chave, String assinatura) {
        Pagamento pagamento = pedido.buscarPagamentoPorChaveIdempotencia(chave);
        if (Objects.equals(pedido.getId(), pedidoId)
                && pagamento != null
                && assinatura.equals(pagamento.getAssinaturaRequisicao())) return pedido;
        throw new RegraDeNegocioException("A chave de idempotência já foi usada com outros dados.");
    }

    private void validarDados(BigDecimal valor, TipoPagamentoEnum tipo, Long metodoPagamentoId, Long usuarioId) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("O valor do pagamento deve ser maior que zero.");
        }
        if (tipo == null) throw new RegraDeNegocioException("O tipo de pagamento é obrigatório.");
        if (metodoPagamentoId == null) throw new RegraDeNegocioException("O método de pagamento é obrigatório.");
        if (usuarioId == null) throw new RegraDeNegocioException("Não foi possível identificar o usuário autenticado.");
    }

    private void validarChaveIdempotencia(String chave) {
        try {
            if (chave == null || !UUID.fromString(chave).toString().equalsIgnoreCase(chave)) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException ex) {
            throw new RegraDeNegocioException("A chave de idempotência deve ser um UUID válido.");
        }
    }

    private String gerarAssinatura(BigDecimal valor, TipoPagamentoEnum tipo, Long metodoId,
                                   StatusPagamentoEnum status) {
        String conteudo = valor.stripTrailingZeros().toPlainString() + "|" + tipo + "|" + metodoId + "|" + status;
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(conteudo.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Não foi possível validar a chave de idempotência.", ex);
        }
    }
}
