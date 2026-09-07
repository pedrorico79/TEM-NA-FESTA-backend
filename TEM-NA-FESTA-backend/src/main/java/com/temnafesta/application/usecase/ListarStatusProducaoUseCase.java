package com.temnafesta.application.usecase;

import com.temnafesta.domain.vo.StatusProducaoEnum;

import java.util.List;

public class ListarStatusProducaoUseCase {

    public List<StatusProducaoEnum> executar() {
        return List.of(StatusProducaoEnum.values());
    }
}
