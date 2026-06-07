package com.temnafesta.service;

import com.temnafesta.dto.usuario.UsuarioAtualizacaoDto;
import com.temnafesta.dto.usuario.UsuarioCriacaoDto;
import com.temnafesta.dto.usuario.UsuarioLoginDto;
import com.temnafesta.dto.usuario.UsuarioTokenDto;
import com.temnafesta.exception.usuario.UsuarioJaExiste;
import com.temnafesta.exception.usuario.UsuarioNaoEncontrado;
import com.temnafesta.mapper.UsuarioMapper;
import com.temnafesta.model.Perfil;
import com.temnafesta.model.Usuario;
import com.temnafesta.repository.PerfilRepository;
import com.temnafesta.repository.UsuarioRepository;
import com.temnafesta.security.GerenciadorTokenJwt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final GerenciadorTokenJwt gerenciadorTokenJwt;
    private final AuthenticationManager authenticationManager;
    private final PerfilRepository perfilRepository;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          GerenciadorTokenJwt gerenciadorTokenJwt,
                          AuthenticationManager authenticationManager, PerfilRepository perfilRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.gerenciadorTokenJwt = gerenciadorTokenJwt;
        this.authenticationManager = authenticationManager;
        this.perfilRepository = perfilRepository;
    }

    public void criar(UsuarioCriacaoDto dto) {
        // 1. Busca o perfil pelo ID que veio do JSON
        Perfil perfil = perfilRepository.findById(dto.getPerfilId())
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado"));

        // 2. Agora sim, chama o Mapper passando os dois!
        Usuario novoUsuario = UsuarioMapper.toEntity(dto, perfil);

        // 3. Criptografa a senha e salva
        novoUsuario.setSenha(passwordEncoder.encode(novoUsuario.getSenha()));
        usuarioRepository.save(novoUsuario);
    }

    public UsuarioTokenDto autenticar(UsuarioLoginDto loginDto) {
        // 1. Cria o objeto de autenticação com as credenciais do DTO
        final UsernamePasswordAuthenticationToken credentials =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getSenha());

        // 2. O Spring Security tenta autenticar (valida a senha criptografada)
        final Authentication authentication = this.authenticationManager.authenticate(credentials);

        // 3. Busca o usuário no banco para pegar os dados completos
        Usuario usuarioAutenticado = usuarioRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(404, "Usuário não encontrado", null));

        // 4. Salva a autenticação no contexto do Spring
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 5. Gera o Token JWT
        final String token = gerenciadorTokenJwt.generateToken(authentication);

        // 6. Retorna o DTO de Token usando o Mapper
        return UsuarioMapper.toTokenDto(usuarioAutenticado, token);
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }
    public List<Usuario> listarAtivos() {
        return usuarioRepository.findByIsAtivoTrue();
    }

    public Usuario buscarPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontrado(id));
    }

    public void desativar(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                        .orElseThrow(() -> new UsuarioNaoEncontrado(id));
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    public void reativar(Integer id){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontrado(id));
        usuario.setAtivo(true);
        usuarioRepository.save(usuario);
    }

    public Usuario atualizar(Integer id, UsuarioAtualizacaoDto dto) {
        // 1. Verifica se o usuário existe
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // 2. Busca o novo perfil pelo ID
        Perfil perfil = perfilRepository.findById(dto.getPerfilId())
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado"));

        // 3. Chama o Mapper
        Usuario dadosAtualizados = UsuarioMapper.toEntity(dto, perfil);

        // 4. Mantém o ID original e a Senha (já que atualização de dados não costuma alterar a senha)
        dadosAtualizados.setId(usuarioExistente.getId());
        dadosAtualizados.setSenha(usuarioExistente.getSenha());
        dadosAtualizados.setDataCriacao(usuarioExistente.getDataCriacao());

        // 5. Salva no banco
        return usuarioRepository.save(dadosAtualizados);
    }

    public void atualizarSenha(Integer id, String novaSenha) {
        Usuario usuario = buscarPorId(id);

        // Criptografa a nova senha antes de salvar
        String senhaCriptografada = passwordEncoder.encode(novaSenha);
        usuario.setSenha(senhaCriptografada);

        usuarioRepository.save(usuario);
    }

    public Page<Usuario> buscarPorNome(
            String nome,
            Integer page
    ) {

        Pageable pageable = PageRequest.of(page, 10);

        return usuarioRepository
                .findByNomeContainingIgnoreCaseAndIsAtivoTrue(
                        nome,
                        pageable
                );
    }
}