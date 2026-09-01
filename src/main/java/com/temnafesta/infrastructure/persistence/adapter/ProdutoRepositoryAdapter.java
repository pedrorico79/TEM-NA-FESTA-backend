package com.temnafesta.infrastructure.persistence.adapter;

import com.temnafesta.domain.model.Produto;
import com.temnafesta.domain.ports.repository.ProdutoRepositoryPort;
import com.temnafesta.infrastructure.persistence.mapper.ProdutoPersistenceMapper;
import com.temnafesta.infrastructure.persistence.repository.SpringDataProdutoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProdutoRepositoryAdapter implements ProdutoRepositoryPort {

    private final SpringDataProdutoRepository repository;
    private final ProdutoPersistenceMapper mapper;

    public ProdutoRepositoryAdapter(SpringDataProdutoRepository repository, ProdutoPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Produto> buscarPorId(Long id) {
        return repository.findByIdAndDeletadoFalse(id).map(mapper::toDomain);
    }

    @Override
    public List<Produto> listarPorNome(String nome) {
        return repository.findByDeletadoFalseAndNomeContainingIgnoreCaseOrderByAtivoDesc(nome).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Produto salvar(Produto produto) {
        return mapper.toDomain(repository.save(mapper.toEntity(produto)));
    }
}