package com.temnafesta.domain.ports.repository;

import com.temnafesta.domain.model.Lembrete;
import java.util.List;
import java.util.Optional;

public interface LembreteRepositoryPort {
    Lembrete salvar(Lembrete lembrete);
    List<Lembrete> listarPorUsuarioId(Long usuarioId);
    Optional<Lembrete> buscarPorId(Long id);
    void deletarPorId(Long id);
}