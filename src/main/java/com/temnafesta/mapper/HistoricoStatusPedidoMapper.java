package com.temnafesta.mapper;

import com.temnafesta.dto.historicosStatusPedido.HistoricoStatusPedidoResponseDto;
import com.temnafesta.model.HistoricoStatusPedido;

public class HistoricoStatusPedidoMapper {

    public static HistoricoStatusPedidoResponseDto toResponseDto(
            HistoricoStatusPedido historico
    ) {

        HistoricoStatusPedidoResponseDto dto =
                new HistoricoStatusPedidoResponseDto();

        dto.setId(historico.getId());

        dto.setPedidoId(
                historico.getPedido().getId()
        );

        dto.setStatusId(
                historico.getStatusProducao().getId()
        );

        dto.setStatusNome(
                historico.getStatusProducao().getNome()
        );

        dto.setUsuarioId(
                historico.getUsuario().getId()
        );

        dto.setUsuarioNome(
                historico.getUsuario().getNome()
        );

        dto.setDataAlteracao(
                historico.getDataAlteracao()
        );

        dto.setObservacao(
                historico.getObservacao()
        );

        return dto;
    }
}
