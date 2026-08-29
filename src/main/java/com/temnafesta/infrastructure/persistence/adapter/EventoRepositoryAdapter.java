package com.temnafesta.infrastructure.persistence.adapter;

import com.temnafesta.domain.model.Evento;
import com.temnafesta.domain.ports.repository.EventoRepositoryPort;
import com.temnafesta.infrastructure.persistence.entity.EventoJpaEntity;
import com.temnafesta.infrastructure.persistence.mapper.GeralPersistenceMapper;
import com.temnafesta.infrastructure.persistence.repository.SpringDataEventoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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
    public Evento salvar(Evento evento) {
        EventoJpaEntity entity = mapper.toEntity(evento);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<Evento> buscarPorId(Long id) {
        return repository.findByIdAndDeletadoFalse(id).map(mapper::toDomain);
    }

    @Override
    public Evento atualizar(Evento evento) {
        EventoJpaEntity entity = mapper.toEntity(evento);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public void deletar(Long id) {
        repository.findById(id).ifPresent(entity -> {
            entity.setDeletado(true);
            repository.save(entity);
        });
    }

    @Override
    public Evento alterarStatus(Long id, boolean ativo) {
        EventoJpaEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento não encontrado com o ID: " + id));
        entity.setAtivo(ativo);
        return mapper.toDomain(repository.save(entity));
    }
}