package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.CriarProdutoCommand;
import com.temnafesta.domain.model.Produto;
import com.temnafesta.domain.ports.repository.ProdutoRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CriarProdutoUseCaseTest {

    @Mock
    private ProdutoRepositoryPort produtoRepositoryPort;

    @InjectMocks
    private CriarProdutoUseCase criarProdutoUseCase;

    @Test
    void deveCriarProdutoAtivoQuandoStatusNaoForInformado() {
        CriarProdutoCommand command = new CriarProdutoCommand(
                "  Bolo de Chocolate  ",
                "Bolo com cobertura de ganache",
                new BigDecimal("49.90"),
                null);
        when(produtoRepositoryPort.salvar(any(Produto.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Produto resultado = criarProdutoUseCase.executar(command);

        ArgumentCaptor<Produto> captor = ArgumentCaptor.forClass(Produto.class);
        verify(produtoRepositoryPort).salvar(captor.capture());
        assertEquals("Bolo de Chocolate", captor.getValue().getNome());
        assertTrue(resultado.isAtivo());
        assertFalse(resultado.isDeletado());
    }

    @Test
    void deveRespeitarStatusInativoInformado() {
        CriarProdutoCommand command = new CriarProdutoCommand(
                "Bolo de Chocolate",
                null,
                new BigDecimal("49.90"),
                false);
        when(produtoRepositoryPort.salvar(any(Produto.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Produto resultado = criarProdutoUseCase.executar(command);

        assertFalse(resultado.isAtivo());
        assertFalse(resultado.isDeletado());
    }
}