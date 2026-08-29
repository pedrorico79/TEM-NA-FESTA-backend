package com.temnafesta.infrastructure.persistence.repository;

import com.temnafesta.infrastructure.persistence.entity.ProdutoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataProdutoRepository extends JpaRepository<ProdutoJpaEntity, Long> {

    List<ProdutoJpaEntity> findByDeletadoFalseAndNomeContainingIgnoreCaseOrderByAtivoDesc(String nome);

}