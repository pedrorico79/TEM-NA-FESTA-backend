package com.temnafesta.infrastructure.persistence.repository;

import com.temnafesta.infrastructure.persistence.entity.EventoJpaEntity;
import com.temnafesta.infrastructure.projection.EventosComparativoProjection;
import com.temnafesta.infrastructure.projection.EvolucaoEventoProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SpringDataEventoRepository extends JpaRepository<EventoJpaEntity, Long> {
    List<EventoJpaEntity> findByDeletadoFalseOrderByAtivoDesc();
    List<EventoJpaEntity> findByAtivoTrueAndDeletadoFalseOrderByAtivoDesc();
    Optional<EventoJpaEntity> findByIdAndDeletadoFalse(Long id);

    @Query(value = """
        SELECT
          e.nome AS evento,
          COUNT(DISTINCT CASE WHEN p.is_deletado = FALSE THEN p.id END) AS pedidosTotais,
          COUNT(DISTINCT p.id) AS vendasObtidas,
          COALESCE(SUM(p.valor_total), 0) AS faturamento,
          ROUND(COALESCE(SUM(p.valor_total), 0) / NULLIF(COUNT(DISTINCT p.id), 0), 2) AS ticketMedio
        FROM pedido p
        INNER JOIN evento e ON p.evento_id = e.id
        WHERE p.status_producao <> 'CANCELADO'
          AND p.data_pedido BETWEEN :de AND :ate
        GROUP BY e.id, e.nome
        ORDER BY faturamento DESC
    """, nativeQuery = true)
    List<EventosComparativoProjection> buscarComparativoEventos(
            @Param("de") LocalDateTime de,
            @Param("ate") LocalDateTime ate
    );

    @Query(value = """
        SELECT periodo,
               SUM(pedidosTotais) AS pedidosTotais,
               SUM(vendasObtidas) AS vendasObtidas,
               SUM(faturamento) AS faturamento,
               ROUND(SUM(faturamento) / NULLIF(SUM(vendasObtidas), 0), 2) AS ticketMedio
        FROM (
            SELECT CASE
                     WHEN :agruparPorAno = true THEN DATE_FORMAT(p.data_pedido, '%Y')
                     ELSE DATE_FORMAT(p.data_pedido, '%Y-%m')
                   END AS periodo,
                   COUNT(DISTINCT CASE WHEN p.is_deletado = FALSE THEN p.id END) AS pedidosTotais,
                   COUNT(DISTINCT p.id) AS vendasObtidas,
                   COALESCE(SUM(p.valor_total), 0) AS faturamento
            FROM pedido p
            WHERE p.evento_id = :eventoId
              AND p.status_producao <> 'CANCELADO'
              AND p.data_pedido BETWEEN :inicio AND :fim
            GROUP BY periodo

        ) AS dados
        GROUP BY periodo
        ORDER BY periodo
        """, nativeQuery = true)
    List<EvolucaoEventoProjection> buscarEvolucaoEvento(
            @Param("eventoId") Long eventoId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("agruparPorAno") boolean agruparPorAno
    );
}
