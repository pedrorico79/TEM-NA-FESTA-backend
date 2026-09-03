package com.temnafesta.infrastructure.persistence.adapter;

import com.temnafesta.application.dto.relatorio.PedidosPeriodoOutput;
import com.temnafesta.application.dto.relatorio.PedidosPorSemanaOutput;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;
import com.temnafesta.domain.vo.StatusProducaoEnum;
import com.temnafesta.infrastructure.persistence.entity.PedidoJpaEntity;
import com.temnafesta.infrastructure.persistence.mapper.PedidoPersistenceMapper;
import com.temnafesta.infrastructure.persistence.repository.SpringDataPedidoRepository;
import com.temnafesta.infrastructure.projection.PedidosPeriodoProjection;
import com.temnafesta.infrastructure.projection.PedidosPorSemanaProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PedidoRepositoryAdapter implements PedidoRepositoryPort {

    private final SpringDataPedidoRepository repository;
    private final PedidoPersistenceMapper mapper;

    public PedidoRepositoryAdapter(SpringDataPedidoRepository repository, PedidoPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Pedido salvar(Pedido pedido) {
        PedidoJpaEntity entity = mapper.toEntity(pedido);
        PedidoJpaEntity entitySalva = repository.save(entity);
        return mapper.toDomain(entitySalva);
    }

    @Override
    public Optional<Pedido> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Pedido> listarPorFiltros(StatusProducaoEnum status, LocalDateTime inicio, LocalDateTime fim) {
        return repository.listarPorFiltros(status, inicio, fim).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Long countByDataPedidoBetween(LocalDateTime de, LocalDateTime ate) {
        return repository.countByDataPedidoBetween(de, ate);
    }

    @Override
    public Long countByStatusEPeriodo(Integer statusId, LocalDateTime de, LocalDateTime ate) {
        return repository.countByStatusEPeriodo(statusId, de, ate);
    }

    @Override
    public BigDecimal somarFaturamentoNoPeriodo(Integer statusEntregueProducao, LocalDateTime de, LocalDateTime ate) {
        return repository.somarFaturamentoNoPeriodo(statusEntregueProducao, de, ate);
    }

    @Override
    public List<PedidosPorSemanaOutput> buscarPedidosAgrupadosPorSemana(LocalDateTime de, LocalDateTime ate) {
        List<PedidosPorSemanaProjection> resultado = repository.buscarPedidosAgrupadosPorSemana(de, ate);
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
