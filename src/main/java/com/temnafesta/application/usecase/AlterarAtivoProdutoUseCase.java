package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.AlterarAtivoProdutoCommand;
import com.temnafesta.application.exception.RecursoNaoEncontradoException;
import com.temnafesta.domain.model.Produto;
import com.temnafesta.domain.ports.repository.ProdutoRepositoryPort;

public class AlterarAtivoProdutoUseCase {

    private final ProdutoRepositoryPort produtoRepositoryPort;

    public AlterarAtivoProdutoUseCase(ProdutoRepositoryPort produtoRepositoryPort) {
        this.produtoRepositoryPort = produtoRepositoryPort;
    }

    public Produto executar(AlterarAtivoProdutoCommand command) {
        Produto produto = produtoRepositoryPort.buscarPorId(command.id())
                .filter(produtoEncontrado -> !produtoEncontrado.isDeletado())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Produto não encontrado com o ID: " + command.id()));

        if (produto.isAtivo() == command.ativo()) {
            return produto;
        }

        produto.alterarAtivo(command.ativo());
        return produtoRepositoryPort.salvar(produto);
    }
}