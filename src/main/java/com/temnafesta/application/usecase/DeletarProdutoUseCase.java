package com.temnafesta.application.usecase;

import com.temnafesta.domain.exception.NaoEncontradoException;
import com.temnafesta.domain.model.Produto;
import com.temnafesta.domain.ports.out.repository.ProdutoRepositoryPort;

public class DeletarProdutoUseCase {

    private final ProdutoRepositoryPort produtoRepositoryPort;

    public DeletarProdutoUseCase(ProdutoRepositoryPort produtoRepositoryPort) {
        this.produtoRepositoryPort = produtoRepositoryPort;
    }

    public void executar(Long id) {
        Produto produto = produtoRepositoryPort.buscarPorId(id)
                .filter(produtoEncontrado -> !produtoEncontrado.isDeletado())
                .orElseThrow(() -> new NaoEncontradoException(
                        "Produto não encontrado com o ID: " + id));

        produto.deletar();
        produtoRepositoryPort.salvar(produto);
    }
}