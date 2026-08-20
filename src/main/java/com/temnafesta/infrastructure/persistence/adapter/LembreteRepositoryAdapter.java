package com.temnafesta.infrastructure.persistence.adapter;

import com.temnafesta.domain.model.Lembrete;
import com.temnafesta.domain.ports.repository.LembreteRepositoryPort;
import com.temnafesta.infrastructure.persistence.mapper.GeralPersistenceMapper;
import com.temnafesta.infrastructure.persistence.repository.SpringDataLembreteRepository;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class LembreteRepositoryAdapter implements LembreteRepositoryPort {

    private final SpringDataLembreteRepository repository;
    private final GeralPersistenceMapper mapper;

    public LembreteRepositoryAdapter(SpringDataLembreteRepository repository, GeralPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Lembrete salvar(Lembrete lembrete) {
        var entity = mapper.toEntity(lembrete);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public List<Lembrete> buscarPorUsuarioId(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId).stream()
                .map(mapper::toDomain)
                .toList();
    }
}