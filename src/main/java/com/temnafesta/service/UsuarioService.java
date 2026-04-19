package com.temnafesta.service;

import com.temnafesta.dto.usuario.UsuarioLoginDto;
import com.temnafesta.dto.usuario.UsuarioTokenDto;
import com.temnafesta.exception.usuario.UsuarioJaExiste;
import com.temnafesta.exception.usuario.UsuarioNaoEncontrado;
import com.temnafesta.mapper.UsuarioMapper;
import com.temnafesta.model.Usuario;
import com.temnafesta.repository.UsuarioRepository;
import com.temnafesta.security.GerenciadorTokenJwt;
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

    // Injeção via construtor (Boa prática)
    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          GerenciadorTokenJwt gerenciadorTokenJwt,
                          AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.gerenciadorTokenJwt = gerenciadorTokenJwt;
        this.authenticationManager = authenticationManager;
    }

    public void criar(Usuario novoUsuario) {

        if (usuarioRepository.existsByEmail(novoUsuario.getEmail())) {
            throw new UsuarioJaExiste(novoUsuario.getEmail());
        }

        // Criptografa a senha antes de salvar
        String senhaCriptografada = passwordEncoder.encode(novoUsuario.getSenha());
        novoUsuario.setSenha(senhaCriptografada);

        this.usuarioRepository.save(novoUsuario);
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

    public Usuario buscarPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontrado(id));
    }

    public void deletar(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNaoEncontrado(id);
        }
        usuarioRepository.deleteById(id);
    }

    public Usuario atualizar(Integer id, Usuario usuarioAtualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontrado(id));

        if (!usuarioExistente.getEmail().equals(usuarioAtualizado.getEmail())
                && usuarioRepository.existsByEmail(usuarioAtualizado.getEmail())) {
            throw new UsuarioJaExiste(usuarioAtualizado.getEmail());
        }
        usuarioExistente.setNome(usuarioAtualizado.getNome());
        usuarioExistente.setEmail(usuarioAtualizado.getEmail());
        usuarioExistente.setAtivo(usuarioAtualizado.getAtivo());

        // Só atualiza a senha se ela for enviada e criptografa
        if (usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isBlank()) {
            usuarioExistente.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
        }

        usuarioExistente.setPerfil(usuarioAtualizado.getPerfil());

        return usuarioRepository.save(usuarioExistente);
    }

    public void atualizarSenha(Integer id, String novaSenha) {
        Usuario usuario = buscarPorId(id);

        // Criptografa a nova senha antes de salvar
        String senhaCriptografada = passwordEncoder.encode(novaSenha);
        usuario.setSenha(senhaCriptografada);

        usuarioRepository.save(usuario);
    }
}