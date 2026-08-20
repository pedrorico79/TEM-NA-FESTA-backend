package com.temnafesta.domain.ports.repository;

import com.temnafesta.domain.model.Evento;
import java.util.List;

public interface EventoRepositoryPort {
    List<Evento> listarEventosAtivos();
}