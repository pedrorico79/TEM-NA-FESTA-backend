package com.temnafesta.presentation.controller;

import com.temnafesta.application.dto.AtualizarLembreteCommand;
import com.temnafesta.application.dto.CriarLembreteCommand;
import com.temnafesta.application.usecase.AtualizarLembreteUseCase;
import com.temnafesta.application.usecase.CriarLembreteUseCase;
import com.temnafesta.application.usecase.DeletarLembreteUseCase;
import com.temnafesta.application.usecase.ListarLembretesUsuarioUseCase;
import com.temnafesta.domain.model.Lembrete;
import com.temnafesta.infrastructure.security.user.UsuarioAutenticado;
import com.temnafesta.presentation.dto.LembreteRequestDto;
import com.temnafesta.presentation.dto.LembreteResponseDto;
import com.temnafesta.presentation.mapper.ConsultasPresentationMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LembreteControllerTest {

    @Mock
    private ListarLembretesUsuarioUseCase listarLembretesUsuarioUseCase;

    @Mock
    private CriarLembreteUseCase criarLembreteUseCase;

    @Mock
    private DeletarLembreteUseCase deletarLembreteUseCase;

    @Mock
    private AtualizarLembreteUseCase atualizarLembreteUseCase;

    @Mock
    private ConsultasPresentationMapper mapper;

    @InjectMocks
    private LembreteController lembreteController;

    @Test
    void deveRetornarLembretesMapeados() {
        Lembrete lembrete = new Lembrete(1L, "Lembrete 1", LocalDate.now(), LocalDate.now().plusDays(1), 10L);
        LembreteResponseDto response = new LembreteResponseDto(1L, "Lembrete 1", LocalDate.now(), LocalDate.now().plusDays(1), 10L);
        when(listarLembretesUsuarioUseCase.executar(10L)).thenReturn(List.of(lembrete));
        when(mapper.toResponse(lembrete)).thenReturn(response);

        ResponseEntity<List<LembreteResponseDto>> resposta = lembreteController.listarPorUsuario(usuarioAutenticado(10L));

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertNotNull(resposta.getBody());
        assertEquals(List.of(response), resposta.getBody());
    }

    @Test
    void deveCadastrarLembreteERetornarStatusCriado() {
        LembreteRequestDto request = new LembreteRequestDto("Novo lembrete", LocalDate.now().plusDays(1));
        CriarLembreteCommand command = new CriarLembreteCommand("Novo lembrete", LocalDate.now().plusDays(1), 10L);
        Lembrete lembreteSalvo = new Lembrete(1L, "Novo lembrete", LocalDate.now(), LocalDate.now().plusDays(1), 10L);
        LembreteResponseDto response = new LembreteResponseDto(1L, "Novo lembrete", LocalDate.now(), LocalDate.now().plusDays(1), 10L);
        when(mapper.toCommand(request, 10L)).thenReturn(command);
        when(criarLembreteUseCase.executar(command)).thenReturn(lembreteSalvo);
        when(mapper.toResponse(lembreteSalvo)).thenReturn(response);

        ResponseEntity<LembreteResponseDto> resposta = lembreteController.criar(request, usuarioAutenticado(10L));

        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertEquals(response, resposta.getBody());
    }

    @Test
    void deveAtualizarLembreteERetornarStatusOk() {
        LembreteRequestDto request = new LembreteRequestDto("Lembrete atualizado", LocalDate.of(2024, 3, 1));
        AtualizarLembreteCommand command = new AtualizarLembreteCommand(1L, "Lembrete atualizado", LocalDate.of(2024, 3, 1), 10L);
        Lembrete lembreteAtualizado = new Lembrete(1L, "Lembrete atualizado", LocalDate.of(2024, 1, 15), LocalDate.of(2024, 3, 1), 10L);
        LembreteResponseDto response = new LembreteResponseDto(1L, "Lembrete atualizado", LocalDate.of(2024, 1, 15), LocalDate.of(2024, 3, 1), 10L);
        when(mapper.toCommand(request, 1L, 10L)).thenReturn(command);
        when(atualizarLembreteUseCase.executar(command)).thenReturn(lembreteAtualizado);
        when(mapper.toResponse(lembreteAtualizado)).thenReturn(response);

        ResponseEntity<LembreteResponseDto> resposta = lembreteController.atualizar(1L, request, usuarioAutenticado(10L));

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(response, resposta.getBody());
    }

    @Test
    void deveExcluirLembreteERetornarStatusSemConteudo() {
        ResponseEntity<Void> resposta = lembreteController.deletar(1L, usuarioAutenticado(10L));

        verify(deletarLembreteUseCase).executar(1L, 10L);
        assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());
        assertNull(resposta.getBody());
    }

    private UsuarioAutenticado usuarioAutenticado(Long id) {
        return new UsuarioAutenticado(id, "Usuario Teste", "usuario@email.com", "senha", true, List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }
}