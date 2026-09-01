package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.AtualizarProdutoCommand;
import com.temnafesta.application.exception.RecursoNaoEncontradoException;
import com.temnafesta.domain.model.Produto;
import com.temnafesta.domain.ports.repository.ProdutoRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtualizarProdutoUseCaseTest {

    @Mock
    private ProdutoRepositoryPort produtoRepositoryPort;

    @InjectMocks
    private AtualizarProdutoUseCase atualizarProdutoUseCase;

    @Test
    void deveAtualizarTodosOsCamposEPreservarOId() {
        Produto existente = new Produto(
                1L,
                "Bolo Antigo",
                "Descrição antiga",
                new BigDecimal("39.90"),
                false,
                false);
        AtualizarProdutoCommand command = new AtualizarProdutoCommand(
                1L,
                "  Bolo Atualizado  ",
                null,
                new BigDecimal("59.90"),
                true);
        when(produtoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(existente));
        when(produtoRepositoryPort.salvar(any(Produto.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Produto resultado = atualizarProdutoUseCase.executar(command);

        ArgumentCaptor<Produto> captor = ArgumentCaptor.forClass(Produto.class);
        verify(produtoRepositoryPort).salvar(captor.capture());
        assertEquals(1L, resultado.getId());
        assertEquals("Bolo Atualizado", captor.getValue().getNome());
        assertNull(captor.getValue().getDescricao());
        assertEquals(new BigDecimal("59.90"), captor.getValue().getPrecoVenda());
        assertTrue(captor.getValue().isAtivo());
        assertFalse(captor.getValue().isDeletado());
    }

    @Test
    void deveRetornarNaoEncontradoQuandoProdutoNaoExistir() {
        AtualizarProdutoCommand command = criarCommand(99L);
        when(produtoRepositoryPort.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> atualizarProdutoUseCase.executar(command));
        verify(produtoRepositoryPort, never()).salvar(any(Produto.class));
    }

    @Test
    void deveTratarProdutoDeletadoComoNaoEncontrado() {
        Produto deletado = new Produto(
                1L,
                "Bolo Deletado",
                null,
                new BigDecimal("39.90"),
                false,
                true);
        AtualizarProdutoCommand command = criarCommand(1L);
        when(produtoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(deletado));

        assertThrows(RecursoNaoEncontradoException.class,
                () -> atualizarProdutoUseCase.executar(command));
        verify(produtoRepositoryPort, never()).salvar(any(Produto.class));
    }

    private AtualizarProdutoCommand criarCommand(Long id) {
        return new AtualizarProdutoCommand(
                id,
                "Bolo Atualizado",
                null,
                new BigDecimal("59.90"),
                true);
    }
}
