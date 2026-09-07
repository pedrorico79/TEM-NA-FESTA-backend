package com.temnafesta.application.usecase;

import com.temnafesta.application.exception.RecursoNaoEncontradoException;
import com.temnafesta.domain.model.Produto;
import com.temnafesta.domain.ports.repository.ProdutoRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeletarProdutoUseCaseTest {

    @Mock
    private ProdutoRepositoryPort produtoRepositoryPort;

    @InjectMocks
    private DeletarProdutoUseCase deletarProdutoUseCase;

    @Test
    void deveExcluirLogicamenteEInativarProduto() {
        Produto produto = criarProduto(false);
        when(produtoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(produto));

        deletarProdutoUseCase.executar(1L);

        assertTrue(produto.isDeletado());
        assertFalse(produto.isAtivo());
        verify(produtoRepositoryPort).salvar(produto);
    }

    @Test
    void deveRetornarNaoEncontradoQuandoProdutoNaoExistir() {
        when(produtoRepositoryPort.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> deletarProdutoUseCase.executar(99L));
        verify(produtoRepositoryPort, never()).salvar(any(Produto.class));
    }

    @Test
    void deveTratarProdutoJaDeletadoComoNaoEncontrado() {
        Produto produtoDeletado = criarProduto(true);
        when(produtoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(produtoDeletado));

        assertThrows(RecursoNaoEncontradoException.class,
                () -> deletarProdutoUseCase.executar(1L));
        verify(produtoRepositoryPort, never()).salvar(any(Produto.class));
    }

    private Produto criarProduto(boolean deletado) {
        return new Produto(
                1L,
                "Bolo de Chocolate",
                "Bolo com cobertura de ganache",
                new BigDecimal("49.90"),
                true,
                deletado);
    }
}
