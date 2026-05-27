package com.temnafesta.service;

import com.temnafesta.dto.usuario.UsuarioLoginDto;
import com.temnafesta.dto.usuario.UsuarioTokenDto;
import com.temnafesta.exception.usuario.UsuarioJaExiste;
import com.temnafesta.model.Perfil;
import com.temnafesta.model.Usuario;
import com.temnafesta.repository.UsuarioRepository;
import com.temnafesta.security.GerenciadorTokenJwt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Usuário")
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private GerenciadorTokenJwt gerenciadorTokenJwt;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UsuarioService usuarioService;

    @Nested
    @DisplayName("Cenários do método criar")
    class CriarTests{

        @Test
        @DisplayName("Deve criar usuário corretamente")
        void deveCriarUsuarioCorretamente() {

            Usuario novoUsuario = new Usuario();
            novoUsuario.setEmail("felipe@exemplo.com");
            novoUsuario.setSenha("senha123");

            Mockito.when(usuarioRepository.existsByEmail("felipe@exemplo.com"))
                    .thenReturn(false);

            Mockito.when(passwordEncoder.encode("senha123"))
                    .thenReturn("senhaCriptografadaXpto");

            usuarioService.criar(novoUsuario);

            Assertions.assertEquals("felipe@exemplo.com", novoUsuario.getEmail());
            Assertions.assertEquals("senhaCriptografadaXpto", novoUsuario.getSenha());

            Mockito.verify(usuarioRepository).save(novoUsuario);
        }

        @Test
        @DisplayName("Deve lançar exceção UsuarioJaExiste caso o e-mail já esteja cadastrado")
        void deveLancarExcecaoQuandoEmailJaExiste() {
            Usuario novoUsuario = new Usuario();
            novoUsuario.setEmail("felipe@exemplo.com");
            novoUsuario.setSenha("senha123");

            Mockito.when(usuarioRepository.existsByEmail("felipe@exemplo.com"))
                    .thenReturn(true);

            Assertions.assertThrows(UsuarioJaExiste.class, () -> {
                usuarioService.criar(novoUsuario);
            });
        }
    }

    @Nested
    @DisplayName("Cenários do método autenticar")
    class AutenticarTests{
        @Test
        @DisplayName("Deve autenticar o usuário com sucesso e retornar o token JWT")
        void deveAutenticarComSucesso() {
            UsuarioLoginDto loginDto = new UsuarioLoginDto();
            loginDto.setEmail("vegeta@email.com");
            loginDto.setSenha("verme_insolente");

            Usuario usuarioDoBanco = new Usuario();
            usuarioDoBanco.setId(1);
            usuarioDoBanco.setNome("Vegeta");
            usuarioDoBanco.setEmail("vegeta@email.com");

            Authentication authenticationMock = Mockito.mock(Authentication.class);

            Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authenticationMock);

            Mockito.when(usuarioRepository.findByEmail("vegeta@email.com"))
                    .thenReturn(Optional.of(usuarioDoBanco));

            Mockito.when(gerenciadorTokenJwt.generateToken(authenticationMock))
                    .thenReturn("token-jwt-super-secreto");

            UsuarioTokenDto resultado = usuarioService.autenticar(loginDto);

            Assertions.assertNotNull(resultado);
            Assertions.assertEquals("Vegeta", resultado.getNome());
            Assertions.assertEquals("vegeta@email.com", resultado.getEmail());
            Assertions.assertEquals("token-jwt-super-secreto", resultado.getToken());
        }

        @Test
        @DisplayName("Deve lançar ResponseStatusException quando o usuário não for encontrado no banco")
        void deveLancarExcecaoQuandoUsuarioNaoExiste() {
            UsuarioLoginDto loginDto = new UsuarioLoginDto();
            loginDto.setEmail("inexistente@email.com");
            loginDto.setSenha("123456");

            Authentication authenticationMock = Mockito.mock(Authentication.class);

            Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authenticationMock);

            Mockito.when(usuarioRepository.findByEmail("inexistente@email.com"))
                    .thenReturn(Optional.empty());

            ResponseStatusException excecao = Assertions.assertThrows(ResponseStatusException.class, () -> {
                usuarioService.autenticar(loginDto);
            });

            Assertions.assertEquals(404, excecao.getStatusCode().value());
            Assertions.assertEquals("Usuário não encontrado", excecao.getReason());
        }
    }

    @Nested
    @DisplayName("Cenários do método listar")
    class ListarTests{
        @Test
        void listar() {
        }
    }

    @Nested
    @DisplayName("Cenários do método listar ativos")
    class ListarAtivosTests{
        @Test
        void listarAtivos() {
        }
    }

    @Nested
    @DisplayName("Cenários do método buscar por id")
    class BuscarPorIdTests{
        @Test
        void buscarPorId() {
        }
    }

    @Nested
    @DisplayName("Cenários do método desativar")
    class DesativarTests{
        @Test
        void desativar() {
        }
    }

    @Nested
    @DisplayName("Cenários do método reativar")
    class ReativarTests{
        @Test
        void reativar() {
        }
    }

    @Nested
    @DisplayName("Cenários do método atualizar")
    class AtualizarTests{
        @Test
        void atualizar() {
        }
    }

    @Nested
    @DisplayName("Cenários do método atualizar senha")
    class AtualizarSenhaTests{
        @Test
        void atualizarSenha() {
        }
    }
}