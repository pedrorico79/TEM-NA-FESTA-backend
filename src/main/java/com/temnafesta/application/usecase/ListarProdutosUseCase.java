package com.temnafesta.application.usecase;

import com.temnafesta.domain.model.Produto;
import com.temnafesta.domain.ports.repository.ProdutoRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class ListarProdutosUseCase {

    private final ProdutoRepositoryPort produtoRepositoryPort;

    public ListarProdutosUseCase(ProdutoRepositoryPort produtoRepositoryPort) {
        this.produtoRepositoryPort = produtoRepositoryPort;
    }

    public List<Produto> executar(String nome, boolean ativos) {
        String filtro = nome == null ? "" : nome.trim();
        if (!ativos) return produtoRepositoryPort.listarPorNome(filtro);
        return produtoRepositoryPort.listarPorNome(filtro, ativos);
    }

    public List<Produto> executar(String nome) {
        return executar(nome, false);
    }

    @Transactional(readOnly = true)
    public Page<Produto> executar(String nome, boolean ativos, Pageable pageable) {
        String filtro = nome == null ? "" : nome.trim();
        return produtoRepositoryPort.listarPorNome(filtro, ativos, pageable);
    }
}
