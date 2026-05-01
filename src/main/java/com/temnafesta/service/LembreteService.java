package com.temnafesta.service;

import com.temnafesta.exception.cliente.ClienteNaoEncontrado;
import com.temnafesta.exception.endereco.EnderecoNaoEncontrado;
import com.temnafesta.exception.lembrete.LembreteNaoEncontrado;
import com.temnafesta.exception.usuario.UsuarioNaoEncontrado;
import com.temnafesta.model.Cliente;
import com.temnafesta.model.Endereco;
import com.temnafesta.model.Lembrete;
import com.temnafesta.model.Usuario;
import com.temnafesta.repository.LembreteRepository;
import com.temnafesta.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LembreteService {

    private final LembreteRepository lembreteRepository;
    private final UsuarioRepository usuarioRepository;

    public LembreteService(LembreteRepository lembreteRepository, UsuarioRepository usuarioRepository) {
        this.lembreteRepository = lembreteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Lembrete criar(Lembrete lembrete, Integer usuarioId) {
        Usuario usuarioEntidade = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontrado(usuarioId));

        lembrete.setUsuario(usuarioEntidade);
        return lembreteRepository.save(lembrete);
    }

    public List<Lembrete> listarTodos(){
        return lembreteRepository.findAll();
    }

    public Lembrete buscarPorId(Integer id){
        return lembreteRepository.findById(id)
                .orElseThrow(() -> new LembreteNaoEncontrado(id));
    }

    public Lembrete atualizar(Integer id, Lembrete lembreteAtualizado, Integer usuarioId) {
        if (!lembreteRepository.existsById(id)) {
            throw new LembreteNaoEncontrado(id);
        }

        Usuario usuarioEntidade = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontrado(usuarioId));

        lembreteAtualizado.setUsuario(usuarioEntidade);
        lembreteAtualizado.setId(id);

        return lembreteRepository.save(lembreteAtualizado);
    }

    public void deletar(Integer id) {
        if (!lembreteRepository.existsById(id)) {
            throw new LembreteNaoEncontrado(id);
        }
        lembreteRepository.deleteById(id);
    }
}
