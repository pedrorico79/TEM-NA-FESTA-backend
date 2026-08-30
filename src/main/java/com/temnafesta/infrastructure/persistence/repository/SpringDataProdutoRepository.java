package com.temnafesta.infrastructure.persistence.repository;

import com.temnafesta.infrastructure.persistence.entity.ProdutoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataProdutoRepository extends JpaRepository<ProdutoJpaEntity, Long> {

    List<ProdutoJpaEntity> findByDeletadoFalseAndNomeContainingIgnoreCaseOrderByAtivoDesc(String nome);

    Optional<ProdutoJpaEntity> findByIdAndDeletadoFalse(Long id);

}