package com.temnafesta.repository;

import com.temnafesta.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository <Produto, Integer>{

    List<Produto> findByIsAtivoTrue();
    List<Produto> findByIsAtivoFalse();
}
