package com.temnafesta.service;

import static org.junit.jupiter.api.Assertions.*;

import com.temnafesta.exception.cliente.ClienteComPedidosAtivosException;
import com.temnafesta.exception.cliente.ClienteNaoEncontrado;
import com.temnafesta.exception.endereco.EnderecoNaoEncontrado;
import com.temnafesta.model.Cliente;
import com.temnafesta.model.Endereco;
import com.temnafesta.repository.ClienteRepository;
import com.temnafesta.repository.EnderecoRepository;
import com.temnafesta.repository.PedidoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Nested
    @DisplayName("Testes do método criar")
    class Criar {

        @Test
        @DisplayName("Deve criar cliente com sucesso")
        void deveCriarClienteComSucesso() {

            Cliente cliente = new Cliente();

            Endereco endereco = new Endereco();
            endereco.setId(1);

            when(enderecoRepository.findById(1))
                    .thenReturn(Optional.of(endereco));

            when(clienteRepository.save(cliente))
                    .thenReturn(cliente);

            Cliente resultado = clienteService.criar(cliente, 1);

            assertNotNull(resultado);
            assertEquals(endereco, cliente.getEndereco());

            verify(enderecoRepository).findById(1);
            verify(clienteRepository).save(cliente);
        }

        @Test
        @DisplayName("Deve lançar exceção quando endereço não existir")
        void deveLancarExcecaoQuandoEnderecoNaoExistir() {

            Cliente cliente = new Cliente();

            when(enderecoRepository.findById(1))
                    .thenReturn(Optional.empty());

            assertThrows(
                    EnderecoNaoEncontrado.class,
                    () -> clienteService.criar(cliente, 1)
            );

            verify(enderecoRepository).findById(1);

            verify(clienteRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Testes do método listarAtivos")
    class ListarAtivos {

        @Test
        @DisplayName("Deve listar clientes ativos")
        void deveListarClientesAtivos() {

            Cliente cliente1 = new Cliente();
            Cliente cliente2 = new Cliente();

            List<Cliente> clientes = List.of(cliente1, cliente2);

            when(clienteRepository.findByIsAtivoTrue())
                    .thenReturn(clientes);

            List<Cliente> resultado = clienteService.listarAtivos();

            assertEquals(2, resultado.size());

            verify(clienteRepository).findByIsAtivoTrue();
        }
    }

    @Nested
    @DisplayName("Testes do método listarTodos")
    class ListarTodos {

        @Test
        @DisplayName("Deve listar todos os clientes")
        void deveListarTodosClientes() {

            Cliente cliente1 = new Cliente();
            Cliente cliente2 = new Cliente();

            List<Cliente> clientes = List.of(cliente1, cliente2);

            when(clienteRepository.findAll())
                    .thenReturn(clientes);

            List<Cliente> resultado = clienteService.listarTodos();

            assertEquals(2, resultado.size());

            verify(clienteRepository).findAll();
        }
    }

    @Nested
    @DisplayName("Testes do método buscarPorId")
    class BuscarPorId {

        @Test
        @DisplayName("Deve buscar cliente por id")
        void deveBuscarClientePorId() {

            Cliente cliente = new Cliente();
            cliente.setId(1);

            when(clienteRepository.findById(1))
                    .thenReturn(Optional.of(cliente));

            Cliente resultado = clienteService.buscarPorId(1);

            assertNotNull(resultado);
            assertEquals(1, resultado.getId());

            verify(clienteRepository).findById(1);
        }

        @Test
        @DisplayName("Deve lançar exceção quando cliente não existir")
        void deveLancarExcecaoQuandoClienteNaoExistir() {

            when(clienteRepository.findById(1))
                    .thenReturn(Optional.empty());

            assertThrows(
                    ClienteNaoEncontrado.class,
                    () -> clienteService.buscarPorId(1)
            );

            verify(clienteRepository).findById(1);
        }
    }

    @Nested
    @DisplayName("Testes do método atualizar")
    class Atualizar {

        @Test
        @DisplayName("Deve atualizar cliente com sucesso")
        void deveAtualizarClienteComSucesso() {

            Cliente clienteAtualizado = new Cliente();

            Endereco endereco = new Endereco();
            endereco.setId(1);

            when(clienteRepository.existsById(1))
                    .thenReturn(true);

            when(enderecoRepository.findById(1))
                    .thenReturn(Optional.of(endereco));

            when(clienteRepository.save(clienteAtualizado))
                    .thenReturn(clienteAtualizado);

            Cliente resultado = clienteService.atualizar(1, clienteAtualizado, 1);

            assertNotNull(resultado);
            assertEquals(1, clienteAtualizado.getId());
            assertEquals(endereco, clienteAtualizado.getEndereco());

            verify(clienteRepository).existsById(1);
            verify(enderecoRepository).findById(1);
            verify(clienteRepository).save(clienteAtualizado);
        }

        @Test
        @DisplayName("Deve lançar exceção quando cliente não existir")
        void deveLancarExcecaoQuandoClienteNaoExistir() {

            Cliente clienteAtualizado = new Cliente();

            when(clienteRepository.existsById(1))
                    .thenReturn(false);

            assertThrows(
                    ClienteNaoEncontrado.class,
                    () -> clienteService.atualizar(1, clienteAtualizado, 1)
            );

            verify(clienteRepository).existsById(1);

            verify(enderecoRepository, never()).findById(any());
            verify(clienteRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve lançar exceção quando endereço não existir")
        void deveLancarExcecaoQuandoEnderecoNaoExistir() {

            Cliente clienteAtualizado = new Cliente();

            when(clienteRepository.existsById(1))
                    .thenReturn(true);

            when(enderecoRepository.findById(1))
                    .thenReturn(Optional.empty());

            assertThrows(
                    EnderecoNaoEncontrado.class,
                    () -> clienteService.atualizar(1, clienteAtualizado, 1)
            );

            verify(clienteRepository).existsById(1);
            verify(enderecoRepository).findById(1);

            verify(clienteRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Testes do método desativar")
    class Desativar {

        @Test
        @DisplayName("Deve desativar cliente com sucesso")
        void deveDesativarClienteComSucesso() {

            Cliente cliente = new Cliente();
            cliente.setId(1);
            cliente.setAtivo(true);

            when(clienteRepository.findById(1))
                    .thenReturn(Optional.of(cliente));

            when(pedidoRepository.existsPedidosAtivosParaCliente(1))
                    .thenReturn(false);

            clienteService.desativar(1);

            assertFalse(cliente.getAtivo());

            verify(clienteRepository).findById(1);
            verify(pedidoRepository).existsPedidosAtivosParaCliente(1);
            verify(clienteRepository).save(cliente);
        }

        @Test
        @DisplayName("Deve lançar exceção quando cliente tiver pedidos ativos")
        void deveLancarExcecaoQuandoClientePossuirPedidosAtivos() {

            Cliente cliente = new Cliente();
            cliente.setId(1);

            when(clienteRepository.findById(1))
                    .thenReturn(Optional.of(cliente));

            when(pedidoRepository.existsPedidosAtivosParaCliente(1))
                    .thenReturn(true);

            assertThrows(
                    ClienteComPedidosAtivosException.class,
                    () -> clienteService.desativar(1)
            );

            verify(clienteRepository).findById(1);
            verify(pedidoRepository).existsPedidosAtivosParaCliente(1);

            verify(clienteRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve lançar exceção quando cliente não existir")
        void deveLancarExcecaoQuandoClienteNaoExistir() {

            when(clienteRepository.findById(1))
                    .thenReturn(Optional.empty());

            assertThrows(
                    ClienteNaoEncontrado.class,
                    () -> clienteService.desativar(1)
            );

            verify(clienteRepository).findById(1);

            verify(pedidoRepository, never())
                    .existsPedidosAtivosParaCliente(any());

            verify(clienteRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Testes do método reativar")
    class Reativar {

        @Test
        @DisplayName("Deve reativar cliente com sucesso")
        void deveReativarClienteComSucesso() {

            Cliente cliente = new Cliente();
            cliente.setId(1);
            cliente.setAtivo(false);

            when(clienteRepository.findById(1))
                    .thenReturn(Optional.of(cliente));

            clienteService.reativar(1);

            assertTrue(cliente.getAtivo());

            verify(clienteRepository).findById(1);
            verify(clienteRepository).save(cliente);
        }

        @Test
        @DisplayName("Deve lançar exceção quando cliente não existir")
        void deveLancarExcecaoQuandoClienteNaoExistir() {

            when(clienteRepository.findById(1))
                    .thenReturn(Optional.empty());

            assertThrows(
                    ClienteNaoEncontrado.class,
                    () -> clienteService.reativar(1)
            );

            verify(clienteRepository).findById(1);

            verify(clienteRepository, never()).save(any());
        }
    }
}