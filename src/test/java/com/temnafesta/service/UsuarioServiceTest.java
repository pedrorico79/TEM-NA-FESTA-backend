package com.temnafesta.service;

import com.temnafesta.dto.usuario.UsuarioAtualizacaoDto;
import com.temnafesta.dto.usuario.UsuarioCriacaoDto;
import com.temnafesta.dto.usuario.UsuarioLoginDto;
import com.temnafesta.dto.usuario.UsuarioTokenDto;
import com.temnafesta.exception.usuario.UsuarioNaoEncontrado;
import com.temnafesta.model.Perfil;
import com.temnafesta.model.Usuario;
import com.temnafesta.repository.PerfilRepository;
import com.temnafesta.repository.UsuarioRepository;
import com.temnafesta.security.GerenciadorTokenJwt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Usuário")
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PerfilRepository perfilRepository;

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
    class CriarTests {

        @Test
        @DisplayName("Deve criar usuário corretamente")
        void deveCriarUsuarioCorretamente() {
            UsuarioCriacaoDto dto = new UsuarioCriacaoDto();
            dto.setNome("Felipe");
            dto.setEmail("felipe@exemplo.com");
            dto.setSenha("senha123");
            dto.setPerfilId(1);

            Perfil perfil = new Perfil();
            perfil.setId(1);
            perfil.setNome("ADMIN");

            when(perfilRepository.findById(1)).thenReturn(Optional.of(perfil));
            when(passwordEncoder.encode("senha123")).thenReturn("senhaCriptografadaXpto");

            usuarioService.criar(dto);

            ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
            verify(usuarioRepository).save(usuarioCaptor.capture());

            Usuario usuarioSalvo = usuarioCaptor.getValue();
            assertEquals("Felipe", usuarioSalvo.getNome());
            assertEquals("felipe@exemplo.com", usuarioSalvo.getEmail());
            assertEquals("senhaCriptografadaXpto", usuarioSalvo.getSenha());
            assertEquals(1, usuarioSalvo.getPerfil().getId());
        }

        @Test
        @DisplayName("Deve lançar exceção RuntimeException caso o perfil não exista")
        void deveLancarExcecaoQuandoPerfilNaoExiste() {
            UsuarioCriacaoDto dto = new UsuarioCriacaoDto();
            dto.setPerfilId(99);

            when(perfilRepository.findById(99)).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                usuarioService.criar(dto);
            });

            assertEquals("Perfil não encontrado", exception.getMessage());
            verify(usuarioRepository, never()).save(any(Usuario.class));
        }
    }

    @Nested
    @DisplayName("Cenários do método autenticar")
    class AutenticarTests {

        @Test
        @DisplayName("Deve autenticar o usuário com sucesso e retornar o token JWT")
        void deveAutenticarComSucesso() {
            UsuarioLoginDto loginDto = new UsuarioLoginDto();
            loginDto.setEmail("vegeta@email.com");
            loginDto.setSenha("verme_insolente");

            Perfil perfil = new Perfil();
            perfil.setId(1);
            perfil.setNome("ADMIN");

            Usuario usuarioDoBanco = new Usuario();
            usuarioDoBanco.setId(1);
            usuarioDoBanco.setNome("Vegeta");
            usuarioDoBanco.setEmail("vegeta@email.com");
            usuarioDoBanco.setPerfil(perfil);

            Authentication authenticationMock = mock(Authentication.class);

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authenticationMock);

            when(usuarioRepository.findByEmail("vegeta@email.com"))
                    .thenReturn(Optional.of(usuarioDoBanco));

            when(gerenciadorTokenJwt.generateToken(authenticationMock))
                    .thenReturn("token-jwt-super-secreto");

            UsuarioTokenDto resultado = usuarioService.autenticar(loginDto);

            assertNotNull(resultado);
            assertEquals("Vegeta", resultado.getNome());
            assertEquals("vegeta@email.com", resultado.getEmail());
            assertEquals("token-jwt-super-secreto", resultado.getToken());
            assertEquals(1, resultado.getPerfilId());
            assertEquals("ADMIN", resultado.getPerfilNome());
        }

        @Test
        @DisplayName("Deve lançar ResponseStatusException quando o usuário não for encontrado no banco")
        void deveLancarExcecaoQuandoUsuarioNaoExiste() {
            UsuarioLoginDto loginDto = new UsuarioLoginDto();
            loginDto.setEmail("inexistente@email.com");
            loginDto.setSenha("123456");

            Authentication authenticationMock = mock(Authentication.class);

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authenticationMock);

            when(usuarioRepository.findByEmail("inexistente@email.com"))
                    .thenReturn(Optional.empty());

            ResponseStatusException excecao = assertThrows(ResponseStatusException.class, () -> {
                usuarioService.autenticar(loginDto);
            });

            assertEquals(404, excecao.getStatusCode().value());
            assertEquals("Usuário não encontrado", excecao.getReason());
        }
    }

    @Nested
    @DisplayName("Cenários do método listar")
    class ListarTests {

        @Test
        @DisplayName("Deve retornar uma lista de usuários")
        void deveListarUsuarios() {
            List<Usuario> usuarios = List.of(new Usuario(), new Usuario());
            when(usuarioRepository.findAll()).thenReturn(usuarios);

            List<Usuario> resultado = usuarioService.listar();

            assertEquals(2, resultado.size());
            verify(usuarioRepository).findAll();
        }
    }

    @Nested
    @DisplayName("Cenários do método listar ativos")
    class ListarAtivosTests {

        @Test
        @DisplayName("Deve retornar uma lista de usuários ativos")
        void deveListarUsuariosAtivos() {
            List<Usuario> usuarios = List.of(new Usuario());
            when(usuarioRepository.findByIsAtivoTrue()).thenReturn(usuarios);

            List<Usuario> resultado = usuarioService.listarAtivos();

            assertEquals(1, resultado.size());
            verify(usuarioRepository).findByIsAtivoTrue();
        }
    }

    @Nested
    @DisplayName("Cenários do método buscar por id")
    class BuscarPorIdTests {

        @Test
        @DisplayName("Deve retornar usuário quando ID existir")
        void deveBuscarPorIdComSucesso() {
            Usuario usuario = new Usuario();
            usuario.setId(1);

            when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

            Usuario resultado = usuarioService.buscarPorId(1);

            assertEquals(1, resultado.getId());
        }

        @Test
        @DisplayName("Deve lançar UsuarioNaoEncontrado quando ID não existir")
        void deveLancarExcecaoAoBuscarIdInexistente() {
            when(usuarioRepository.findById(1)).thenReturn(Optional.empty());

            assertThrows(UsuarioNaoEncontrado.class, () -> usuarioService.buscarPorId(1));
        }
    }

    @Nested
    @DisplayName("Cenários do método desativar")
    class DesativarTests {

        @Test
        @DisplayName("Deve desativar o usuário corretamente")
        void deveDesativarUsuario() {
            Usuario usuario = new Usuario();
            usuario.setId(1);
            usuario.setAtivo(true);

            when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

            usuarioService.desativar(1);

            assertFalse(usuario.getAtivo());
            verify(usuarioRepository).save(usuario);
        }

        @Test
        @DisplayName("Deve lançar UsuarioNaoEncontrado ao tentar desativar usuário inexistente")
        void deveLancarExcecaoAoDesativarIdInexistente() {
            when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

            assertThrows(UsuarioNaoEncontrado.class, () -> usuarioService.desativar(99));
        }
    }

    @Nested
    @DisplayName("Cenários do método reativar")
    class ReativarTests {

        @Test
        @DisplayName("Deve reativar o usuário corretamente")
        void deveReativarUsuario() {
            Usuario usuario = new Usuario();
            usuario.setId(1);
            usuario.setAtivo(false);

            when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

            usuarioService.reativar(1);

            assertTrue(usuario.getAtivo());
            verify(usuarioRepository).save(usuario);
        }

        @Test
        @DisplayName("Deve lançar UsuarioNaoEncontrado ao tentar reativar usuário inexistente")
        void deveLancarExcecaoAoReativarIdInexistente() {
            when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

            assertThrows(UsuarioNaoEncontrado.class, () -> usuarioService.reativar(99));
        }
    }

    @Nested
    @DisplayName("Cenários do método atualizar")
    class AtualizarTests {

        @Test
        @DisplayName("Deve atualizar o usuário com sucesso")
        void deveAtualizarCorretamente() {
            UsuarioAtualizacaoDto dto = new UsuarioAtualizacaoDto();
            dto.setNome("Nome Atualizado");
            dto.setEmail("atualizado@email.com");
            dto.setAtivo(true);
            dto.setPerfilId(2);

            Usuario usuarioExistente = new Usuario();
            usuarioExistente.setId(1);
            usuarioExistente.setSenha("senhaManteve");

            Perfil perfil = new Perfil();
            perfil.setId(2);
            perfil.setNome("FUNCIONARIO");

            when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioExistente));
            when(perfilRepository.findById(2)).thenReturn(Optional.of(perfil));

            // Retorna o próprio objeto que foi passado para o save
            when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArguments()[0]);

            Usuario resultado = usuarioService.atualizar(1, dto);

            assertNotNull(resultado);
            assertEquals("Nome Atualizado", resultado.getNome());
            assertEquals("atualizado@email.com", resultado.getEmail());
            assertEquals("senhaManteve", resultado.getSenha());
            assertEquals(2, resultado.getPerfil().getId());

            verify(usuarioRepository).save(any(Usuario.class));
        }

        @Test
        @DisplayName("Deve lançar RuntimeException se usuário não for encontrado na atualização")
        void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
            UsuarioAtualizacaoDto dto = new UsuarioAtualizacaoDto();

            when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                usuarioService.atualizar(99, dto);
            });

            assertEquals("Usuário não encontrado", exception.getMessage());
            verify(perfilRepository, never()).findById(any());
        }

        @Test
        @DisplayName("Deve lançar RuntimeException se perfil não for encontrado na atualização")
        void deveLancarExcecaoQuandoPerfilNaoEncontrado() {
            UsuarioAtualizacaoDto dto = new UsuarioAtualizacaoDto();
            dto.setPerfilId(99);

            Usuario usuarioExistente = new Usuario();

            when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioExistente));
            when(perfilRepository.findById(99)).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                usuarioService.atualizar(1, dto);
            });

            assertEquals("Perfil não encontrado", exception.getMessage());
            verify(usuarioRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Cenários do método atualizar senha")
    class AtualizarSenhaTests {

        @Test
        @DisplayName("Deve atualizar a senha com sucesso")
        void deveAtualizarSenha() {
            Usuario usuario = new Usuario();
            usuario.setId(1);
            usuario.setSenha("senhaVelha");

            when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
            when(passwordEncoder.encode("senhaNova123")).thenReturn("novaCriptografada");

            usuarioService.atualizarSenha(1, "senhaNova123");

            assertEquals("novaCriptografada", usuario.getSenha());
            verify(usuarioRepository).save(usuario);
        }

        @Test
        @DisplayName("Deve lançar UsuarioNaoEncontrado ao tentar atualizar senha de id inexistente")
        void deveLancarExcecaoAoAtualizarSenhaDeIdInexistente() {
            when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

            assertThrows(UsuarioNaoEncontrado.class, () -> usuarioService.atualizarSenha(99, "senhaNova"));
            verify(passwordEncoder, never()).encode(any());
        }
    }
}