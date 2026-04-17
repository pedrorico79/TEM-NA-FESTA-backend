package com.temnafesta.mapper;

import com.temnafesta.dto.pedido.PedidoRequestDto;
import com.temnafesta.dto.pedido.PedidoResponseDto;
import com.temnafesta.model.*;

import java.util.List;

public class PedidoMapper {

    private PedidoMapper(){}

    public static Pedido toEntity(PedidoRequestDto dto) {
        Pedido pedido = new Pedido();
        pedido.setDataEntrega(dto.getDataEntrega());
        pedido.setValorTotal(dto.getValorTotal());
        pedido.setObservacao(dto.getObservacao());
        return pedido;
    }

    public static PedidoResponseDto toResponseDto(Pedido pedido) {

        Cliente clienteEntidade = pedido.getCliente();

        PedidoResponseDto.ClientePedidoDto clienteDto = new PedidoResponseDto.ClientePedidoDto();
        clienteDto.setId(clienteEntidade.getId());
        clienteDto.setNome(clienteEntidade.getNome());
        clienteDto.setTelefone(clienteEntidade.getTelefone());
        clienteDto.setWhatsapp(clienteEntidade.getWhatsapp());
        clienteDto.setInstagram(clienteEntidade.getInstagram());
        clienteDto.setDataCadastro(clienteEntidade.getDataCadastro());
        clienteDto.setAnotacoes(clienteEntidade.getAnotacoes());

        Endereco enderecoEntidade = pedido.getCliente().getEndereco();
        PedidoResponseDto.ClientePedidoDto.EnderecoClientePedidoDto enderecoDto = new PedidoResponseDto.ClientePedidoDto.EnderecoClientePedidoDto();
        enderecoDto.setId(enderecoEntidade.getId());
        enderecoDto.setCep(enderecoEntidade.getCep());
        enderecoDto.setCidade(enderecoEntidade.getCidade());
        enderecoDto.setNumero(enderecoEntidade.getNumero());
        enderecoDto.setLogradouro(enderecoEntidade.getLogradouro());

        clienteDto.setEndereco(enderecoDto);

        Usuario usuarioEntidade = pedido.getUsuario();

        PedidoResponseDto.UsuarioPedidoDto usuarioDto = new PedidoResponseDto.UsuarioPedidoDto();
        usuarioDto.setId(usuarioEntidade.getId());
        usuarioDto.setNome(usuarioEntidade.getNome());


        PedidoResponseDto.StatusPedidoDto statusDto = new PedidoResponseDto.StatusPedidoDto();
        statusDto.setNome(pedido.getStatusProducao().name());

        PedidoResponseDto dto = new PedidoResponseDto();
        dto.setId(pedido.getId());
        dto.setDataPedido(pedido.getDataPedido());
        dto.setDataEntrega(pedido.getDataEntrega());
        dto.setValorTotal(pedido.getValorTotal());
        dto.setObservacao(pedido.getObservacao());

        dto.setCliente(clienteDto);
        dto.setUsuario(usuarioDto);
        dto.setStatusProducao(statusDto);

        return dto;
    }

    public static List<PedidoResponseDto> toResponseDtoList(List<Pedido> pedidos) {
        return pedidos.stream()
                .map(PedidoMapper::toResponseDto)
                .toList();
    }
}