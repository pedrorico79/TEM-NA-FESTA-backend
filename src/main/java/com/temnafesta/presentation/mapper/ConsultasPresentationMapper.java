package com.temnafesta.presentation.mapper;

import com.temnafesta.application.dto.CriarLembreteCommand;
import com.temnafesta.domain.model.Evento;
import com.temnafesta.domain.model.Lembrete;
import com.temnafesta.domain.model.MetodoPagamento;
import com.temnafesta.presentation.dto.EventoResponseDto;
import com.temnafesta.presentation.dto.LembreteRequestDto;
import com.temnafesta.presentation.dto.LembreteResponseDto;
import com.temnafesta.presentation.dto.MetodoPagamentoResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConsultasPresentationMapper {

    // Lembrete
    CriarLembreteCommand toCommand(LembreteRequestDto dto, Long usuarioId);
    LembreteResponseDto toResponse(Lembrete domain);

    // Eventos e Métodos de Pagamento (Apenas Response, pois são Query Use Cases)
    EventoResponseDto toResponse(Evento domain);
    MetodoPagamentoResponseDto toResponse(MetodoPagamento domain);
}