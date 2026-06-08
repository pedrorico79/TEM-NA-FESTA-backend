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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

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
    @DisplayName("Testes do método listar")
    class Listar {

        @Test
        @DisplayName("Deve listar clientes com filtro de busca")
        void deveListarClientesComFiltro() {

            Pageable pageable = PageRequest.of(0, 10);
            Page<Cliente> page = new PageImpl<>(List.of(new Cliente(), new Cliente()));

            when(clienteRepository.findByIsDeletadoFalseAndNomeContainingIgnoreCase("João", pageable))
                    .thenReturn(page);

            Page<Cliente> resultado = clienteService.listar("João", pageable);

            assertEquals(2, resultado.getTotalElements());

            verify(clienteRepository).findByIsDeletadoFalseAndNomeContainingIgnoreCase("João", pageable);
        }

        @Test
        @DisplayName("Deve listar clientes sem filtro quando busca for nula")
        void deveListarClientesSemFiltroQuandoBuscaForNula() {

            Pageable pageable = PageRequest.of(0, 10);
            Page<Cliente> page = new PageImpl<>(List.of(new Cliente()));

            when(clienteRepository.findByIsDeletadoFalseAndNomeContainingIgnoreCase("", pageable))
                    .thenReturn(page);

            Page<Cliente> resultado = clienteService.listar(null, pageable);

            assertEquals(1, resultado.getTotalElements());

            verify(clienteRepository).findByIsDeletadoFalseAndNomeContainingIgnoreCase("", pageable);
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
    @DisplayName("Testes do método toggleAtivo")
    class ToggleAtivo {

        @Test
        @DisplayName("Deve desativar cliente ativo sem pedidos ativos")
        void deveDesativarClienteAtivoSemPedidosAtivos() {

            Cliente cliente = new Cliente();
            cliente.setId(1);
            cliente.setIsAtivo(true);

            when(clienteRepository.findById(1))
                    .thenReturn(Optional.of(cliente));

            when(pedidoRepository.existsPedidosAtivosParaCliente(1))
                    .thenReturn(false);

            clienteService.toggleAtivo(1);

            assertFalse(cliente.getIsAtivo());

            verify(clienteRepository).findById(1);
            verify(pedidoRepository).existsPedidosAtivosParaCliente(1);
            verify(clienteRepository).save(cliente);
        }

        @Test
        @DisplayName("Deve lançar exceção ao desativar cliente com pedidos ativos")
        void deveLancarExcecaoAoDesativarClienteComPedidosAtivos() {

            Cliente cliente = new Cliente();
            cliente.setId(1);
            cliente.setIsAtivo(true);

            when(clienteRepository.findById(1))
                    .thenReturn(Optional.of(cliente));

            when(pedidoRepository.existsPedidosAtivosParaCliente(1))
                    .thenReturn(true);

            assertThrows(
                    ClienteComPedidosAtivosException.class,
                    () -> clienteService.toggleAtivo(1)
            );

            verify(clienteRepository).findById(1);
            verify(pedidoRepository).existsPedidosAtivosParaCliente(1);
            verify(clienteRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve reativar cliente inativo sem verificar pedidos")
        void deveReativarClienteInativoSemVerificarPedidos() {

            Cliente cliente = new Cliente();
            cliente.setId(1);
            cliente.setIsAtivo(false);

            when(clienteRepository.findById(1))
                    .thenReturn(Optional.of(cliente));

            clienteService.toggleAtivo(1);

            assertTrue(cliente.getIsAtivo());

            verify(clienteRepository).findById(1);
            verify(pedidoRepository, never()).existsPedidosAtivosParaCliente(any());
            verify(clienteRepository).save(cliente);
        }

        @Test
        @DisplayName("Deve lançar exceção quando cliente não existir")
        void deveLancarExcecaoQuandoClienteNaoExistir() {

            when(clienteRepository.findById(1))
                    .thenReturn(Optional.empty());

            assertThrows(
                    ClienteNaoEncontrado.class,
                    () -> clienteService.toggleAtivo(1)
            );

            verify(clienteRepository).findById(1);
            verify(pedidoRepository, never()).existsPedidosAtivosParaCliente(any());
            verify(clienteRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Testes do método deletar")
    class Deletar {

        @Test
        @DisplayName("Deve deletar cliente com sucesso")
        void deveDeletarClienteComSucesso() {

            Cliente cliente = new Cliente();
            cliente.setId(1);
            cliente.setIsDeletado(false);
            cliente.setIsAtivo(true);

            when(clienteRepository.findById(1))
                    .thenReturn(Optional.of(cliente));

            clienteService.deletar(1);

            assertTrue(cliente.getIsDeletado());
            assertFalse(cliente.getIsAtivo());

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
                    () -> clienteService.deletar(1)
            );

            verify(clienteRepository).findById(1);
            verify(clienteRepository, never()).save(any());
        }
    }
}