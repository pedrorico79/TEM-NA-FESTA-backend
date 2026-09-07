package com.temnafesta.application.usecase;

import com.temnafesta.domain.model.Produto;
import com.temnafesta.domain.ports.repository.ProdutoRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarProdutosUseCaseTest {

    @Mock
    private ProdutoRepositoryPort produtoRepositoryPort;

    @InjectMocks
    private ListarProdutosUseCase listarProdutosUseCase;

    @Test
    void deveListarTodosQuandoNomeNaoForInformado() {
        List<Produto> produtos = List.of();
        when(produtoRepositoryPort.listarPorNome("")).thenReturn(produtos);

        List<Produto> resultado = listarProdutosUseCase.executar(null);

        assertSame(produtos, resultado);
        verify(produtoRepositoryPort).listarPorNome("");
    }

    @Test
    void deveRemoverEspacosDoFiltroPorNome() {
        List<Produto> produtos = List.of();
        when(produtoRepositoryPort.listarPorNome("bolo")).thenReturn(produtos);

        List<Produto> resultado = listarProdutosUseCase.executar("  bolo  ");

        assertSame(produtos, resultado);
        verify(produtoRepositoryPort).listarPorNome("bolo");
    }
}
