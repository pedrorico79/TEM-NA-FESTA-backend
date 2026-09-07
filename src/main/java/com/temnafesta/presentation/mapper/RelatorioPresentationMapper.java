package com.temnafesta.presentation.mapper;

import com.temnafesta.application.dto.relatorio.KpiOutput;
import com.temnafesta.application.dto.relatorio.PedidosPeriodoOutput;
import com.temnafesta.application.dto.relatorio.PedidosPorSemanaOutput;
import com.temnafesta.presentation.dto.KpiResponseDto;
import com.temnafesta.presentation.dto.PedidosPeriodoResponseDto;
import com.temnafesta.presentation.dto.PedidosPorSemanaResponseDto;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RelatorioPresentationMapper {


    KpiResponseDto toResponse(KpiOutput output);
    List<PedidosPorSemanaResponseDto> toResponse(List<PedidosPorSemanaOutput> lista);}
