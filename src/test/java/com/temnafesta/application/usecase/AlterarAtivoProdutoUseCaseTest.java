package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.AlterarAtivoProdutoCommand;
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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlterarAtivoProdutoUseCaseTest {

    @Mock
    private ProdutoRepositoryPort produtoRepositoryPort;

    @InjectMocks
    private AlterarAtivoProdutoUseCase alterarAtivoProdutoUseCase;

    @Test
    void deveDesativarProduto() {
        Produto produto = criarProduto(true, false);
        AlterarAtivoProdutoCommand command = new AlterarAtivoProdutoCommand(1L, false);
        when(produtoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(produto));
        when(produtoRepositoryPort.salvar(produto)).thenReturn(produto);

        Produto resultado = alterarAtivoProdutoUseCase.executar(command);

        assertFalse(resultado.isAtivo());
        verify(produtoRepositoryPort).salvar(produto);
    }

    @Test
    void deveReativarProdutoInativo() {
        Produto produto = criarProduto(false, false);
        AlterarAtivoProdutoCommand command = new AlterarAtivoProdutoCommand(1L, true);
        when(produtoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(produto));
        when(produtoRepositoryPort.salvar(produto)).thenReturn(produto);

        Produto resultado = alterarAtivoProdutoUseCase.executar(command);

        assertTrue(resultado.isAtivo());
        verify(produtoRepositoryPort).salvar(produto);
    }

    @Test
    void deveRetornarSucessoSemSalvarQuandoStatusJaForODesejado() {
        Produto produto = criarProduto(true, false);
        AlterarAtivoProdutoCommand command = new AlterarAtivoProdutoCommand(1L, true);
        when(produtoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(produto));

        Produto resultado = alterarAtivoProdutoUseCase.executar(command);

        assertSame(produto, resultado);
        verify(produtoRepositoryPort, never()).salvar(any(Produto.class));
    }

    @Test
    void deveRetornarNaoEncontradoQuandoProdutoNaoExistir() {
        AlterarAtivoProdutoCommand command = new AlterarAtivoProdutoCommand(99L, true);
        when(produtoRepositoryPort.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> alterarAtivoProdutoUseCase.executar(command));
        verify(produtoRepositoryPort, never()).salvar(any(Produto.class));
    }

    @Test
    void deveTratarProdutoDeletadoComoNaoEncontrado() {
        Produto produtoDeletado = criarProduto(false, true);
        AlterarAtivoProdutoCommand command = new AlterarAtivoProdutoCommand(1L, true);
        when(produtoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(produtoDeletado));

        assertThrows(RecursoNaoEncontradoException.class,
                () -> alterarAtivoProdutoUseCase.executar(command));
        verify(produtoRepositoryPort, never()).salvar(any(Produto.class));
    }

    private Produto criarProduto(boolean ativo, boolean deletado) {
        return new Produto(
                1L,
                "Bolo de Chocolate",
                null,
                new BigDecimal("49.90"),
                ativo,
                deletado);
    }
}