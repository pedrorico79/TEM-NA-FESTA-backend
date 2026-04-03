package com.temnafesta.repository;

import com.temnafesta.mapper.CampanhaMapper;
import com.temnafesta.model.Campanha;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CampanhaRepository extends JpaRepository <Campanha, Integer> {


    Optional<Campanha> findByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Integer id);

    List<Campanha> findByAtiva(Boolean ativa);
}


