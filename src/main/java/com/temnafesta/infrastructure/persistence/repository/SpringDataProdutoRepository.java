package com.temnafesta.infrastructure.persistence.repository;

import com.temnafesta.infrastructure.persistence.entity.ProdutoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataProdutoRepository extends JpaRepository<ProdutoJpaEntity, Long> {
}