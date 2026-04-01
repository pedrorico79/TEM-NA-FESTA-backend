package com.temnafesta.service;

import com.temnafesta.exception.perfil.PerfilNaoEncontrado;
import com.temnafesta.exception.usuario.UsuarioNaoEncontrado; // Supondo que a exception exista
import com.temnafesta.model.Perfil;
import com.temnafesta.model.Usuario;
import com.temnafesta.repository.PerfilRepository;
import com.temnafesta.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, PerfilRepository perfilRepository) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
    }

    public Usuario criar(Usuario usuario, Integer perfilId) {
        Perfil perfilEntidade = perfilRepository.findById(perfilId)
                .orElseThrow(() -> new PerfilNaoEncontrado(perfilId));
        usuario.setPerfil(perfilEntidade);
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontrado(id));
    }

    public Usuario atualizar(Integer id, Usuario usuarioAtualizado, Integer perfilId) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNaoEncontrado(id);
        }
        Perfil perfilEntidade = perfilRepository.findById(perfilId)
                .orElseThrow(() -> new PerfilNaoEncontrado(perfilId));

        usuarioAtualizado.setPerfil(perfilEntidade);
        // usuarioAtualizado.setId(id); // Descomente se sua Entidade Usuario tiver setId
        return usuarioRepository.save(usuarioAtualizado);
    }

    public void deletar(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNaoEncontrado(id);
        }
        usuarioRepository.deleteById(id);
    }
}
