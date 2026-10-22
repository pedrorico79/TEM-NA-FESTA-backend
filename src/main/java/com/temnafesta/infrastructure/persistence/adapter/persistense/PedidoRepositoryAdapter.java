package com.temnafesta.infrastructure.persistence.adapter.persistense;

import com.temnafesta.domain.model.ItemPedido;
import com.temnafesta.domain.model.Pagamento;
import com.temnafesta.application.dto.relatorio.PedidosPeriodoOutput;
import com.temnafesta.application.dto.relatorio.PedidosPorSemanaOutput;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;
import com.temnafesta.domain.vo.StatusProducaoEnum;
import com.temnafesta.infrastructure.persistence.entity.PedidoJpaEntity;
import com.temnafesta.infrastructure.persistence.entity.EnderecoJpaEntity;
import com.temnafesta.infrastructure.persistence.mapper.PedidoPersistenceMapper;
import com.temnafesta.infrastructure.persistence.repository.SpringDataEnderecoRepository;
import com.temnafesta.infrastructure.persistence.repository.SpringDataPedidoRepository;
import com.temnafesta.infrastructure.projection.PedidosPeriodoProjection;
import com.temnafesta.infrastructure.projection.PedidosPorSemanaProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import com.temnafesta.domain.exception.NaoEncontradoException;

@Component
@Transactional(readOnly = true)
public class PedidoRepositoryAdapter implements PedidoRepositoryPort {

    private final SpringDataPedidoRepository repository;
    private final SpringDataEnderecoRepository enderecoRepository;
    private final PedidoPersistenceMapper mapper;

    public PedidoRepositoryAdapter(SpringDataPedidoRepository repository,
                                    SpringDataEnderecoRepository enderecoRepository,
                                    PedidoPersistenceMapper mapper) {
        this.repository = repository;
        this.enderecoRepository = enderecoRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Pedido salvar(Pedido pedido) {
        PedidoJpaEntity entity = mapper.toEntity(pedido);
        if (pedido.getId() != null) {
            PedidoJpaEntity existente = repository.findById(pedido.getId())
                    .orElseThrow(() -> new NaoEncontradoException("Pedido não encontrado com ID: " + pedido.getId()));
            entity.setVersao(existente.getVersao());
            entity.setEnderecoEntrega(resolverEnderecoEntrega(entity.getEnderecoEntrega(), existente.getEnderecoEntrega()));
        } else if (entity.getEnderecoEntrega() != null) {
            entity.setEnderecoEntrega(enderecoRepository.save(entity.getEnderecoEntrega()));
        }
        PedidoJpaEntity entitySalva = repository.save(entity);
        return mapper.toDomain(entitySalva);
    }

    @Override
    @Transactional
    public Pedido atualizarStatus(Long pedidoId, StatusProducaoEnum novoStatus) {
        PedidoJpaEntity pedidoGerenciado = repository.buscarPorIdParaAtualizarStatus(pedidoId)
                .orElseThrow(() -> new NaoEncontradoException("Pedido não encontrado com ID: " + pedidoId));
        Pedido pedidoValidado = mapper.toDomain(pedidoGerenciado);
        pedidoValidado.transitarPara(novoStatus);
        pedidoGerenciado.setStatusProducao(novoStatus);
        return mapper.toDomain(repository.saveAndFlush(pedidoGerenciado));
    }

    @Override
    public Optional<Pedido> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Pedido> buscarPorIdParaRegistrarPagamento(Long id) {
        return repository.buscarPorIdParaRegistrarPagamento(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Pedido> buscarPorChaveIdempotencia(String chave) {
        return repository.buscarPorChaveIdempotencia(chave).map(mapper::toDomain);
    }

    @Override
    public List<Pedido> listarPorFiltros(StatusProducaoEnum status, LocalDateTime inicio, LocalDateTime fim) {
        return repository.listarPorFiltros(status, inicio, fim).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existePedidoEmAndamentoPorCliente(Long clienteId) {
        return repository.existePedidoEmAndamentoPorCliente(clienteId);
    }

    @Override
    @Transactional
    public Pedido atualizar(Pedido pedido) {
        PedidoJpaEntity entity = mapper.toEntity(pedido);
        if (pedido.getId() != null) {
            PedidoJpaEntity existente = repository.findById(pedido.getId())
                    .orElseThrow(() -> new NaoEncontradoException("Pedido não encontrado com ID: " + pedido.getId()));
            entity.setVersao(existente.getVersao());
            entity.setEnderecoEntrega(resolverEnderecoEntrega(entity.getEnderecoEntrega(), existente.getEnderecoEntrega()));
        } else if (entity.getEnderecoEntrega() != null) {
            entity.setEnderecoEntrega(enderecoRepository.save(entity.getEnderecoEntrega()));
        }
        PedidoJpaEntity entityAtualizada = repository.save(entity);
        return mapper.toDomain(entityAtualizada);
    }

    private EnderecoJpaEntity resolverEnderecoEntrega(EnderecoJpaEntity enderecoRecebido,
                                                       EnderecoJpaEntity enderecoAtual) {
        if (enderecoRecebido == null) {
            return null;
        }
        if (mesmoEndereco(enderecoRecebido, enderecoAtual)) {
            return enderecoAtual;
        }
        return enderecoRepository.save(enderecoRecebido);
    }

    private boolean mesmoEndereco(EnderecoJpaEntity primeiro, EnderecoJpaEntity segundo) {
        return segundo != null
                && Objects.equals(primeiro.getCep(), segundo.getCep())
                && Objects.equals(primeiro.getLogradouro(), segundo.getLogradouro())
                && Objects.equals(primeiro.getNumero(), segundo.getNumero())
                && Objects.equals(primeiro.getComplemento(), segundo.getComplemento())
                && Objects.equals(primeiro.getBairro(), segundo.getBairro())
                && Objects.equals(primeiro.getCidade(), segundo.getCidade())
                && Objects.equals(primeiro.getEstado(), segundo.getEstado());
    }

    @Override
    public long contarPorStatus(StatusProducaoEnum status) {
        return repository.countByStatusProducaoAndDeletadoFalse(status);
    }

    @Override
    public long contarPedidosComPagamentoPendente() {
        return repository.contarPedidosComPagamentoPendente();
    }

    @Override
    public List<Pedido> listarProximasRetiradas(LocalDateTime limite) {
        return repository.listarProximasRetiradas(limite).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Pedido> listarPedidos(String busca, StatusProducaoEnum status, Long eventoId) {
        return listarPedidos(busca, status, eventoId, Pageable.unpaged()).getContent();
    }

    @Override
    public Page<Pedido> listarPedidos(String busca, StatusProducaoEnum status, Long eventoId, Pageable pageable) {
        return repository.listarPedidos(busca, status, eventoId, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<ItemPedido> buscarItemPorId(Long pedidoId, Long itemId) {
        return repository.findById(pedidoId)
                .flatMap(pedido -> pedido.getItens().stream()
                        .filter(item -> item.getId().equals(itemId))
                        .findFirst())
                .map(mapper::toDomain);
    }

    @Override
    public List<Pagamento> listarPagamentos(Long pedidoId) {
        return repository.findById(pedidoId)
                .map(PedidoJpaEntity::getPagamentos)
                .orElse(List.of())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Long countByDataPedidoBetweenAndDeletadoFalse(LocalDateTime de, LocalDateTime ate, Long eventoId) {
        return repository.countByDataPedidoBetweenAndDeletadoFalse(de, ate, eventoId);
    }

    @Override
    public Long countByStatusEPeriodo(StatusProducaoEnum statusId, LocalDateTime de, LocalDateTime ate, Long eventoId) {
        return repository.countByStatusEPeriodo(statusId, de, ate, eventoId);
    }

    @Override
    public BigDecimal somarFaturamentoNoPeriodo(LocalDateTime de, LocalDateTime ate, Long eventoId) {
        return repository.somarFaturamentoNoPeriodo(de, ate, eventoId);
    }

    @Override
    public List<PedidosPorSemanaOutput> buscarPedidosAgrupadosPorSemana(LocalDateTime de, LocalDateTime ate, Long eventoId) {
        List<PedidosPorSemanaProjection> resultado = repository.buscarPedidosAgrupadosPorSemana(de, ate, eventoId);
        if (resultado == null) return List.of();

        return resultado.stream()
                .map(p -> new PedidosPorSemanaOutput(
                        p.getRotulo(),
                        p.getPeriodo(),
                        p.getQuantidade()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Page<PedidosPeriodoOutput> buscarPedidosPeriodoPaginado(LocalDateTime de, LocalDateTime ate, Pageable pageable) {
        Objects.requireNonNull(de);
        Objects.requireNonNull(ate);
        Objects.requireNonNull(pageable);

        Page<PedidosPeriodoProjection> pageProjection = repository.buscarPedidosPeriodoPaginado(de, ate, pageable);
        if (pageProjection == null) return Page.empty();

        return pageProjection.map(p -> new PedidosPeriodoOutput(
                p.getId(),
                p.getDataPedido(),
                p.getClienteNome(),
                p.getEventoNome(),
                p.getValorTotal(),
                p.getValorPago(),
                p.getStatusNome()
        ));
    }

    @Override
    public List<PedidosPeriodoOutput> buscarRelatorioDinamico(Integer eventoId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        List<PedidosPeriodoProjection> resultados =
                repository.buscarRelatorioDinamico(eventoId, dataInicio, dataFim);

        return resultados.stream()
                .map(p -> new PedidosPeriodoOutput(
                        p.getId(),
                        p.getDataPedido(),
                        p.getClienteNome(),
                        p.getEventoNome(),
                        p.getValorTotal(),
                        p.getValorPago(),
                        p.getStatusNome()
                ))
                .toList();
    }
}
