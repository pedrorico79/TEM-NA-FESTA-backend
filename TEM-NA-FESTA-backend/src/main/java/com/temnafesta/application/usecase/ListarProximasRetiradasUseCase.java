package com.temnafesta.application.usecase;

import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;

import java.time.LocalDateTime;
import java.util.List;

public class ListarProximasRetiradasUseCase {

    private final PedidoRepositoryPort pedidoRepository;

    public ListarProximasRetiradasUseCase(
            PedidoRepositoryPort pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<Pedido> executar(int dias) {

        if (dias < 0) {
            throw new IllegalArgumentException(
                    "A quantidade de dias não pode ser negativa."
            );
        }

        LocalDateTime limite = LocalDateTime.now()
                .plusDays(dias)
                .toLocalDate()
                .atTime(23, 59, 59);

        return pedidoRepository.listarProximasRetiradas(limite);
    }
}