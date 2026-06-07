package com.temnafesta.controller;

import com.temnafesta.dto.relatorio.kpi.KpiResponseDto;
import com.temnafesta.dto.relatorio.pedidosporsemana.PedidosPorSemanaResponseDto;
import com.temnafesta.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
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
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @Operation(summary = "Obtém os dados consumidos pela KPIs do relatório")
    @GetMapping("/kpis")
    public ResponseEntity<KpiResponseDto> obterKpis(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate de,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ate
    ) {
        return ResponseEntity.ok(relatorioService.obterKpis(de, ate));
    }


    @Operation(summary = "Obtém a quantidade de pedidos agrupados por semana para o gráfico")
    @GetMapping("/pedidos-por-semana")
    public ResponseEntity<List<PedidosPorSemanaResponseDto>> obterPedidosAgrupadosPorSemana(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate de,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ate
    ){
        List<PedidosPorSemanaResponseDto> dadosGrafico = relatorioService
                .retornarPedidosPorSemana(de, ate);

        if (dadosGrafico.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(dadosGrafico);

    }
}
