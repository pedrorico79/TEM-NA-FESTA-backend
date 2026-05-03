package com.temnafesta.mapper;

import com.temnafesta.dto.pedido.PedidoRequestDto;
import com.temnafesta.dto.pedido.PedidoResponseDto;
import com.temnafesta.model.*;

import java.math.BigDecimal;

public class PedidoMapper {

    private PedidoMapper() {
    }

    public static Pedido toEntity(PedidoRequestDto dto) {
        Pedido pedido = new Pedido();
        pedido.setDataEntrega(dto.getDataEntrega());
        pedido.setValorTotal(dto.getValorTotal());
        pedido.setObservacao(dto.getObservacao());
        return pedido;
    }


    public static PedidoResponseDto toResponseDto(Pedido pedido, BigDecimal valorPago) {

        Endereco enderecoEntidade = pedido.getCliente().getEndereco();
        PedidoResponseDto.ClientePedidoDto.EnderecoClientePedidoDto enderecoDto =
                new PedidoResponseDto.ClientePedidoDto.EnderecoClientePedidoDto();
        enderecoDto.setId(enderecoEntidade.getId());
        enderecoDto.setCep(enderecoEntidade.getCep());
        enderecoDto.setLogradouro(enderecoEntidade.getLogradouro());
        enderecoDto.setNumero(enderecoEntidade.getNumero());
        enderecoDto.setCidade(enderecoEntidade.getCidade());

        Cliente clienteEntidade = pedido.getCliente();
        PedidoResponseDto.ClientePedidoDto clienteDto = new PedidoResponseDto.ClientePedidoDto();
        clienteDto.setId(clienteEntidade.getId());
        clienteDto.setNome(clienteEntidade.getNome());
        clienteDto.setTelefone(clienteEntidade.getTelefone());
        clienteDto.setWhatsapp(clienteEntidade.getWhatsapp());
        clienteDto.setInstagram(clienteEntidade.getInstagram());
        clienteDto.setDataCadastro(clienteEntidade.getDataCadastro());
        clienteDto.setAnotacoes(clienteEntidade.getAnotacoes());
        clienteDto.setEndereco(enderecoDto);

        Usuario usuarioEntidade = pedido.getUsuario();
        PedidoResponseDto.UsuarioPedidoDto usuarioDto = new PedidoResponseDto.UsuarioPedidoDto();
        usuarioDto.setId(usuarioEntidade.getId());
        usuarioDto.setNome(usuarioEntidade.getNome());

        PedidoResponseDto.StatusPedidoDto statusDto = new PedidoResponseDto.StatusPedidoDto();
        statusDto.setNome(pedido.getStatusProducao().name());

        PedidoResponseDto.CampanhaPedidoDto campanhaDto = null;
        if (pedido.getCampanha() != null) {
            campanhaDto = new PedidoResponseDto.CampanhaPedidoDto();
            campanhaDto.setId(pedido.getCampanha().getId());
            campanhaDto.setNome(pedido.getCampanha().getNome());
        }

        BigDecimal valorTotal = pedido.getValorTotal();
        boolean isPago = valorPago.compareTo(valorTotal) >= 0;

        PedidoResponseDto dto = new PedidoResponseDto();
        dto.setId(pedido.getId());
        dto.setDataPedido(pedido.getDataPedido());
        dto.setDataEntrega(pedido.getDataEntrega());
        dto.setValorTotal(valorTotal);
        dto.setValorPago(valorPago);
        dto.setIsPago(isPago);
        dto.setObservacao(pedido.getObservacao());
        dto.setCliente(clienteDto);
        dto.setUsuario(usuarioDto);
        dto.setStatusProducao(statusDto);
        dto.setCampanha(campanhaDto);

        return dto;
    }
}