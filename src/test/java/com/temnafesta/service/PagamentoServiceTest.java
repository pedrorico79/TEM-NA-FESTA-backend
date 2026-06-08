package com.temnafesta.service;

import com.temnafesta.dto.pagamento.PagamentoRequestDto;
import com.temnafesta.exception.pagamento.PagamentoNaoEncontrado;
import com.temnafesta.exception.pedido.PedidoNaoEncontrado;
import com.temnafesta.exception.usuario.UsuarioNaoEncontrado;
import com.temnafesta.model.MetodoPagamento;
import com.temnafesta.model.Pagamento;
import com.temnafesta.model.Pedido;
import com.temnafesta.model.Usuario;
import com.temnafesta.repository.PagamentoRepository;
import com.temnafesta.repository.PedidoRepository;
import com.temnafesta.repository.UsuarioRepository;
import com.temnafesta.repository.MetodoPagamentoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pagamento Service Tests")
class PagamentoServiceTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private MetodoPagamentoRepository metodoPagamentoRepository;

    @InjectMocks
    private PagamentoService pagamentoService;


    @Nested
    @DisplayName("1 Cenários do método criar")
    class CriarTestes {

        @Test
        @DisplayName("Deve criar e retornar pagamento corretamente")
        void deveCriarRetornarPagamento() {
            PagamentoRequestDto dto = new PagamentoRequestDto();
            dto.setPedidoId(1);
            dto.setUsuarioId(1);

            Pedido pedido = new Pedido();
            Usuario usuario = new Usuario();
            Pagamento pagamento = new Pagamento();

            when(pedidoRepository.findById(1))
                    .thenReturn(Optional.of(pedido));
            when(usuarioRepository.findById(1))
                    .thenReturn(Optional.of(usuario));

            when(pagamentoRepository.save(any(Pagamento.class)))
                    .thenReturn(pagamento);

            MetodoPagamento metodo = new MetodoPagamento();
            when(metodoPagamentoRepository.findById(dto.getMetodoPagamentoId()))
                    .thenReturn(Optional.of(metodo));

            Pagamento resultado = pagamentoService.criar(dto);

            assertNotNull(resultado);
            assertEquals(pagamento, resultado);
            verify(pagamentoRepository).save(any(Pagamento.class));
        }

        @Test
        @DisplayName("Deve lançar PedidoNaoEncontrado quando o pedido não existir")
        void deveLancarPedidoNaoEncontrado() {
            PagamentoRequestDto dto = new PagamentoRequestDto();
            dto.setPedidoId(1);
            dto.setUsuarioId(1);

            when(pedidoRepository.findById(1))
                    .thenReturn(Optional.empty());

            PedidoNaoEncontrado exception = assertThrows(
                    PedidoNaoEncontrado.class,
                    () -> pagamentoService.criar(dto)
            );

            assertEquals(
                    "Pedido com id 1 não encontrado",
                    exception.getMessage()
            );

            verify(pagamentoRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve lançar UsuarioNaoEncontrado quando o usuário não existir")
        void deveLancarUsuarioNaoEncontrado() {
            PagamentoRequestDto dto = new PagamentoRequestDto();
            dto.setPedidoId(1);
            dto.setUsuarioId(1);

            Pedido pedido = new Pedido();

            when(pedidoRepository.findById(1))
                    .thenReturn(Optional.of(pedido));

            when(usuarioRepository.findById(1))
                    .thenReturn(Optional.empty());

            UsuarioNaoEncontrado exception = assertThrows(
                    UsuarioNaoEncontrado.class,
                    () -> pagamentoService.criar(dto)
            );

            assertEquals(
                    "Usuario com id 1 não encontrado",
                    exception.getMessage()
            );

            verify(pagamentoRepository, never()).save(any());
        }

    }


    @Nested
    @DisplayName("2 Cenários do método buscarPorId")
    class BuscarPorIdTestes {

        @Test
        @DisplayName("Deve buscar um pagamento por id corretamente")
        void buscarPorId() {
            Pagamento p = new Pagamento();
            when(pagamentoRepository.findById(1))
                    .thenReturn(Optional.of(p));
            Pagamento resultado = pagamentoService.buscarPorId(1);
            assertEquals(p, resultado);
            assertEquals(p.getId(), resultado.getId());
            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Deve lançar PagamentoNaoEncontrado quando o pagamento não existir")
        void deveLancarPagamentoNaoEncontrado() {
            when(pagamentoRepository.findById(1))
                    .thenReturn(Optional.empty());
            PagamentoNaoEncontrado exception = assertThrows(
                    PagamentoNaoEncontrado.class,
                    () -> pagamentoService.buscarPorId(1)
            );
            assertEquals(
                    "Pagamento com id 1 não encontrado",
                    exception.getMessage()
            );
        }

    }


    @Nested
    @DisplayName("3 Cenários do método listar")
    class ListarTestes {

        @Test
        @DisplayName("Deve listar todos os pagamentos corretamente")
        void deveListarTodosCorretamente() {
            List<Pagamento> lista = new ArrayList<>();
            Pagamento p = new Pagamento();
            lista.add(p);

            when(pagamentoRepository.findAll())
                    .thenReturn(lista);

            List<Pagamento> resultado = pagamentoService.listar();

            assertNotNull(resultado);
            assertEquals(1, resultado.size());
            assertEquals(lista, resultado);
            assertIterableEquals(lista, resultado);
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não houver pagamentos")
        void deveRetornarListaVazia() {
            List<Pagamento> listaVazia = new ArrayList<>();

            when(pagamentoRepository.findAll())
                    .thenReturn(listaVazia);

            List<Pagamento> resultado = pagamentoService.listar();

            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());
            assertEquals(listaVazia, resultado);
        }
    }


    @Nested
    @DisplayName("4 Cenários do método listarPorPedido")
    class ListarPorPedidoTestes {

        @Test
        @DisplayName("Deve listar pagamentos por pedido corretamente")
        void deveListarPorPedidoCorretamente() {

            Integer pedidoId = 1;

            List<Pagamento> pagamentos = List.of(
                    new Pagamento(),
                    new Pagamento()
            );

            when(pedidoRepository.existsById(pedidoId))
                    .thenReturn(true);

            when(pagamentoRepository.findByPedidoId(pedidoId))
                    .thenReturn(pagamentos);

            List<Pagamento> resultado =
                    pagamentoService.listarPorPedido(pedidoId);

            assertNotNull(resultado);
            assertEquals(2, resultado.size());

            verify(pedidoRepository).existsById(pedidoId);
            verify(pagamentoRepository).findByPedidoId(pedidoId);
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não houver pagamentos para o pedido")
        void deveRetornarListaVazia() {

            Integer pedidoId = 1;

            when(pedidoRepository.existsById(pedidoId))
                    .thenReturn(true);

            when(pagamentoRepository.findByPedidoId(pedidoId))
                    .thenReturn(List.of());

            List<Pagamento> resultado =
                    pagamentoService.listarPorPedido(pedidoId);

            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());

            verify(pedidoRepository).existsById(pedidoId);
            verify(pagamentoRepository).findByPedidoId(pedidoId);
        }

        @Test
        @DisplayName("Deve lançar PedidoNaoEncontrado quando o pedido não existir")
        void deveLancarPedidoNaoEncontrado() {

            Integer pedidoId = 1;

            when(pedidoRepository.existsById(pedidoId))
                    .thenReturn(false);

            PedidoNaoEncontrado exception = assertThrows(
                    PedidoNaoEncontrado.class,
                    () -> pagamentoService.listarPorPedido(pedidoId)
            );

            assertEquals(
                    "Pedido com id 1 não encontrado",
                    exception.getMessage()
            );

            verify(pedidoRepository).existsById(pedidoId);
            verify(pagamentoRepository, never())
                    .findByPedidoId(any());
        }
    }


    @Nested
    @DisplayName("5 Cenários do método deletar")
    class DeletarTestes {

        @Test
        @DisplayName("Deve deletar um pagamento por id corretamente")
        void deveDeletarPorIdCorretamente() {
            Integer pagamentoId = 1;

            when(pagamentoRepository.existsById(pagamentoId))
                    .thenReturn(true);

            pagamentoService.deletar(pagamentoId);

            verify(pagamentoRepository).deleteById(pagamentoId);
        }

        @Test
        @DisplayName("Deve lançar PagamentoNaoEncontrado quando o pagamento não existir")
        void deveLancarPagamentoNaoEncontrado() {
            Integer pagamentoId = 1;

            when(pagamentoRepository.existsById(pagamentoId))
                    .thenReturn(false);

            PagamentoNaoEncontrado exception = assertThrows(
                    PagamentoNaoEncontrado.class,
                    () -> pagamentoService.deletar(pagamentoId)
            );

            assertEquals(
                    "Pagamento com id 1 não encontrado",
                    exception.getMessage()
            );

            verify(pagamentoRepository).existsById(pagamentoId);
            verify(pagamentoRepository, never())
                    .deleteById(any());
        }
    }
}