package com.temnafesta.domain.ports.repository;

import com.temnafesta.application.dto.relatorio.ProdutosMaisVendidosOutput;
import com.temnafesta.domain.model.Produto;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ProdutoRepositoryPort {

    Optional<Produto> buscarPorId(Long id);

    List<Produto> listarPorNome(String nome);
    default List<Produto> listarPorNome(String nome, boolean ativos) {
        List<Produto> produtos = listarPorNome(nome);
        return ativos ? produtos.stream().filter(Produto::isAtivo).toList() : produtos;
    }

    Produto salvar(Produto produto);

    Page<ProdutosMaisVendidosOutput> buscarProdutosMaisVendidosPaginado(
            LocalDateTime de,
            LocalDateTime ate,
            Long eventoId,
            Pageable pageable
    );
}
