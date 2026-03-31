package com.temnafesta.repository;

import com.temnafesta.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository <Produto, Integer>{
}
