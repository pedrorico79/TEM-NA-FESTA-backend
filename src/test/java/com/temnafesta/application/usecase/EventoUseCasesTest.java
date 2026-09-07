package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.AtualizarEventoCommand;
import com.temnafesta.application.dto.CriarEventoCommand;
import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.Evento;
import com.temnafesta.domain.ports.repository.EventoRepositoryPort;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventoUseCasesTest {

    @Mock
    private EventoRepositoryPort eventoRepositoryPort;

    @InjectMocks
    private CriarEventoUseCase criarEventoUseCase;
    @InjectMocks
    private AtualizarEventoUseCase atualizarEventoUseCase;
    @InjectMocks
    private DeletarEventoUseCase deletarEventoUseCase;
    @InjectMocks
    private AlterarStatusEventoUseCase alterarStatusEventoUseCase;
    @InjectMocks
    private ListarEventosAtivosUseCase listarEventosAtivosUseCase;

    private static CriarEventoCommand criarCommand() {
        return new CriarEventoCommand("  Festa Junina  ",
                LocalDate.of(2026, 6, 10), LocalDate.of(2026, 6, 20));
    }

    private static Evento eventoAtivo() {
        return new Evento(1L, "Festa Junina",
                LocalDate.of(2026, 6, 10), LocalDate.of(2026, 6, 20), true, false);
    }

    @Nested
    class Criar {
        @Test
        void deveCriarTrimandoNome() {
            when(eventoRepositoryPort.salvar(any(Evento.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            Evento resultado = criarEventoUseCase.executar(criarCommand());

            ArgumentCaptor<Evento> captor = ArgumentCaptor.forClass(Evento.class);
            verify(eventoRepositoryPort).salvar(captor.capture());
            assertEquals("Festa Junina", captor.getValue().getNome());
            assertTrue(resultado.isAtivo());
        }

        @Test
        void naoDeveCriarComCommandNulo() {
            assertThrows(RegraDeNegocioException.class, () -> criarEventoUseCase.executar(null));
            verify(eventoRepositoryPort, never()).salvar(any());
        }

        @Test
        void naoDeveCriarSemNome() {
            CriarEventoCommand command = new CriarEventoCommand("  ",
                    LocalDate.of(2026, 6, 10), LocalDate.of(2026, 6, 20));

            assertThrows(RegraDeNegocioException.class, () -> criarEventoUseCase.executar(command));
            verify(eventoRepositoryPort, never()).salvar(any());
        }

        @Test
        void naoDeveCriarSemDatas() {
            CriarEventoCommand semInicio = new CriarEventoCommand("E", null, LocalDate.of(2026, 6, 20));
            CriarEventoCommand semFim = new CriarEventoCommand("E", LocalDate.of(2026, 6, 10), null);

            assertThrows(RegraDeNegocioException.class, () -> criarEventoUseCase.executar(semInicio));
            assertThrows(RegraDeNegocioException.class, () -> criarEventoUseCase.executar(semFim));
        }
    }

    @Nested
    class Atualizar {
        @Test
        void deveAtualizarPreservandoIdEFlags() {
            AtualizarEventoCommand command = new AtualizarEventoCommand(" Natal ",
                    LocalDate.of(2026, 12, 20), LocalDate.of(2026, 12, 25));
            when(eventoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(eventoAtivo()));
            when(eventoRepositoryPort.atualizar(any(Evento.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            Evento resultado = atualizarEventoUseCase.executar(1L, command);

            ArgumentCaptor<Evento> captor = ArgumentCaptor.forClass(Evento.class);
            verify(eventoRepositoryPort).atualizar(captor.capture());
            assertEquals(1L, captor.getValue().getId());
            assertEquals("Natal", captor.getValue().getNome());
            assertEquals(resultado.getNome(), "Natal");
        }

        @Test
        void naoDeveAtualizarQuandoNaoEncontrado() {
            AtualizarEventoCommand command = new AtualizarEventoCommand("Natal",
                    LocalDate.of(2026, 12, 20), LocalDate.of(2026, 12, 25));
            when(eventoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.empty());

            assertThrows(RegraDeNegocioException.class, () -> atualizarEventoUseCase.executar(1L, command));
            verify(eventoRepositoryPort, never()).atualizar(any());
        }

        @Test
        void naoDeveAtualizarComCommandNulo() {
            when(eventoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(eventoAtivo()));

            assertThrows(RegraDeNegocioException.class, () -> atualizarEventoUseCase.executar(1L, null));
        }
    }

    @Nested
    class DeletarEStatus {
        @Test
        void deveDeletarQuandoExistenteENaoDeletado() {
            when(eventoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(eventoAtivo()));

            deletarEventoUseCase.executar(1L);

            verify(eventoRepositoryPort).deletar(1L);
        }

        @Test
        void naoDeveDeletarQuandoNaoEncontrado() {
            when(eventoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.empty());

            assertThrows(RegraDeNegocioException.class, () -> deletarEventoUseCase.executar(1L));
            verify(eventoRepositoryPort, never()).deletar(any());
        }

        @Test
        void naoDeveDeletarQuandoJaRemovido() {
            Evento removido = new Evento(1L, "E",
                    LocalDate.of(2026, 6, 10), LocalDate.of(2026, 6, 20), false, true);
            when(eventoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(removido));

            assertThrows(RegraDeNegocioException.class, () -> deletarEventoUseCase.executar(1L));
            verify(eventoRepositoryPort, never()).deletar(any());
        }

        @Test
        void deveAlterarStatusDelegandoAoPort() {
            Evento atualizado = new Evento(1L, "Festa Junina",
                    LocalDate.of(2026, 6, 10), LocalDate.of(2026, 6, 20), false, false);
            when(eventoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(eventoAtivo()));
            when(eventoRepositoryPort.alterarStatus(1L, false)).thenReturn(atualizado);

            assertEquals(false, alterarStatusEventoUseCase.executar(1L, false).isAtivo());
        }

        @Test
        void naoDeveAlterarStatusDeEventoRemovido() {
            Evento removido = new Evento(1L, "E",
                    LocalDate.of(2026, 6, 10), LocalDate.of(2026, 6, 20), false, true);
            when(eventoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(removido));

            assertThrows(RegraDeNegocioException.class,
                    () -> alterarStatusEventoUseCase.executar(1L, true));
            verify(eventoRepositoryPort, never()).alterarStatus(any(), anyBoolean());
        }

        @Test
        void deveListarAtivos() {
            when(eventoRepositoryPort.listarEventosAtivos()).thenReturn(List.of(eventoAtivo()));

            assertEquals(1, listarEventosAtivosUseCase.executar().size());
        }
    }
}
