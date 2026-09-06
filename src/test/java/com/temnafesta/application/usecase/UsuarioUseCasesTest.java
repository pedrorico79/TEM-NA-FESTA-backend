package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.CriarUsuarioCommand;
import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.Perfil;
import com.temnafesta.domain.model.Usuario;
import com.temnafesta.domain.ports.repository.PerfilRepositoryPort;
import com.temnafesta.domain.ports.repository.UsuarioRepositoryPort;
import com.temnafesta.infrastructure.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioUseCasesTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepositoryPort;
    @Mock
    private PerfilRepositoryPort perfilRepositoryPort;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private CriarUsuarioUseCase criarUsuarioUseCase;
    @InjectMocks
    private AtualizarUsuarioUseCase atualizarUsuarioUseCase;
    @InjectMocks
    private DeletarUsuarioUseCase deletarUsuarioUseCase;
    @InjectMocks
    private ListarUsuarioUseCase listarUsuarioUseCase;
    @InjectMocks
    private AutenticarUsuarioUseCase autenticarUsuarioUseCase;

    private static Perfil perfil() {
        return new Perfil(1L, "ADMIN", "Administrador");
    }

    private static Usuario usuarioAtivo() {
        return new Usuario(1L, "Ana", "ana@mail.com", "hash", true, false,
                LocalDateTime.now(), perfil());
    }

    @Nested
    class Criar {
        @Test
        void deveCriarComSenhaCriptografadaETrim() {
            CriarUsuarioCommand command = new CriarUsuarioCommand("  Ana  ", "  ana@mail.com  ", "123456", 1L);
            when(usuarioRepositoryPort.buscarPorEmail("  ana@mail.com  ")).thenReturn(Optional.empty());
            when(perfilRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(perfil()));
            when(passwordEncoder.encode("123456")).thenReturn("hash");
            when(usuarioRepositoryPort.salvar(any(Usuario.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            Usuario resultado = criarUsuarioUseCase.executar(command);

            ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
            verify(usuarioRepositoryPort).salvar(captor.capture());
            assertEquals("Ana", captor.getValue().getNome());
            assertEquals("ana@mail.com", captor.getValue().getEmail());
            assertEquals("hash", resultado.getSenha());
            assertTrue(resultado.isAtivo());
        }

        @Test
        void naoDeveCriarComCommandNulo() {
            assertThrows(RegraDeNegocioException.class, () -> criarUsuarioUseCase.executar(null));
            verify(usuarioRepositoryPort, never()).salvar(any());
        }

        @Test
        void naoDeveCriarSemNomeEmailOuSenha() {
            assertThrows(RegraDeNegocioException.class, () -> criarUsuarioUseCase.executar(
                    new CriarUsuarioCommand(" ", "a@mail.com", "123", 1L)));
            assertThrows(RegraDeNegocioException.class, () -> criarUsuarioUseCase.executar(
                    new CriarUsuarioCommand("Ana", " ", "123", 1L)));
            assertThrows(RegraDeNegocioException.class, () -> criarUsuarioUseCase.executar(
                    new CriarUsuarioCommand("Ana", "a@mail.com", null, 1L)));
            verify(usuarioRepositoryPort, never()).salvar(any());
        }

        @Test
        void naoDeveCriarComEmailDuplicado() {
            CriarUsuarioCommand command = new CriarUsuarioCommand("Ana", "ana@mail.com", "123456", 1L);
            when(usuarioRepositoryPort.buscarPorEmail("ana@mail.com"))
                    .thenReturn(Optional.of(usuarioAtivo()));

            assertThrows(RegraDeNegocioException.class, () -> criarUsuarioUseCase.executar(command));
            verify(usuarioRepositoryPort, never()).salvar(any());
        }

        @Test
        void naoDeveCriarComPerfilInexistente() {
            CriarUsuarioCommand command = new CriarUsuarioCommand("Ana", "ana@mail.com", "123456", 99L);
            when(usuarioRepositoryPort.buscarPorEmail("ana@mail.com")).thenReturn(Optional.empty());
            when(perfilRepositoryPort.buscarPorId(99L)).thenReturn(Optional.empty());

            assertThrows(IllegalArgumentException.class, () -> criarUsuarioUseCase.executar(command));
            verify(usuarioRepositoryPort, never()).salvar(any());
        }
    }

    @Nested
    class Atualizar {
        @Test
        void deveAtualizarPreservandoSenhaEFlags() {
            when(usuarioRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(usuarioAtivo()));
            when(perfilRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(perfil()));
            when(usuarioRepositoryPort.atualizar(any(Usuario.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            Usuario resultado = atualizarUsuarioUseCase.executar(1L, " Ana Paula ", " ana2@mail.com ", 1L);

            assertEquals("Ana Paula", resultado.getNome());
            assertEquals("ana2@mail.com", resultado.getEmail());
            assertEquals("hash", resultado.getSenha());
        }

        @Test
        void naoDeveAtualizarQuandoNaoEncontrado() {
            when(usuarioRepositoryPort.buscarPorId(1L)).thenReturn(Optional.empty());

            assertThrows(RegraDeNegocioException.class,
                    () -> atualizarUsuarioUseCase.executar(1L, "Ana", "a@mail.com", 1L));
            verify(usuarioRepositoryPort, never()).atualizar(any());
        }

        @Test
        void naoDeveAtualizarSemNomeOuEmail() {
            when(usuarioRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(usuarioAtivo()));

            assertThrows(RegraDeNegocioException.class,
                    () -> atualizarUsuarioUseCase.executar(1L, " ", "a@mail.com", 1L));
            assertThrows(RegraDeNegocioException.class,
                    () -> atualizarUsuarioUseCase.executar(1L, "Ana", null, 1L));
        }
    }

    @Nested
    class DeletarEListar {
        @Test
        void deveDeletarQuandoExistenteENaoRemovido() {
            when(usuarioRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(usuarioAtivo()));

            deletarUsuarioUseCase.executar(1L);

            verify(usuarioRepositoryPort).deletar(1L);
        }

        @Test
        void naoDeveDeletarQuandoNaoEncontrado() {
            when(usuarioRepositoryPort.buscarPorId(1L)).thenReturn(Optional.empty());

            assertThrows(RegraDeNegocioException.class, () -> deletarUsuarioUseCase.executar(1L));
            verify(usuarioRepositoryPort, never()).deletar(any());
        }

        @Test
        void naoDeveDeletarQuandoJaRemovido() {
            Usuario removido = new Usuario(1L, "Ana", "a@mail.com", "hash",
                    false, true, LocalDateTime.now(), perfil());
            when(usuarioRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(removido));

            assertThrows(RegraDeNegocioException.class, () -> deletarUsuarioUseCase.executar(1L));
            verify(usuarioRepositoryPort, never()).deletar(any());
        }

        @Test
        void deveListarPaginadoDelegandoAoPort() {
            Page<Usuario> pagina = new PageImpl<>(List.of(usuarioAtivo()));
            when(usuarioRepositoryPort.listarPorNomePaginado("ana", Pageable.unpaged())).thenReturn(pagina);

            assertEquals(1, listarUsuarioUseCase.executar("ana", Pageable.unpaged()).getTotalElements());
        }
    }

    @Nested
    class Autenticar {
        @Test
        void deveRetornarTokenQuandoCredenciaisValidas() {
            when(usuarioRepositoryPort.buscarPorEmail("ana@mail.com"))
                    .thenReturn(Optional.of(usuarioAtivo()));
            when(passwordEncoder.matches("123", "hash")).thenReturn(true);
            when(jwtTokenProvider.gerarToken("ana@mail.com", "ADMIN", false)).thenReturn("token");

            assertEquals("token", autenticarUsuarioUseCase.executar("ana@mail.com", "123", false));
        }

        @Test
        void naoDeveAutenticarComEmailDesconhecido() {
            when(usuarioRepositoryPort.buscarPorEmail("x@mail.com")).thenReturn(Optional.empty());

            assertThrows(IllegalArgumentException.class,
                    () -> autenticarUsuarioUseCase.executar("x@mail.com", "123", false));
        }

        @Test
        void naoDeveAutenticarComSenhaInvalida() {
            when(usuarioRepositoryPort.buscarPorEmail("ana@mail.com"))
                    .thenReturn(Optional.of(usuarioAtivo()));
            when(passwordEncoder.matches("errada", "hash")).thenReturn(false);

            assertThrows(IllegalArgumentException.class,
                    () -> autenticarUsuarioUseCase.executar("ana@mail.com", "errada", false));
        }

        @Test
        void naoDeveAutenticarUsuarioInativoOuDeletado() {
            Usuario inativo = new Usuario(1L, "Ana", "a@mail.com", "hash",
                    false, false, LocalDateTime.now(), perfil());
            Usuario deletado = new Usuario(1L, "Ana", "a@mail.com", "hash",
                    true, true, LocalDateTime.now(), perfil());
            when(usuarioRepositoryPort.buscarPorEmail("a@mail.com"))
                    .thenReturn(Optional.of(inativo)).thenReturn(Optional.of(deletado));

            assertThrows(IllegalStateException.class,
                    () -> autenticarUsuarioUseCase.executar("a@mail.com", "123", false));
            assertThrows(IllegalStateException.class,
                    () -> autenticarUsuarioUseCase.executar("a@mail.com", "123", false));
        }
    }
}
