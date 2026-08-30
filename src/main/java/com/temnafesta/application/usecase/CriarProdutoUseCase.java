package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.CriarProdutoCommand;
import com.temnafesta.domain.model.Produto;
import com.temnafesta.domain.ports.repository.ProdutoRepositoryPort;

public class CriarProdutoUseCase {

    private final ProdutoRepositoryPort produtoRepositoryPort;

    public CriarProdutoUseCase(ProdutoRepositoryPort produtoRepositoryPort) {
        this.produtoRepositoryPort = produtoRepositoryPort;
    }

    public Produto executar(CriarProdutoCommand command) {
        String nome = command.nome() == null ? null : command.nome().trim();

        Produto produto = new Produto(
                null,
                nome,
                command.descricao(),
                command.precoVenda(),
                command.ativo(),
                false
        );

        return produtoRepositoryPort.salvar(produto);
    }
}
