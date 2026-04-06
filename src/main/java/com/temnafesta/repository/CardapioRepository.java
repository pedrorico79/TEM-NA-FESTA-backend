package com.temnafesta.repository;


import com.temnafesta.model.Cardapio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardapioRepository extends JpaRepository <Cardapio, Integer> {

    Optional<Cardapio> findByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Integer id);

    List<Cardapio> findByIsAtivo(Boolean filtro);

}
