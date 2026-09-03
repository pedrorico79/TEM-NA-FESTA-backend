package com.temnafesta.infrastructure.persistence.adapter;

import com.temnafesta.application.dto.relatorio.EventoComparativoOutput;
import com.temnafesta.domain.model.Evento;
import com.temnafesta.domain.ports.repository.EventoRepositoryPort;
import com.temnafesta.infrastructure.persistence.mapper.GeralPersistenceMapper;
import com.temnafesta.infrastructure.persistence.repository.SpringDataEventoRepository;
import com.temnafesta.infrastructure.projection.EventosComparativoProjection;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class EventoRepositoryAdapter implements EventoRepositoryPort {

    private final SpringDataEventoRepository repository;
    private final GeralPersistenceMapper mapper;

    public EventoRepositoryAdapter(SpringDataEventoRepository repository, GeralPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<Evento> listarEventosAtivos() {
        return repository.findByAtivoTrueAndDeletadoFalse().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<EventoComparativoOutput> buscarComparativoEventos(LocalDateTime de, LocalDateTime ate) {
        List<EventosComparativoProjection> resultados =
                repository.buscarComparativoEventos(de, ate);

        return resultados.stream()
                .map(r -> new EventoComparativoOutput(
                        r.getEvento(),
                        r.getPedidosTotais(),
                        r.getVendasObtidas(),
                        r.getFaturamento(),
                        r.getTicketMedio()
                ))
                .toList();
    }
}