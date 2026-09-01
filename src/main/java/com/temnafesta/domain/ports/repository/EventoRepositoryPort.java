package com.temnafesta.domain.ports.repository;

import com.temnafesta.domain.model.Evento;

import java.util.List;
import java.util.Optional;

public interface EventoRepositoryPort {
    List<Evento> listarEventosAtivos();
    Evento salvar(Evento evento);
    Optional<Evento> buscarPorId(Long id);
    Evento atualizar(Evento evento);
    void deletar(Long id);
    Evento alterarStatus(Long id, boolean ativo);
}