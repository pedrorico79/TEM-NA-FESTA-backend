package com.temnafesta.infrastructure.persistence.repository;

import com.temnafesta.infrastructure.persistence.entity.ClienteJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringDataClienteRepository extends JpaRepository<ClienteJpaEntity, Long> {

    @Query("""
            SELECT c FROM ClienteJpaEntity c
            WHERE c.deletado = false
              AND (:busca = ''
                   OR UPPER(c.nome) LIKE UPPER(CONCAT('%', :busca, '%'))
                   OR UPPER(c.telefone) LIKE UPPER(CONCAT('%', :busca, '%'))
                   OR UPPER(c.whatsapp) LIKE UPPER(CONCAT('%', :busca, '%'))
                   OR UPPER(c.instagram) LIKE UPPER(CONCAT('%', :busca, '%')))
            ORDER BY c.ativo DESC
            """)
    List<ClienteJpaEntity> buscarClientesNaoDeletados(@Param("busca") String busca);

}