package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.AtualizarLembreteCommand;
import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.Lembrete;
import com.temnafesta.domain.ports.repository.LembreteRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtualizarLembreteUseCaseTest {

    @Mock
    private LembreteRepositoryPort lembreteRepositoryPort;

    @InjectMocks
    private AtualizarLembreteUseCase atualizarLembreteUseCase;

    @Test
    void deveAtualizarDescricaoEDataLimitePreservandoIdDataCriacaoEUsuarioId() {
        LocalDate dataCriacao = LocalDate.of(2024, 1, 15);
        Lembrete existente = new Lembrete(1L, "Lembrete antigo", dataCriacao, LocalDate.of(2024, 2, 1), 10L);
        AtualizarLembreteCommand command = new AtualizarLembreteCommand(
                1L,
                "  Lembrete atualizado  ",
                LocalDate.of(2024, 3, 1),
                10L);
        when(lembreteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(existente));
        when(lembreteRepositoryPort.salvar(any(Lembrete.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Lembrete resultado = atualizarLembreteUseCase.executar(command);

        ArgumentCaptor<Lembrete> captor = ArgumentCaptor.forClass(Lembrete.class);
        verify(lembreteRepositoryPort).salvar(captor.capture());
        assertEquals(1L, resultado.getId());
        assertEquals("Lembrete atualizado", captor.getValue().getDescricao());
        assertEquals(LocalDate.of(2024, 3, 1), captor.getValue().getDataLimite());
        assertEquals(dataCriacao, captor.getValue().getDataCriacao());
        assertEquals(10L, captor.getValue().getUsuarioId());
    }

    @Test
    void deveLancarExcecaoQuandoLembreteNaoExistir() {
        AtualizarLembreteCommand command = new AtualizarLembreteCommand(99L, "Nova desc", LocalDate.now(), 10L);
        when(lembreteRepositoryPort.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThrows(RegraDeNegocioException.class,
                () -> atualizarLembreteUseCase.executar(command));
        verify(lembreteRepositoryPort, never()).salvar(any(Lembrete.class));
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoForDonoDoLembrete() {
        Lembrete existente = new Lembrete(1L, "Lembrete antigo", LocalDate.now(), LocalDate.now().plusDays(1), 10L);
        AtualizarLembreteCommand command = new AtualizarLembreteCommand(1L, "Nova desc", LocalDate.now().plusDays(1), 20L);
        when(lembreteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(existente));

        assertThrows(RegraDeNegocioException.class,
                () -> atualizarLembreteUseCase.executar(command));
        verify(lembreteRepositoryPort, never()).salvar(any(Lembrete.class));
    }

    @Test
    void devePermitirAtualizarApenasDescricaoMantendoDataLimiteOriginal() {
        LocalDate dataCriacao = LocalDate.of(2024, 1, 15);
        LocalDate dataLimiteOriginal = LocalDate.of(2024, 2, 1);
        Lembrete existente = new Lembrete(1L, "Lembrete antigo", dataCriacao, dataLimiteOriginal, 10L);
        AtualizarLembreteCommand command = new AtualizarLembreteCommand(
                1L,
                "Nova descricao",
                dataLimiteOriginal,
                10L);
        when(lembreteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(existente));
        when(lembreteRepositoryPort.salvar(any(Lembrete.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Lembrete resultado = atualizarLembreteUseCase.executar(command);

        assertEquals("Nova descricao", resultado.getDescricao());
        assertEquals(dataLimiteOriginal, resultado.getDataLimite());
        assertEquals(dataCriacao, resultado.getDataCriacao());
        assertEquals(10L, resultado.getUsuarioId());
    }
}