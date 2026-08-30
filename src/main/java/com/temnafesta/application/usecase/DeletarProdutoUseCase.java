package com.temnafesta.application.usecase;

import com.temnafesta.application.exception.RecursoNaoEncontradoException;
import com.temnafesta.domain.model.Produto;
import com.temnafesta.domain.ports.repository.ProdutoRepositoryPort;

public class DeletarProdutoUseCase {

    private final ProdutoRepositoryPort produtoRepositoryPort;

    public DeletarProdutoUseCase(ProdutoRepositoryPort produtoRepositoryPort) {
        this.produtoRepositoryPort = produtoRepositoryPort;
    }

    public void executar(Long id) {
        Produto produto = produtoRepositoryPort.buscarPorId(id)
                .filter(produtoEncontrado -> !produtoEncontrado.isDeletado())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Produto não encontrado com o ID: " + id));

        produto.deletar();
        produtoRepositoryPort.salvar(produto);
    }
}