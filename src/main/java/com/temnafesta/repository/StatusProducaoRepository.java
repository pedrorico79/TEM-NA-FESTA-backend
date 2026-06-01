package com.temnafesta.repository;

import com.temnafesta.model.StatusProducao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusProducaoRepository extends JpaRepository<StatusProducao, Integer> {
    Optional<StatusProducao> findByNome(String nome);
}