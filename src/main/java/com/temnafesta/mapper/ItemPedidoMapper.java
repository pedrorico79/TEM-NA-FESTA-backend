package com.temnafesta.mapper;

import com.temnafesta.dto.pedido.ItemPedidoDto;
import com.temnafesta.dto.pedidoproduto.PedidoProdutoRequestDto;
import com.temnafesta.dto.pedidoproduto.PedidoProdutoResponseDto;
import com.temnafesta.model.ItemPedido;

import java.util.List;

public class ItemPedidoMapper {

    private ItemPedidoMapper() {}

    public static ItemPedido toEntity(PedidoProdutoRequestDto dto) {
        if (dto == null) return null;

        ItemPedido entity = new ItemPedido();
        entity.setQuantidade(dto.getQuantidade());
        entity.setPrecoUnitario(dto.getPrecoUnitario());

        return entity;
    }

    public static ItemPedido toEntity(ItemPedidoDto dto) {
        if (dto == null) return null;

        ItemPedido entity = new ItemPedido();
        entity.setQuantidade(dto.getQuantidade());
        entity.setPrecoUnitario(dto.getPrecoUnitario());

        return entity;
    }

    public static PedidoProdutoResponseDto toResponseDto(ItemPedido entity) {
        if (entity == null) return null;

        PedidoProdutoResponseDto dto = new PedidoProdutoResponseDto();
        dto.setId(entity.getId());
        dto.setQuantidade(entity.getQuantidade());
        dto.setPrecoUnitario(entity.getPrecoUnitario());
        // O subtotal é calculado automaticamente pelo getter

        // Mapeamento do PedidoDto
        if (entity.getPedido() != null) {
            PedidoProdutoResponseDto.PedidoDto pDto = new PedidoProdutoResponseDto.PedidoDto();
            pDto.setId(entity.getPedido().getId());
            pDto.setDataEntrega(entity.getPedido().getDataEntrega());

            // Buscando o nome do cliente através do relacionamento do Pedido
            if (entity.getPedido().getCliente() != null) {
                pDto.setNomeCliente(entity.getPedido().getCliente().getNome());
            }
            dto.setPedido(pDto);
        }

        // Mapeamento do ProdutoDto
        if (entity.getProduto() != null) {
            PedidoProdutoResponseDto.ProdutoDto prodDto = new PedidoProdutoResponseDto.ProdutoDto();
            prodDto.setId(entity.getProduto().getId());
            prodDto.setNome(entity.getProduto().getNome());
            dto.setProduto(prodDto);
        }

        return dto;
    }

    public static List<PedidoProdutoResponseDto> toResponseDtoList(List<ItemPedido> entities) {
        return entities.stream()
                .map(ItemPedidoMapper::toResponseDto)
                .toList();
    }
}