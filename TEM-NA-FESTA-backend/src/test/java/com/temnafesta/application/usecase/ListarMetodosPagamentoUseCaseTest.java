package com.temnafesta.application.usecase;

import com.temnafesta.domain.model.MetodoPagamento;
import com.temnafesta.domain.ports.repository.MetodoPagamentoRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarMetodosPagamentoUseCaseTest {

    @Mock
    private MetodoPagamentoRepositoryPort metodoPagamentoRepositoryPort;

    @InjectMocks
    private ListarMetodosPagamentoUseCase useCase;

    @Test
    void deveListarTodos() {
        when(metodoPagamentoRepositoryPort.listarTodos())
                .thenReturn(List.of(new MetodoPagamento(1L, "Pix")));

        assertEquals(1, useCase.executar().size());
    }

    @Test
    void deveRetornarVazioSemErro() {
        when(metodoPagamentoRepositoryPort.listarTodos()).thenReturn(List.of());

        assertTrue(useCase.executar().isEmpty());
    }
}
