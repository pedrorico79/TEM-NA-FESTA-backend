package com.temnafesta.infrastructure.persistence.repository;

import com.temnafesta.infrastructure.persistence.entity.ClienteJpaEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringDataClienteRepository extends JpaRepository<ClienteJpaEntity, Long> {
    @Query("""
    SELECT c FROM ClienteJpaEntity c
    WHERE (:termo IS NULL OR :termo = ''
           OR UPPER(c.nome) LIKE UPPER(CONCAT('%', :termo, '%'))
           OR UPPER(c.telefone) LIKE UPPER(CONCAT('%', :termo, '%')))
      AND c.deletado = false
""")
    List<ClienteJpaEntity> buscarClientes(@Param("termo") String termo, PageRequest pageable);

}