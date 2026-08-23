package com.temnafesta.infrastructure.persistence.adapter;

import com.temnafesta.domain.model.Lembrete;
import com.temnafesta.domain.ports.repository.LembreteRepositoryPort;
import com.temnafesta.infrastructure.persistence.mapper.GeralPersistenceMapper;
import com.temnafesta.infrastructure.persistence.repository.SpringDataLembreteRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

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
    public List<Lembrete> listarPorUsuarioId(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Lembrete> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public void deletarPorId(Long id) {
        repository.deleteById(id);
    }
}