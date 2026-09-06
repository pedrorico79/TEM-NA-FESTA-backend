package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.AlterarAtivoClienteCommand;
import com.temnafesta.application.dto.AtualizarClienteCommand;
import com.temnafesta.application.dto.CriarClienteCommand;
import com.temnafesta.application.dto.CriarEndrecoCommand;
import com.temnafesta.application.exception.RecursoNaoEncontradoException;
import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.Cliente;
import com.temnafesta.domain.ports.repository.ClienteRepositoryPort;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteUseCasesTest {

    @Mock
    private ClienteRepositoryPort clienteRepositoryPort;
    @Mock
    private PedidoRepositoryPort pedidoRepositoryPort;

    @InjectMocks
    private CriarClienteUseCase criarClienteUseCase;
    @InjectMocks
    private AtualizarClienteUseCase atualizarClienteUseCase;
    @InjectMocks
    private AlterarAtivoClienteUseCase alterarAtivoClienteUseCase;
    @InjectMocks
    private DeletarClienteUseCase deletarClienteUseCase;
    @InjectMocks
    private BuscarClientePorIdUseCase buscarClientePorIdUseCase;
    @InjectMocks
    private ListarClientesUseCase listarClientesUseCase;

    private static Cliente clienteAtivo() {
        return new Cliente(1L, "Maria", "11999999999", null, null, null, null, null, true, false);
    }

    private static CriarEndrecoCommand endereco() {
        return new CriarEndrecoCommand("01234-567", "Rua A", "100", null, "Centro", "São Paulo", "SP");
    }

    @Nested
    class Criar {
        @Test
        void deveCriarAtivoComEndereco() {
            CriarClienteCommand command = new CriarClienteCommand(
                    "Maria", "11999999999", null, null, null, endereco());
            when(clienteRepositoryPort.salvar(any(Cliente.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            Cliente resultado = criarClienteUseCase.executar(command);

            ArgumentCaptor<Cliente> captor = ArgumentCaptor.forClass(Cliente.class);
            verify(clienteRepositoryPort).salvar(captor.capture());
            assertTrue(captor.getValue().isAtivo());
            assertNotNull(captor.getValue().getEndereco());
            assertEquals("01234-567", resultado.getEndereco().getCep());
        }

        @Test
        void deveCriarSemEnderecoQuandoAusente() {
            CriarClienteCommand command = new CriarClienteCommand(
                    "Maria", null, null, null, null, null);
            when(clienteRepositoryPort.salvar(any(Cliente.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            Cliente resultado = criarClienteUseCase.executar(command);

            assertEquals(null, resultado.getEndereco());
        }
    }

    @Nested
    class Atualizar {
        @Test
        void deveAtualizarTrimandoNomeEPreservandoCadastro() {
            Cliente existente = clienteAtivo();
            AtualizarClienteCommand command = new AtualizarClienteCommand(
                    1L, "  Maria Silva  ", null, null, null, null, null);
            when(clienteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(existente));
            when(clienteRepositoryPort.salvar(any(Cliente.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            Cliente resultado = atualizarClienteUseCase.executar(command);

            assertEquals("Maria Silva", resultado.getNome());
            assertEquals(existente.getDataCadastro(), resultado.getDataCadastro());
        }

        @Test
        void naoDeveAtualizarQuandoNaoEncontrado() {
            AtualizarClienteCommand command = new AtualizarClienteCommand(
                    1L, "Maria", null, null, null, null, null);
            when(clienteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.empty());

            assertThrows(RecursoNaoEncontradoException.class,
                    () -> atualizarClienteUseCase.executar(command));
            verify(clienteRepositoryPort, never()).salvar(any());
        }
    }

    @Nested
    class AlterarAtivo {
        @Test
        void deveRetornarSemSalvarQuandoStatusIgual() {
            Cliente cliente = clienteAtivo();
            when(clienteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(cliente));

            Cliente resultado = alterarAtivoClienteUseCase.executar(new AlterarAtivoClienteCommand(1L, true));

            assertEquals(cliente, resultado);
            verify(clienteRepositoryPort, never()).salvar(any());
        }

        @Test
        void deveDesativarQuandoSemPedidoEmAndamento() {
            Cliente cliente = clienteAtivo();
            when(clienteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(cliente));
            when(pedidoRepositoryPort.existePedidoEmAndamentoPorCliente(1L)).thenReturn(false);
            when(clienteRepositoryPort.salvar(any(Cliente.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            Cliente resultado = alterarAtivoClienteUseCase.executar(new AlterarAtivoClienteCommand(1L, false));

            assertFalse(resultado.isAtivo());
        }

        @Test
        void naoDeveDesativarComPedidoEmAndamento() {
            Cliente cliente = clienteAtivo();
            when(clienteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(cliente));
            when(pedidoRepositoryPort.existePedidoEmAndamentoPorCliente(1L)).thenReturn(true);

            assertThrows(RegraDeNegocioException.class, () -> alterarAtivoClienteUseCase
                    .executar(new AlterarAtivoClienteCommand(1L, false)));
            verify(clienteRepositoryPort, never()).salvar(any());
        }

        @Test
        void naoDeveAlterarQuandoNaoEncontrado() {
            when(clienteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.empty());

            assertThrows(RecursoNaoEncontradoException.class, () -> alterarAtivoClienteUseCase
                    .executar(new AlterarAtivoClienteCommand(1L, false)));
        }
    }

    @Nested
    class Deletar {
        @Test
        void deveDeletarQuandoSemPedidoEmAndamento() {
            Cliente cliente = clienteAtivo();
            when(clienteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(cliente));
            when(pedidoRepositoryPort.existePedidoEmAndamentoPorCliente(1L)).thenReturn(false);

            deletarClienteUseCase.executar(1L);

            assertTrue(cliente.isDeletado());
            verify(clienteRepositoryPort).salvar(cliente);
        }

        @Test
        void naoDeveDeletarComPedidoEmAndamento() {
            when(clienteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(clienteAtivo()));
            when(pedidoRepositoryPort.existePedidoEmAndamentoPorCliente(1L)).thenReturn(true);

            assertThrows(RegraDeNegocioException.class, () -> deletarClienteUseCase.executar(1L));
            verify(clienteRepositoryPort, never()).salvar(any());
        }

        @Test
        void naoDeveDeletarQuandoNaoEncontrado() {
            when(clienteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.empty());

            assertThrows(RecursoNaoEncontradoException.class, () -> deletarClienteUseCase.executar(1L));
        }
    }

    @Nested
    class BuscarEListar {
        @Test
        void deveBuscarPorId() {
            when(clienteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(clienteAtivo()));

            assertEquals("Maria", buscarClientePorIdUseCase.executar(1L).getNome());
        }

        @Test
        void naoDeveBuscarQuandoNaoEncontrado() {
            when(clienteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.empty());

            assertThrows(RecursoNaoEncontradoException.class, () -> buscarClientePorIdUseCase.executar(1L));
        }

        @Test
        void deveTrimarBuscaAoListar() {
            when(clienteRepositoryPort.listarNaoDeletadosPorBusca("maria"))
                    .thenReturn(List.of(clienteAtivo()));

            assertEquals(1, listarClientesUseCase.executar("  maria  ").size());
            verify(clienteRepositoryPort).listarNaoDeletadosPorBusca("maria");
        }

        @Test
        void deveListarVazioSemErro() {
            when(clienteRepositoryPort.listarNaoDeletadosPorBusca("")).thenReturn(List.of());

            assertTrue(listarClientesUseCase.executar(null).isEmpty());
            verify(clienteRepositoryPort).listarNaoDeletadosPorBusca("");
        }
    }
}
