package com.temnafesta.mapper;

import com.temnafesta.dto.cliente.ClienteRequestDto;
import com.temnafesta.dto.cliente.ClienteResponseDto;
import com.temnafesta.model.Cliente;
import com.temnafesta.model.Endereco;

import java.util.List;

public class ClienteMapper {

    private ClienteMapper(){}

    public static Cliente toEntity(ClienteRequestDto dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setTelefone(dto.getTelefone());
        cliente.setWhatsapp(dto.getWhatsapp());
        cliente.setInstagram(dto.getInstagram());
        cliente.setAnotacoes(dto.getAnotacoes());
        return cliente;
    }

    public static ClienteResponseDto toResponseDto(Cliente cliente) {

        Endereco enderecoEntidade =
                cliente.getEndereco();

        ClienteResponseDto.EnderecoClienteDto enderecoDto = new
                ClienteResponseDto.EnderecoClienteDto();

        enderecoDto.setId(enderecoEntidade.getId());
        enderecoDto.setCep(enderecoEntidade.getCep());
        enderecoDto.setLogradouro(enderecoEntidade.getLogradouro());
        enderecoDto.setNumero(enderecoEntidade.getNumero());
        enderecoDto.setCidade(enderecoEntidade.getCidade());
        ClienteResponseDto dto = new ClienteResponseDto();

        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setTelefone(cliente.getTelefone());
        dto.setWhatsapp(cliente.getWhatsapp());
        dto.setInstagram(cliente.getInstagram());
        dto.setDataCadastro(cliente.getDataCadastro());
        dto.setAnotacoes(cliente.getAnotacoes());
        dto.setEndereco(enderecoDto);

        return dto;
    }

    public static List<ClienteResponseDto> toResponseDtoList(List<Cliente> clientes) {
        return clientes.stream()
                .map(ClienteMapper::toResponseDto)
                .toList();
    }
}
