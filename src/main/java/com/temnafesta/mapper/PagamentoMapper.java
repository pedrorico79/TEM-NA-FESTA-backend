package com.temnafesta.mapper;

import com.temnafesta.dto.pagamento.PagamentoRequestDto;
import com.temnafesta.dto.pagamento.PagamentoResponseDto;
import com.temnafesta.model.Pagamento;

import java.util.List;

public class PagamentoMapper {

    private PagamentoMapper() {}

    public static Pagamento toEntity(PagamentoRequestDto dto) {
        Pagamento pagamento = new Pagamento();
        pagamento.setValor(dto.getValor());
        pagamento.setMetodo(dto.getMetodo());

        return pagamento;
    }

    public static PagamentoResponseDto toResponseDto(Pagamento pagamento) {
        PagamentoResponseDto.PedidoDto pedidoDto = new PagamentoResponseDto.PedidoDto();
        pedidoDto.setId(pagamento.getPedido().getId());
        pedidoDto.setValorTotal(pagamento.getPedido().getValorTotal());
        pedidoDto.setStatusProducao(pagamento.getPedido().getStatusProducao());

        PagamentoResponseDto.UsuarioDto usuarioDto = new PagamentoResponseDto.UsuarioDto();
        usuarioDto.setId(pagamento.getUsuario().getId());
        usuarioDto.setNome(pagamento.getUsuario().getNome());

        PagamentoResponseDto dto = new PagamentoResponseDto();
        dto.setId(pagamento.getId());
        dto.setValor(pagamento.getValor());
        dto.setDataPagamento(pagamento.getDataPagamento());
        dto.setMetodo(pagamento.getMetodo());
        dto.setPedido(pedidoDto);
        dto.setUsuario(usuarioDto);

        return dto;
    }

    public static List<PagamentoResponseDto> toResponseDtoList(List<Pagamento> pagamentos) {
        return pagamentos.stream()
                .map(PagamentoMapper::toResponseDto)
                .toList();
    }
}