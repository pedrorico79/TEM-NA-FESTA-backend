package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.AtualizarProdutoCommand;
import com.temnafesta.application.exception.RecursoNaoEncontradoException;
import com.temnafesta.domain.model.Produto;
import com.temnafesta.domain.ports.repository.ProdutoRepositoryPort;

public class AtualizarProdutoUseCase {

    private final ProdutoRepositoryPort produtoRepositoryPort;

    public AtualizarProdutoUseCase(ProdutoRepositoryPort produtoRepositoryPort) {
        this.produtoRepositoryPort = produtoRepositoryPort;
    }

    public Produto executar(AtualizarProdutoCommand command) {
        Produto produtoExistente = produtoRepositoryPort.buscarPorId(command.id())
                .filter(produto -> !produto.isDeletado())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Produto não encontrado com o ID: " + command.id()));

        String nome = command.nome() == null ? null : command.nome().trim();
        Produto produtoAtualizado = new Produto(
                produtoExistente.getId(),
                nome,
                command.descricao(),
                command.precoVenda(),
                command.ativo(),
                produtoExistente.isDeletado()
        );

        return produtoRepositoryPort.salvar(produtoAtualizado);
    }
}
