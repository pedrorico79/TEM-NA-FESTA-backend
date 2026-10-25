package com.temnafesta.application.usecase;

import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.ports.out.repository.PedidoRepositoryPort;

import java.time.LocalDateTime;
import java.util.List;
import java.time.Clock;

public class ListarProximasRetiradasUseCase {

    private final PedidoRepositoryPort pedidoRepository;
    private final Clock relogioNegocio;

    public ListarProximasRetiradasUseCase(PedidoRepositoryPort pedidoRepository) {
        this(pedidoRepository, com.temnafesta.domain.time.RelogioNegocio.sistema());
    }

    public ListarProximasRetiradasUseCase(
            PedidoRepositoryPort pedidoRepository,
            Clock relogioNegocio) {
        this.pedidoRepository = pedidoRepository;
        this.relogioNegocio = relogioNegocio != null
                ? relogioNegocio : com.temnafesta.domain.time.RelogioNegocio.sistema();
    }

    public List<Pedido> executar(int dias) {

        if (dias < 0) {
            throw new IllegalArgumentException(
                    "A quantidade de dias não pode ser negativa."
            );
        }

        LocalDateTime limite = LocalDateTime.now(relogioNegocio)
                .plusDays(dias)
                .toLocalDate()
                .atTime(23, 59, 59);

        return pedidoRepository.listarProximasRetiradas(limite);
    }
}
