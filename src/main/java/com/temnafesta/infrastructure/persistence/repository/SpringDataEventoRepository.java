package com.temnafesta.infrastructure.persistence.repository;

import com.temnafesta.infrastructure.persistence.entity.EventoJpaEntity;
import com.temnafesta.infrastructure.projection.EventosComparativoProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SpringDataEventoRepository extends JpaRepository<EventoJpaEntity, Long> {
    List<EventoJpaEntity> findByAtivoTrueAndDeletadoFalse();

    @Query(value = """
        SELECT
          e.nome AS evento,
          COUNT(DISTINCT CASE WHEN p.data_pedido BETWEEN :de AND :ate THEN p.id END) AS pedidosTotais,
          COUNT(DISTINCT pag.pedido_id) AS vendasObtidas,
          COALESCE(SUM(pag.valor), 0) AS faturamento,
          ROUND(COALESCE(SUM(pag.valor), 0) / NULLIF(COUNT(DISTINCT pag.pedido_id), 0), 2) AS ticketMedio
        FROM pedido p
        INNER JOIN evento e ON p.evento_id = e.id
        LEFT JOIN pagamento pag ON pag.pedido_id = p.id AND pag.data_pagamento BETWEEN :de AND :ate
        WHERE (p.data_pedido BETWEEN :de AND :ate OR pag.data_pagamento BETWEEN :de AND :ate)
        GROUP BY e.id, e.nome
        ORDER BY faturamento DESC
    """, nativeQuery = true)
    List<EventosComparativoProjection> buscarComparativoEventos(
            @Param("de") LocalDateTime de,
            @Param("ate") LocalDateTime ate
    );
}