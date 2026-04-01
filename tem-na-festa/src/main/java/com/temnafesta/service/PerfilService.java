package com.temnafesta.service;

import com.temnafesta.exception.perfil.PerfilNaoEncontrado;
import com.temnafesta.model.Perfil;
import com.temnafesta.repository.PerfilRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerfilService {

    private final PerfilRepository perfilRepository;

    public PerfilService(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    public Perfil criar(Perfil perfil) {
        return perfilRepository.save(perfil);
    }

    public List<Perfil> listar() {
        return perfilRepository.findAll();
    }

    public Perfil buscarPorId(Integer id) {
        return perfilRepository.findById(id)
                .orElseThrow(() -> new PerfilNaoEncontrado(id));
    }

    public Perfil atualizar(Integer id, Perfil perfilAtualizado) {
        if (!perfilRepository.existsById(id)) {
            throw new PerfilNaoEncontrado(id);
        }
        perfilAtualizado.setId(id);
        return perfilRepository.save(perfilAtualizado);
    }

    public void deletar(Integer id) {
        if (!perfilRepository.existsById(id)) {
            throw new PerfilNaoEncontrado(id);
        }
        perfilRepository.deleteById(id);
    }
}
