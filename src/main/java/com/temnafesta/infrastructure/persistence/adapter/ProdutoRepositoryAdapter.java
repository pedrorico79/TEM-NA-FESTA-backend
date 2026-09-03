package com.temnafesta.infrastructure.persistence.adapter;

import com.temnafesta.application.dto.relatorio.ProdutosMaisVendidosOutput;
import com.temnafesta.domain.model.Produto;
import com.temnafesta.domain.ports.repository.ProdutoRepositoryPort;
import com.temnafesta.infrastructure.persistence.mapper.ProdutoPersistenceMapper;
import com.temnafesta.infrastructure.persistence.repository.SpringDataProdutoRepository;
import com.temnafesta.infrastructure.projection.MaisVendidosProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<ProdutosMaisVendidosOutput> buscarProdutosMaisVendidosPaginado(LocalDateTime de, LocalDateTime ate, Pageable pageable) {
        Page<MaisVendidosProjection> maisVendidosProjections =
                repository.buscarProdutosMaisVendidosPaginado(de, ate, pageable);

        return maisVendidosProjections.map(p -> new ProdutosMaisVendidosOutput(
                p.getItem(),
                p.getQtdeVendida(),
                p.getFaturamento(),
                p.getPorcentagemDoTotal()
        ));
    }
}