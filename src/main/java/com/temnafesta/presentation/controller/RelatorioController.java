package com.temnafesta.presentation.controller;

import com.temnafesta.application.dto.relatorio.EventoComparativoOutput;
import com.temnafesta.application.dto.relatorio.PedidosPeriodoOutput;
import com.temnafesta.application.dto.relatorio.PedidosPorSemanaOutput;
import com.temnafesta.application.dto.relatorio.ProdutosMaisVendidosOutput;
import com.temnafesta.application.usecase.*;
import com.temnafesta.presentation.dto.*;
import com.temnafesta.presentation.mapper.RelatorioPresentationMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/relatorios")
@AllArgsConstructor
public class RelatorioController {

    private final ListaKpisUseCase listaKpisUseCase;
    private final ListaPedidosPorSemanaUseCase listaPedidosPorSemanaUseCase;
    private final ListaPedidosPeriodoUseCase listaPedidosPeriodoUseCase;
    private final ListaProdutosMaisVendidosUseCase listaProdutosMaisVendidosUseCase;
    private final ListaComparativoEventosUseCase listaComparativoEventosUseCase;
    private final ListaRelatorioDinamicoUseCase listaRelatorioDinamicoUseCase;
    private final RelatorioPresentationMapper mapper;


    @GetMapping("/kpis")
    public ResponseEntity<KpiResponseDto> obterKpis(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate de,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ate
    ) {
        return ResponseEntity.ok(mapper.toResponse(listaKpisUseCase.execute(de, ate)));
    }

    @GetMapping("/pedidos-por-semana")
    public ResponseEntity<List<PedidosPorSemanaResponseDto>> obterPedidosAgrupadosPorSemana(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate de,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ate
    ) {
    if (de == null || ate == null || de.isAfter(ate)) {
        return ResponseEntity.badRequest().build();
    }

    List<PedidosPorSemanaOutput> outputs = listaPedidosPorSemanaUseCase.execute(de, ate);
    List<PedidosPorSemanaResponseDto> dadosGrafico = mapper.toResponse(outputs);

    if (dadosGrafico.isEmpty()) return ResponseEntity.noContent().build();
    return ResponseEntity.ok(dadosGrafico);
}

    @GetMapping("/pedidos-periodo")
    public ResponseEntity<Page<PedidosPeriodoResponseDto>> obterPedidosPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate de,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ate,
            Pageable pageable
    ) {
        if (de == null || ate == null || de.isAfter(ate)) {
            return ResponseEntity.badRequest().build();
        }

        Page<PedidosPeriodoOutput> outputs = listaPedidosPeriodoUseCase.execute(de, ate, pageable);
        Page<PedidosPeriodoResponseDto> response = outputs.map(o -> new PedidosPeriodoResponseDto(
                o.id(),
                o.dataPedido(),
                o.clienteNome(),
                o.eventoNome(),
                o.valorTotal(),
                o.valorPago(),
                o.statusNome())
        );

        if (response.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/produtos-mais-vendidos")
    public ResponseEntity<Page<ProdutoMaisVendidosResponseDto>> obterProdutosMaisVendidos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate de,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ate,
            Pageable pageable
    ) {
        if (de == null || ate == null || de.isAfter(ate)) {
            return ResponseEntity.badRequest().build();
        }

        Page<ProdutosMaisVendidosOutput> outputs =
                listaProdutosMaisVendidosUseCase.execute(de, ate, pageable);

        Page<ProdutoMaisVendidosResponseDto> response = outputs.map(o -> new ProdutoMaisVendidosResponseDto(
                o.item(),
                o.qtdeVendida(),
                o.faturamento(),
                o.porcentagemDoTotal()
        ));

        if (response.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(response);
    }


        @GetMapping("/comparativo-eventos")
    public ResponseEntity<List<EventoComparativoResponseDto>> obterComparativoEventos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate de,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ate
    ) {
        if (de == null || ate == null || de.isAfter(ate)) {
            return ResponseEntity.badRequest().build();
        }

        List<EventoComparativoOutput> outputs = listaComparativoEventosUseCase.execute(de, ate);

        List<EventoComparativoResponseDto> response = outputs.stream()
                .map(o -> new EventoComparativoResponseDto(
                        o.evento(),
                        o.pedidosTotais(),
                        o.vendasObtidas(),
                        o.faturamento(),
                        o.ticketMedio()
                ))
                .toList();

        if (response.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/busca-dinamica")
    public ResponseEntity<List<PedidosPeriodoResponseDto>> obterRelatorioDinamico(
            @RequestParam(required = false) Integer eventoId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim
    ) {
        if (dataInicio != null && dataFim != null && dataInicio.isAfter(dataFim)) {
            return ResponseEntity.badRequest().build();
        }

        List<PedidosPeriodoOutput> outputs = listaRelatorioDinamicoUseCase.execute(eventoId, dataInicio, dataFim);

        List<PedidosPeriodoResponseDto> response = outputs.stream()
                .map(o -> new PedidosPeriodoResponseDto(
                        o.id(),
                        o.dataPedido(),
                        o.clienteNome(),
                        o.eventoNome(),
                        o.valorTotal(),
                        o.valorPago(),
                        o.statusNome()
                ))
                .toList();

        if (response.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(response);
    }

}
