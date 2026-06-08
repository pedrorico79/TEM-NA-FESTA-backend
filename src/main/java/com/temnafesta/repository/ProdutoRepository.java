package com.temnafesta.repository;

import com.temnafesta.dto.relatorio.produtosmaisvendidos.MaisVendidosProjection;
import com.temnafesta.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProdutoRepository extends JpaRepository <Produto, Integer>{

    List<Produto> findByIsAtivoTrue();
    List<Produto> findByIsAtivoFalse();
    List<Produto> findByIsDeletadoFalse();

    Page<Produto> findByIsDeletadoFalseAndNomeContainingIgnoreCaseOrderByIsAtivoDesc(String nome, Pageable pageable);

    // Busca produtos mais vendidos paginado
    @Query(value = "SELECT " +
            "  prod.nome AS item, " +
            "  CAST(SUM(item.quantidade) AS SIGNED) AS qtdeVendida, " +
            "  SUM(item.quantidade * item.preco_unitario) AS faturamento, " +
            "  ROUND((SUM(item.quantidade * item.preco_unitario) / " +
            "    (SELECT SUM(it.quantidade * it.preco_unitario) " +
            "     FROM item_pedido it " +
            "     INNER JOIN pedido pe ON it.pedido_id = pe.id " +
            "     WHERE pe.data_pedido BETWEEN :de AND :ate)) * 100, 2) AS porcentagemDoTotal " +
            "FROM pedido p " +
            "INNER JOIN item_pedido item ON item.pedido_id = p.id " +
            "INNER JOIN produto prod ON item.produto_id = prod.id " +
            "WHERE p.data_pedido BETWEEN :de AND :ate " +
            "GROUP BY prod.id, prod.nome " +
            "ORDER BY qtdeVendida DESC",
            countQuery = "SELECT COUNT(DISTINCT item.produto_id) " +
                    "FROM pedido p " +
                    "INNER JOIN item_pedido item ON item.pedido_id = p.id " +
                    "WHERE p.data_pedido BETWEEN :de AND :ate",
            nativeQuery = true)
    Page<MaisVendidosProjection> buscarProdutosMaisVendidosPaginado(
            @Param("de") java.time.LocalDateTime de,
            @Param("ate") java.time.LocalDateTime ate,
            Pageable pageable
    );
}
