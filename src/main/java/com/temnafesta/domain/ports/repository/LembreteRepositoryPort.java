package com.temnafesta.domain.ports.repository;

import com.temnafesta.domain.model.Lembrete;
import java.util.List;

public interface LembreteRepositoryPort {
    Lembrete salvar(Lembrete lembrete);
    List<Lembrete> buscarPorUsuarioId(Long usuarioId);
}