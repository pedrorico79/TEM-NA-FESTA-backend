package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.relatorio.KpiOutput;
import com.temnafesta.domain.ports.repository.EventoRepositoryPort;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;
import com.temnafesta.domain.ports.repository.ProdutoRepositoryPort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ListaKpisUseCase  {

    private final PedidoRepositoryPort pedidoRepositoryPort;
    private final ProdutoRepositoryPort produtoRepositoryPort;
    private final EventoRepositoryPort eventoRepositoryPort;

    public ListaKpisUseCase(PedidoRepositoryPort pedidoRepositoryPort, ProdutoRepositoryPort produtoRepositoryPort, EventoRepositoryPort eventoRepositoryPort) {
        this.pedidoRepositoryPort = pedidoRepositoryPort;
        this.produtoRepositoryPort = produtoRepositoryPort;
        this.eventoRepositoryPort = eventoRepositoryPort;
    }

    public KpiOutput execute(LocalDate de, LocalDate ate) {
        Long dias = ChronoUnit.DAYS.between(de, ate); // intervalo entre as datas
        Integer statusEntregueProducao = 4; // alterar com base no Id correspondente no bd.

        Long totalPedidos = pedidoRepositoryPort.countByDataPedidoBetween(
                de.atStartOfDay(),
                ate.atTime(23, 59, 59)
        );

        Long totalEntregues = pedidoRepositoryPort.countByStatusEPeriodo(
                statusEntregueProducao,
                de.atStartOfDay(),
                ate.atTime(23, 59, 59)
        );

        BigDecimal faturamento = pedidoRepositoryPort.somarFaturamentoNoPeriodo(
                statusEntregueProducao,
                de.atStartOfDay(),
                ate.atTime(23, 59, 59)
        );

        double taxaConclusao =  0.0;
        if (totalPedidos > 0) {
            taxaConclusao = ((double)totalEntregues / totalPedidos) * 100; // sem esse (double) o java conta errado
        }

        return new KpiOutput(
                totalPedidos,
                totalEntregues,
                taxaConclusao,
                faturamento,
                dias
        );
    }
}


