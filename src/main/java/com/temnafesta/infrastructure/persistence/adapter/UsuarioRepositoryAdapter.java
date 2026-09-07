package com.temnafesta.infrastructure.persistence.adapter;

import com.temnafesta.domain.model.Usuario;
import com.temnafesta.domain.ports.repository.UsuarioRepositoryPort;
import com.temnafesta.infrastructure.persistence.entity.UsuarioJpaEntity;
import com.temnafesta.infrastructure.persistence.mapper.UsuarioPersistenceMapper;
import com.temnafesta.infrastructure.persistence.repository.SpringDataUsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final SpringDataUsuarioRepository repository;
    private final UsuarioPersistenceMapper mapper;

    public UsuarioRepositoryAdapter(SpringDataUsuarioRepository repository, UsuarioPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        UsuarioJpaEntity entity = mapper.toEntity(usuario);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return repository.findByIdAndDeletadoFalse(id).map(mapper::toDomain);
    }

    @Override
    public Optional<List<Usuario>> listarTodos() {
        return Optional.of(repository.findAll().stream()
                .filter(entity -> !entity.isDeletado())
                .map(mapper::toDomain)
                .toList());
    }

    @Override
    public Page<Usuario> listarPorNomePaginado(String nome, Pageable pageable) {
        String filtro = nome == null ? "" : nome.trim();
        if (filtro.isBlank()) {
            return repository.findByDeletadoFalse(pageable).map(mapper::toDomain);
        }
        return repository.findByDeletadoFalseAndNomeContainingIgnoreCase(filtro, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Usuario atualizar(Usuario usuario) {
        UsuarioJpaEntity entity = mapper.toEntity(usuario);
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
    public Optional<Usuario> buscarPorEmail(String email) {
        return repository.findByEmail(email).map(mapper::toDomain);
    }
}