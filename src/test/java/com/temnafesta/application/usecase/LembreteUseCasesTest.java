package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.CriarLembreteCommand;
import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.Lembrete;
import com.temnafesta.domain.ports.repository.LembreteRepositoryPort;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LembreteUseCasesTest {

    @Mock
    private LembreteRepositoryPort lembreteRepositoryPort;

    @InjectMocks
    private CriarLembreteUseCase criarLembreteUseCase;
    @InjectMocks
    private DeletarLembreteUseCase deletarLembreteUseCase;
    @InjectMocks
    private ListarLembretesUsuarioUseCase listarLembretesUsuarioUseCase;

    private static Lembrete lembrete(Long usuarioId) {
        return new Lembrete(1L, "Comprar ovos", LocalDate.now(), LocalDate.now().plusDays(1), usuarioId);
    }

    @Nested
    class Criar {
        @Test
        void deveCriarComDataCriacaoDeHoje() {
            CriarLembreteCommand command = new CriarLembreteCommand(
                    "Comprar ovos", LocalDate.now().plusDays(1), 10L);
            when(lembreteRepositoryPort.salvar(any(Lembrete.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            Lembrete resultado = criarLembreteUseCase.executar(command);

            ArgumentCaptor<Lembrete> captor = ArgumentCaptor.forClass(Lembrete.class);
            verify(lembreteRepositoryPort).salvar(captor.capture());
            assertEquals(LocalDate.now(), captor.getValue().getDataCriacao());
            assertNotNull(resultado);
        }
    }

    @Nested
    class Deletar {
        @Test
        void deveDeletarQuandoDono() {
            when(lembreteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(lembrete(10L)));

            deletarLembreteUseCase.executar(1L, 10L);

            verify(lembreteRepositoryPort).deletarPorId(1L);
        }

        @Test
        void naoDeveDeletarQuandoNaoEncontrado() {
            when(lembreteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.empty());

            assertThrows(RegraDeNegocioException.class, () -> deletarLembreteUseCase.executar(1L, 10L));
            verify(lembreteRepositoryPort, never()).deletarPorId(any());
        }

        @Test
        void naoDeveDeletarDeOutroUsuario() {
            when(lembreteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(lembrete(10L)));

            RegraDeNegocioException ex = assertThrows(RegraDeNegocioException.class,
                    () -> deletarLembreteUseCase.executar(1L, 99L));
            assertTrue(ex.getMessage().contains("permissão"));
            verify(lembreteRepositoryPort, never()).deletarPorId(any());
        }
    }

    @Nested
    class Listar {
        @Test
        void deveListarPorUsuario() {
            when(lembreteRepositoryPort.listarPorUsuarioId(10L)).thenReturn(List.of(lembrete(10L)));

            assertEquals(1, listarLembretesUsuarioUseCase.executar(10L).size());
        }

        @Test
        void deveRetornarVazioSemErro() {
            when(lembreteRepositoryPort.listarPorUsuarioId(10L)).thenReturn(List.of());

            assertTrue(listarLembretesUsuarioUseCase.executar(10L).isEmpty());
        }
    }
}
