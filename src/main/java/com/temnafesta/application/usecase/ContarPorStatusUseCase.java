package com.temnafesta.application.usecase;

import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;
import com.temnafesta.domain.vo.StatusProducaoEnum;

import java.util.EnumMap;
import java.util.Map;

public class ContarPorStatusUseCase {

    private final PedidoRepositoryPort pedidoRepository;

    public ContarPorStatusUseCase(PedidoRepositoryPort pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public Map<StatusProducaoEnum, Long> executar() {
        Map<StatusProducaoEnum, Long> contagem = new EnumMap<>(StatusProducaoEnum.class);

        for (StatusProducaoEnum status : StatusProducaoEnum.values()) {
            contagem.put(status, pedidoRepository.contarPorStatus(status));
        }

        return contagem;
    }
}