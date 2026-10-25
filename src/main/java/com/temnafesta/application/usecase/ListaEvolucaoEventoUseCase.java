package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.relatorio.EvolucaoEventoOutput;
import com.temnafesta.application.dto.relatorio.EvolucaoEventoPeriodoOutput;
import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.Evento;
import com.temnafesta.domain.ports.out.repository.EventoRepositoryPort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListaEvolucaoEventoUseCase {

    private static final DateTimeFormatter CHAVE_MES = DateTimeFormatter.ofPattern("yyyy-MM");
    private static final DateTimeFormatter ROTULO_MES = DateTimeFormatter.ofPattern("MM/yyyy");
    private static final DateTimeFormatter CHAVE_ANO = DateTimeFormatter.ofPattern("yyyy");

    private final EventoRepositoryPort eventoRepositoryPort;

    public ListaEvolucaoEventoUseCase(EventoRepositoryPort eventoRepositoryPort) {
        this.eventoRepositoryPort = eventoRepositoryPort;
    }

    public EvolucaoEventoOutput executar(Long eventoId) {
        if (eventoId == null) {
            throw new RegraDeNegocioException("ID do evento é obrigatório.");
        }

        Evento evento = eventoRepositoryPort.buscarPorId(eventoId)
                .orElseThrow(() -> new RegraDeNegocioException("Evento não encontrado com o ID: " + eventoId));

        LocalDate inicio = evento.getDataInicio();
        LocalDate fim = evento.getDataFim();
        if (inicio == null || fim == null || !fim.isAfter(inicio.plusMonths(1))) {
            return new EvolucaoEventoOutput("NENHUM", List.of());
        }

        boolean agruparPorAno = fim.isAfter(inicio.plusYears(1));
        String agrupamento = agruparPorAno ? "ANO" : "MES";
        LocalDateTime inicioPeriodo = inicio.atStartOfDay();
        LocalDateTime fimPeriodo = fim.atTime(23, 59, 59, 999_999_999);

        List<EvolucaoEventoPeriodoOutput> resultados = eventoRepositoryPort.buscarEvolucaoEvento(
                eventoId,
                inicioPeriodo,
                fimPeriodo,
                agruparPorAno
        );
        Map<String, EvolucaoEventoPeriodoOutput> porPeriodo = resultados.stream()
                .collect(Collectors.toMap(EvolucaoEventoPeriodoOutput::periodo, Function.identity(), (a, b) -> a));

        Stream<LocalDate> periodos = agruparPorAno
                ? Stream.iterate(inicio.withDayOfYear(1), data -> data.getYear() <= fim.getYear(), data -> data.plusYears(1))
                : Stream.iterate(inicio.withDayOfMonth(1), data -> !data.isAfter(fim.withDayOfMonth(1)), data -> data.plusMonths(1));

        List<EvolucaoEventoPeriodoOutput> dados = periodos.map(data -> {
            String chave = data.format(agruparPorAno ? CHAVE_ANO : CHAVE_MES);
            EvolucaoEventoPeriodoOutput agregado = porPeriodo.get(chave);
            String rotulo = data.format(agruparPorAno ? CHAVE_ANO : ROTULO_MES);
            return agregado == null
                    ? new EvolucaoEventoPeriodoOutput(rotulo, 0L, 0L, BigDecimal.ZERO, BigDecimal.ZERO)
                    : new EvolucaoEventoPeriodoOutput(
                            rotulo,
                            agregado.pedidosTotais(),
                            agregado.vendasObtidas(),
                            agregado.faturamento(),
                            agregado.ticketMedio()
                    );
        }).toList();

        return new EvolucaoEventoOutput(agrupamento, dados);
    }
}
