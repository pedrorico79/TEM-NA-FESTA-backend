package com.temnafesta.application.usecase;

import com.temnafesta.domain.vo.StatusProducaoEnum;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ListarStatusProducaoUseCaseTest {

    private final ListarStatusProducaoUseCase useCase = new ListarStatusProducaoUseCase();

    @Test
    void deveListarTodosOsStatusDoEnum() {
        List<StatusProducaoEnum> resultado = useCase.executar();

        assertEquals(StatusProducaoEnum.values().length, resultado.size());
        assertTrue(resultado.contains(StatusProducaoEnum.RASCUNHO));
        assertTrue(resultado.contains(StatusProducaoEnum.CANCELADO));
    }
}
