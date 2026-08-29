package com.temnafesta.application.usecase;

import com.temnafesta.domain.model.Produto;
import com.temnafesta.domain.ports.repository.ProdutoRepositoryPort;

import java.util.List;

public class ListarProdutosUseCase {

    private final ProdutoRepositoryPort produtoRepositoryPort;

    public ListarProdutosUseCase(ProdutoRepositoryPort produtoRepositoryPort) {
        this.produtoRepositoryPort = produtoRepositoryPort;
    }

    public List<Produto> executar(String nome) {
        String filtro = nome == null ? "" : nome.trim();
        return produtoRepositoryPort.listarPorNome(filtro);
    }
}
