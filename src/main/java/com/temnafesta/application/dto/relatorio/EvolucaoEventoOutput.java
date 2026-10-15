package com.temnafesta.application.dto.relatorio;

import java.util.List;

public record EvolucaoEventoOutput(
        String agrupamento,
        List<EvolucaoEventoPeriodoOutput> dados
) {}
