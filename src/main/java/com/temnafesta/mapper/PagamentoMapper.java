package com.temnafesta.mapper;

import com.temnafesta.dto.pagamento.PagamentoRequestDto;
import com.temnafesta.dto.pagamento.PagamentoResponseDto;
import com.temnafesta.model.MetodoPagamento;
import com.temnafesta.model.Pagamento;
import com.temnafesta.model.StatusProducao;

import java.util.List;

public class PagamentoMapper {

    private PagamentoMapper() {}

    public static Pagamento toEntity(PagamentoRequestDto dto) {
        Pagamento pagamento = new Pagamento();
        pagamento.setValor(dto.getValor());

        return pagamento;
    }

    public static PagamentoResponseDto toResponseDto(Pagamento pagamento) {
        PagamentoResponseDto.PedidoDto pedidoDto = new PagamentoResponseDto.PedidoDto();
        pedidoDto.setId(pagamento.getPedido().getId());
        pedidoDto.setValorTotal(pagamento.getPedido().getValorTotal());

        // Corrigido: agora StatusProducao é entidade
        StatusProducao statusEntidade = pagamento.getPedido().getStatusProducao();
        if (statusEntidade != null) {
            pedidoDto.setStatusProducaoNome(statusEntidade.getNome());
            pedidoDto.setStatusProducaoId(statusEntidade.getId());
        }

        PagamentoResponseDto.UsuarioDto usuarioDto = new PagamentoResponseDto.UsuarioDto();
        usuarioDto.setId(pagamento.getUsuario().getId());
        usuarioDto.setNome(pagamento.getUsuario().getNome());

        MetodoPagamento metodoEntidade = pagamento.getMetodoPagamento();
        PagamentoResponseDto.MetodoPagamentoDto metodoDto = null;
        if (metodoEntidade != null) {
            metodoDto = new PagamentoResponseDto.MetodoPagamentoDto();
            metodoDto.setId(metodoEntidade.getId());
            metodoDto.setNome(metodoEntidade.getNome());
        }

        PagamentoResponseDto dto = new PagamentoResponseDto();
        dto.setId(pagamento.getId());
        dto.setValor(pagamento.getValor());
        dto.setDataPagamento(pagamento.getDataPagamento());
        dto.setMetodoPagamento(metodoDto);
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