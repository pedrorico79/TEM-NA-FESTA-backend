package com.temnafesta.repository;

import com.temnafesta.model.Lembrete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LembreteRepository extends JpaRepository <Lembrete, Integer> {

    List<Lembrete> findByUsuarioId(Integer usuarioId);

}
