package com.temnafesta.infrastructure.persistence.repository;

import com.temnafesta.domain.vo.StatusProducaoEnum;
import com.temnafesta.infrastructure.persistence.entity.ItemPedidoJpaEntity;
import com.temnafesta.infrastructure.persistence.entity.PedidoJpaEntity;
import com.temnafesta.infrastructure.projection.PedidosPeriodoProjection;
import com.temnafesta.infrastructure.projection.PedidosPorSemanaProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SpringDataPedidoRepository
        extends JpaRepository<PedidoJpaEntity, Long> {

    /*
     * Lista pedidos aplicando filtros de status e período.
     */
    @Query("""
        SELECT p
        FROM PedidoJpaEntity p
        WHERE (:status IS NULL OR p.statusProducao = :status)
          AND (:inicio IS NULL OR p.dataEntrega >= :inicio)
          AND (:fim IS NULL OR p.dataEntrega <= :fim)
          AND p.deletado = false
        """)
    List<PedidoJpaEntity> listarPorFiltros(
            @Param("status") StatusProducaoEnum status,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );


    /*
     * Verifica se o cliente possui pedido em andamento.
     */
    @Query("""
        SELECT COUNT(p) > 0
        FROM PedidoJpaEntity p
        WHERE p.clienteId = :clienteId
          AND p.statusProducao NOT IN (
              com.temnafesta.domain.vo.StatusProducaoEnum.ENTREGUE,
              com.temnafesta.domain.vo.StatusProducaoEnum.CANCELADO
          )
          AND p.deletado = false
        """)
    boolean existePedidoEmAndamentoPorCliente(
            @Param("clienteId") Long clienteId
    );



    /*
     * Quantidade de pedidos por status.
     */
    long countByStatusProducaoAndDeletadoFalse(StatusProducaoEnum status);


    /*
     * Próximas retiradas/entregas.
     */
    @Query("""
        SELECT p
        FROM PedidoJpaEntity p
        WHERE p.dataEntrega <= :limite
          AND p.statusProducao NOT IN (
              com.temnafesta.domain.vo.StatusProducaoEnum.ENTREGUE,
              com.temnafesta.domain.vo.StatusProducaoEnum.CANCELADO
          )
          AND p.deletado = false
        ORDER BY p.dataEntrega ASC
        """)
    List<PedidoJpaEntity> listarProximasRetiradas(
            @Param("limite") LocalDateTime limite
    );


    /*
     * Busca pedidos por cliente, ID, status e evento.
     *
     * Evento é opcional no banco, portanto LEFT JOIN.
     */
    @Query(value = """
    SELECT p.*
    FROM pedido p
    INNER JOIN cliente c
        ON p.cliente_id = c.id
    LEFT JOIN evento e
        ON p.evento_id = e.id
    WHERE p.is_deletado = FALSE
      AND (
          :busca IS NULL
          OR :busca = ''
          OR LOWER(c.nome) LIKE LOWER(CONCAT('%', :busca, '%'))
          OR CAST(p.id AS CHAR) LIKE CONCAT('%', :busca, '%')
      )
      AND (:status IS NULL OR p.status_producao = :status)
      AND (:eventoId IS NULL OR p.evento_id = :eventoId)
    ORDER BY p.data_entrega ASC
    """,
            nativeQuery = true)
    List<PedidoJpaEntity> listarPedidos(
            @Param("busca") String busca,
            @Param("status") StatusProducaoEnum status,
            @Param("eventoId") Long eventoId
    );


    /*
     * Busca um item específico dentro de um pedido.
     */
    @Query("""
        SELECT i
        FROM ItemPedidoJpaEntity i
        WHERE i.id = :itemId
          AND i.pedido.id = :pedidoId
        """)
    Optional<ItemPedidoJpaEntity> buscarItemPorId(
            @Param("pedidoId") Long pedidoId,
            @Param("itemId") Long itemId
    );


    /*
     * Quantidade de pedidos realizados em determinado período.
     */
    Long countByDataPedidoBetweenAndDeletadoFalse(
            LocalDateTime de,
            LocalDateTime ate
    );


    /*
     * Quantidade de pedidos por status e período.
     *   p.status_producao é VARCHAR/Enum.
     */
    @Query("""
        SELECT COUNT(p)
        FROM PedidoJpaEntity p
        WHERE p.statusProducao = :status
          AND p.dataPedido BETWEEN :de AND :ate
          AND p.deletado = false
        """)
    Long countByStatusEPeriodo(
            @Param("status") StatusProducaoEnum status,
            @Param("de") LocalDateTime de,
            @Param("ate") LocalDateTime ate
    );


    /*
     * Faturamento por período.
     */
    @Query("""
    SELECT COALESCE(SUM(p.valorTotal), 0)
    FROM PedidoJpaEntity p
    WHERE p.dataPedido BETWEEN :inicio AND :fim
      AND p.deletado = false
      AND p.statusProducao = 'ENTREGUE'
""")
    BigDecimal somarFaturamentoNoPeriodo(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );



    /*
     * Pedidos agrupados por semana.
     */
    @Query(
            value = """
            SELECT
                CONCAT('Sem ', ROW_NUMBER() OVER (ORDER BY t.semana)) AS rotulo,

                CONCAT(
                    DATE_FORMAT(t.data_inicio, '%d/%m'),
                    ' - ',
                    DATE_FORMAT(t.data_fim, '%d/%m')
                ) AS periodo,

                t.quantidade

            FROM (
                SELECT
                    WEEK(p.data_pedido) AS semana,
                    MIN(p.data_pedido) AS data_inicio,
                    MAX(p.data_pedido) AS data_fim,
                    COUNT(p.id) AS quantidade

                FROM pedido p

                WHERE p.data_pedido BETWEEN :de AND :ate
                  AND p.is_deletado = FALSE

                GROUP BY WEEK(p.data_pedido)
            ) t

            ORDER BY t.semana ASC
            """,
            nativeQuery = true
    )
    List<PedidosPorSemanaProjection> buscarPedidosAgrupadosPorSemana(
            @Param("de") LocalDateTime de,
            @Param("ate") LocalDateTime ate
    );


    /*
     * Relatório paginado de pedidos por período.
     */
    @Query(
            value = """
            SELECT
                p.id AS id,
                p.data_pedido AS dataPedido,
                c.nome AS clienteNome,
                e.nome AS eventoNome,
                p.valor_total AS valorTotal,

                COALESCE(SUM(pag.valor), 0) AS valorPago,

                p.status_producao AS statusNome

            FROM pedido p

            INNER JOIN cliente c
                ON p.cliente_id = c.id

            LEFT JOIN evento e
                ON p.evento_id = e.id

            LEFT JOIN pagamento pag
                ON pag.pedido_id = p.id
                AND pag.status_pagamento = 'CONFIRMADO'

            WHERE p.data_pedido BETWEEN :de AND :ate
              AND p.is_deletado = FALSE

            GROUP BY
                p.id,
                p.data_pedido,
                c.nome,
                e.nome,
                p.valor_total,
                p.status_producao

            ORDER BY p.data_pedido DESC
            """,

            countQuery = """
            SELECT COUNT(*)
            FROM pedido p
            WHERE p.data_pedido BETWEEN :de AND :ate
              AND p.is_deletado = FALSE
            """,

            nativeQuery = true
    )
    Page<PedidosPeriodoProjection> buscarPedidosPeriodoPaginado(
            @Param("de") LocalDateTime de,
            @Param("ate") LocalDateTime ate,
            Pageable pageable
    );


    /*
     * Relatório dinâmico.
     * Evento é opcional
     */
    @Query(
            value = """
            SELECT
                p.id AS id,
                p.data_pedido AS dataPedido,
                c.nome AS clienteNome,
                e.nome AS eventoNome,
                p.valor_total AS valorTotal,

                COALESCE(
                    (
                        SELECT SUM(pag.valor)
                        FROM pagamento pag
                        WHERE pag.pedido_id = p.id
                          AND pag.status_pagamento = 'CONFIRMADO'
                    ),
                    0
                ) AS valorPago,

                p.status_producao AS statusNome

            FROM pedido p

            INNER JOIN cliente c
                ON p.cliente_id = c.id

            LEFT JOIN evento e
                ON p.evento_id = e.id

            WHERE p.is_deletado = FALSE
              AND (:eventoId IS NULL OR p.evento_id = :eventoId)
              AND (:dataInicio IS NULL OR p.data_pedido >= :dataInicio)
              AND (:dataFim IS NULL OR p.data_pedido <= :dataFim)

            ORDER BY p.data_pedido DESC
            """,
            nativeQuery = true
    )
    List<PedidosPeriodoProjection> buscarRelatorioDinamico(
            @Param("eventoId") Integer eventoId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim
    );
}