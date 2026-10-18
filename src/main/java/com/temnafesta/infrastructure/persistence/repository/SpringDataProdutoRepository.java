package com.temnafesta.infrastructure.persistence.repository;

import com.temnafesta.infrastructure.persistence.entity.ProdutoJpaEntity;
import com.temnafesta.infrastructure.projection.MaisVendidosProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

public interface SpringDataProdutoRepository extends JpaRepository<ProdutoJpaEntity, Long> {

    @Query("""
            SELECT p FROM ProdutoJpaEntity p
            WHERE p.deletado = false
              AND (:ativos = false OR p.ativo = true)
              AND LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
            ORDER BY p.ativo DESC
            """)
    List<ProdutoJpaEntity> buscarPorNomeNaoDeletados(
            @Param("nome") String nome,
            @Param("ativos") boolean ativos
    );

    @Query("""
            SELECT p FROM ProdutoJpaEntity p
            WHERE p.deletado = false
              AND (:ativos = false OR p.ativo = true)
              AND LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
            """)
    Page<ProdutoJpaEntity> buscarPorNomeNaoDeletados(
            @Param("nome") String nome,
            @Param("ativos") boolean ativos,
            Pageable pageable
    );

    Optional<ProdutoJpaEntity> findByIdAndDeletadoFalse(Long id);


    // Busca produtos mais vendidos paginado
    @Query(value = "SELECT " +
            "  prod.nome AS item, " +
            "  CAST(SUM(item.quantidade) AS SIGNED) AS qtdeVendida, " +
            "  SUM((item.quantidade * item.preco_unitario + item.acrescimo) * " +
            "    (1 - COALESCE(p.desconto_pedido, 0) / NULLIF((SELECT " +
            "      COALESCE(SUM(it.quantidade * it.preco_unitario + it.acrescimo), 0) + COALESCE(p.taxa_entrega, 0) " +
            "      FROM item_pedido it WHERE it.pedido_id = p.id), 0))) AS faturamento, " +
            "  COALESCE(ROUND((SUM((item.quantidade * item.preco_unitario + item.acrescimo) * " +
            "    (1 - COALESCE(p.desconto_pedido, 0) / NULLIF((SELECT " +
            "      COALESCE(SUM(it.quantidade * it.preco_unitario + it.acrescimo), 0) + COALESCE(p.taxa_entrega, 0) " +
            "      FROM item_pedido it WHERE it.pedido_id = p.id), 0))) / NULLIF(" +
            "    (SELECT SUM((it.quantidade * it.preco_unitario + it.acrescimo) * " +
            "      (1 - COALESCE(pe.desconto_pedido, 0) / NULLIF((SELECT " +
            "        COALESCE(SUM(it2.quantidade * it2.preco_unitario + it2.acrescimo), 0) + COALESCE(pe.taxa_entrega, 0) " +
            "        FROM item_pedido it2 WHERE it2.pedido_id = pe.id), 0))) " +
            "     FROM item_pedido it " +
            "     INNER JOIN pedido pe ON it.pedido_id = pe.id " +
            "     WHERE pe.data_pedido BETWEEN :de AND :ate " +
            "       AND pe.status_producao <> 'CANCELADO' " +
            "       AND (:eventoId IS NULL OR pe.evento_id = :eventoId)), 0)) * 100, 2), 0) AS porcentagemDoTotal " +
            "FROM pedido p " +
            "INNER JOIN item_pedido item ON item.pedido_id = p.id " +
            "INNER JOIN produto prod ON item.produto_id = prod.id " +
            "WHERE p.data_pedido BETWEEN :de AND :ate " +
            "  AND p.status_producao <> 'CANCELADO' " +
            "  AND (:eventoId IS NULL OR p.evento_id = :eventoId) " +
            "GROUP BY prod.id, prod.nome " +
            "ORDER BY qtdeVendida DESC",
            countQuery = "SELECT COUNT(DISTINCT item.produto_id) " +
                    "FROM pedido p " +
                    "INNER JOIN item_pedido item ON item.pedido_id = p.id " +
                    "WHERE p.data_pedido BETWEEN :de AND :ate " +
                    "  AND p.status_producao <> 'CANCELADO' " +
                    "  AND (:eventoId IS NULL OR p.evento_id = :eventoId)",
            nativeQuery = true)
    Page<MaisVendidosProjection> buscarProdutosMaisVendidosPaginado(
            @Param("de") LocalDateTime de,
            @Param("ate") LocalDateTime ate,
            @Param("eventoId") Long eventoId,
            Pageable pageable
    );
}
