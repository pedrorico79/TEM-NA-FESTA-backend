package com.temnafesta.presentation.mapper;

import com.temnafesta.application.dto.AtualizarPedidoCommand;
import com.temnafesta.application.dto.CriarPedidoCommand;
import com.temnafesta.application.dto.relatorio.PedidosPorSemanaOutput;
import com.temnafesta.domain.model.HistoricoStatusPedido;
import com.temnafesta.domain.model.ItemPedido;
import com.temnafesta.domain.model.Pagamento;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.presentation.dto.*;
import jakarta.validation.Valid;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PedidoPresentationMapper {

    // Converte o DTO da Web para o Command do Use Case
    CriarPedidoCommand toCommand(Long usuarioId, CriarPedidoRequestDto dto);

    CriarPedidoCommand.ItemCommand toCommand(ItemPedidoRequestDto dto);

    // Converte a Entidade de Domínio para o DTO de Saída (Response)
    // A anotação @Transactional no controller garante que a sessão JPA esteja ativa
    PedidoResponseDto toResponse(Pedido domain);

    ItemPedidoResponseDto toResponse(ItemPedido domain);
    PagamentoResponseDto toResponse(Pagamento domain);

    AtualizarPedidoCommand toCommand(Long pedidoId, @Valid AtualizarPedidoRequestDto request);

    HistoricoStatusPedidoResponseDto toResponse(HistoricoStatusPedido domain);

    List<PedidosPorSemanaResponseDto> toPedidosPorSemanaResponse(List<PedidosPorSemanaOutput> outputs);
}