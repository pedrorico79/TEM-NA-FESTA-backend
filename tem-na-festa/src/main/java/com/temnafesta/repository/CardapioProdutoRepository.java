package com.temnafesta.repository;

import com.temnafesta.model.CardapioProduto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardapioProdutoRepository extends JpaRepository<CardapioProduto, Integer> {
    List<CardapioProduto> findByCardapioId(Integer cardapioId);
}