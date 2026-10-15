package com.temnafesta.presentation.mapper;

import com.temnafesta.application.dto.AtualizarPedidoCommand;
import com.temnafesta.application.dto.CriarPedidoCommand;
import com.temnafesta.domain.model.Endereco;
import com.temnafesta.domain.model.ItemPedido;
import com.temnafesta.domain.model.Pagamento;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.vo.StatusProducaoEnum;
import com.temnafesta.presentation.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PedidoPresentationMapperTest {

    private PedidoPresentationMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(PedidoPresentationMapper.class);
    }

    private static EnderecoRequestDto enderecoRequestDto() {
        return new EnderecoRequestDto(
                "01234567",
                "Rua das Flores",
                "123",
                "Apto 45",
                "Jardim Primavera",
                "São Paulo",
                "SP"
        );
    }

    private static Endereco enderecoDomain() {
        return new Endereco(1L, "01234567", "Rua das Flores", "123",
                "Apto 45", "Jardim Primavera", "São Paulo", "SP");
    }

    private static ItemPedidoRequestDto itemRequestDto() {
        return new ItemPedidoRequestDto(1L, new BigDecimal("50.00"), 2, "Sem lactose");
    }

    @Test
    void deveMapearCriarPedidoRequestDtoParaCommandComEndereco() {
        EnderecoRequestDto enderecoDto = enderecoRequestDto();
        CriarPedidoRequestDto request = new CriarPedidoRequestDto(
                1L,
                LocalDateTime.now().plusDays(7),
                new BigDecimal("10.00"),
                "Observação do pedido",
                null,
                enderecoDto,
                List.of(itemRequestDto())
        );

        CriarPedidoCommand command = mapper.toCommand(2L, request);

        assertEquals(1L, command.clienteId());
        assertEquals(2L, command.usuarioId());
        assertNotNull(command.dataEntrega());
        assertEquals(new BigDecimal("10.00"), command.taxaEntrega());
        assertEquals("Observação do pedido", command.observacao());
        assertNull(command.eventoId());

        Endereco endereco = command.enderecoEntrega();
        assertNotNull(endereco);
        assertEquals("01234567", endereco.getCep());
        assertEquals("Rua das Flores", endereco.getLogradouro());
        assertEquals("123", endereco.getNumero());
        assertEquals("Apto 45", endereco.getComplemento());
        assertEquals("Jardim Primavera", endereco.getBairro());
        assertEquals("São Paulo", endereco.getCidade());
        assertEquals("SP", endereco.getEstado());

        assertEquals(1, command.itens().size());
        assertEquals(1L, command.itens().getFirst().produtoId());
        assertEquals(2, command.itens().getFirst().quantidade());
        assertEquals(new BigDecimal("50.00"), command.itens().getFirst().precoUnitario());
    }

    @Test
    void deveMapearAtualizarPedidoRequestDtoParaCommandComEndereco() {
        EnderecoRequestDto enderecoDto = enderecoRequestDto();
        AtualizarPedidoRequestDto request = new AtualizarPedidoRequestDto(
                LocalDateTime.now().plusDays(5),
                new BigDecimal("15.00"),
                "Nova observação",
                3L,
                enderecoDto,
                List.of(itemRequestDto())
        );

        AtualizarPedidoCommand command = mapper.toCommand(10L, request);

        assertEquals(10L, command.pedidoId());
        assertNotNull(command.dataEntrega());
        assertEquals(new BigDecimal("15.00"), command.taxaEntrega());
        assertEquals("Nova observação", command.observacao());

        Endereco endereco = command.enderecoEntrega();
        assertNotNull(endereco);
        assertEquals("01234567", endereco.getCep());
        assertEquals("Rua das Flores", endereco.getLogradouro());
        assertEquals("123", endereco.getNumero());
        assertEquals("Apto 45", endereco.getComplemento());
        assertEquals("Jardim Primavera", endereco.getBairro());
        assertEquals("São Paulo", endereco.getCidade());
        assertEquals("SP", endereco.getEstado());
    }

    @Test
    void deveMapearPedidoDomainParaResponseDtoComEndereco() {
        Endereco endereco = enderecoDomain();
        Pedido pedido = new Pedido(
                100L,
                LocalDateTime.of(2024, 1, 15, 10, 30),
                LocalDateTime.of(2024, 1, 20, 14, 0),
                new BigDecimal("10.00"),
                "Obs do pedido",
                StatusProducaoEnum.CONFIRMADO,
                1L,
                2L,
                3L,
                endereco,
                List.of(new ItemPedido(1L, 1L, 2, new BigDecimal("50.00"), null)),
                List.of(new Pagamento(1L, new BigDecimal("50.00"), LocalDateTime.now(),
                        com.temnafesta.domain.vo.TipoPagamentoEnum.SINAL,
                        com.temnafesta.domain.vo.StatusPagamentoEnum.CONFIRMADO,
                        1L, 1L)),
                true,
                false
        );

        PedidoResponseDto response = mapper.toResponse(pedido);

        assertEquals(100L, response.id());
        assertEquals(LocalDateTime.of(2024, 1, 15, 10, 30), response.dataPedido());
        assertEquals(LocalDateTime.of(2024, 1, 20, 14, 0), response.dataEntrega());
        assertEquals(new BigDecimal("110.00"), response.valorTotal());
        assertEquals(new BigDecimal("10.00"), response.taxaEntrega());
        assertEquals("Obs do pedido", response.observacao());
        assertEquals("CONFIRMADO", response.statusProducao());
        assertEquals(1L, response.clienteId());
        assertEquals(2L, response.usuarioId());
        assertEquals(3L, response.eventoId());

        EnderecoResponseDto enderecoResponse = response.enderecoEntrega();
        assertNotNull(enderecoResponse);
        assertEquals(1L, enderecoResponse.id());
        assertEquals("01234567", enderecoResponse.cep());
        assertEquals("Rua das Flores", enderecoResponse.logradouro());
        assertEquals("123", enderecoResponse.numero());
        assertEquals("Apto 45", enderecoResponse.complemento());
        assertEquals("Jardim Primavera", enderecoResponse.bairro());
        assertEquals("São Paulo", enderecoResponse.cidade());
        assertEquals("SP", enderecoResponse.estado());

        assertEquals(1, response.itens().size());
        assertEquals(1, response.pagamentos().size());
    }

    @Test
    void deveMapearEnderecoDomainParaResponseDto() {
        Endereco endereco = enderecoDomain();

        EnderecoResponseDto response = mapper.toResponse(endereco);

        assertEquals(1L, response.id());
        assertEquals("01234567", response.cep());
        assertEquals("Rua das Flores", response.logradouro());
        assertEquals("123", response.numero());
        assertEquals("Apto 45", response.complemento());
        assertEquals("Jardim Primavera", response.bairro());
        assertEquals("São Paulo", response.cidade());
        assertEquals("SP", response.estado());
    }

    @Test
    void deveMapearEnderecoRequestDtoParaDomain() {
        EnderecoRequestDto request = enderecoRequestDto();

        Endereco domain = mapper.toDomain(request);

        assertNull(domain.getId());
        assertEquals("01234567", domain.getCep());
        assertEquals("Rua das Flores", domain.getLogradouro());
        assertEquals("123", domain.getNumero());
        assertEquals("Apto 45", domain.getComplemento());
        assertEquals("Jardim Primavera", domain.getBairro());
        assertEquals("São Paulo", domain.getCidade());
        assertEquals("SP", domain.getEstado());
    }

    @Test
    void deveMapearRequestDtoComEnderecoNull() {
        CriarPedidoRequestDto request = new CriarPedidoRequestDto(
                1L,
                LocalDateTime.now().plusDays(7),
                new BigDecimal("10.00"),
                "Observação",
                null,
                null,
                List.of(itemRequestDto())
        );

        CriarPedidoCommand command = mapper.toCommand(2L, request);

        assertNull(command.enderecoEntrega());
    }

    @Test
    void deveMapearRequestDtoComEnderecoSemComplemento() {
        EnderecoRequestDto enderecoDto = new EnderecoRequestDto(
                "01234567",
                "Rua das Flores",
                "123",
                null,
                "Jardim Primavera",
                "São Paulo",
                "SP"
        );
        CriarPedidoRequestDto request = new CriarPedidoRequestDto(
                1L,
                LocalDateTime.now().plusDays(7),
                new BigDecimal("10.00"),
                "Observação",
                null,
                enderecoDto,
                List.of(itemRequestDto())
        );

        CriarPedidoCommand command = mapper.toCommand(2L, request);

        assertNotNull(command.enderecoEntrega());
        assertNull(command.enderecoEntrega().getComplemento());
    }
}