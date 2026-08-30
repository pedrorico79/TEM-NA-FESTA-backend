package com.temnafesta.infrastructure.persistence.repository;

import com.temnafesta.domain.vo.StatusProducaoEnum;
import com.temnafesta.infrastructure.persistence.entity.ItemPedidoJpaEntity;
import com.temnafesta.infrastructure.persistence.entity.PedidoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SpringDataPedidoRepository extends JpaRepository<PedidoJpaEntity, Long> {

    @Query("SELECT p FROM PedidoJpaEntity p " +
            "WHERE (:status IS NULL OR p.statusProducao = :status) " +
            "AND (:inicio IS NULL OR p.dataEntrega >= :inicio) " +
            "AND (:fim IS NULL OR p.dataEntrega <= :fim) " +
            "AND p.deletado = false")
    List<PedidoJpaEntity> listarPorFiltros(@Param("status") StatusProducaoEnum status,
                                           @Param("inicio") LocalDateTime inicio,
                                           @Param("fim") LocalDateTime fim);

    @Query("SELECT COUNT(p) > 0 FROM PedidoJpaEntity p " +
            "WHERE p.clienteId = :clienteId " +
            "AND p.statusProducao NOT IN ('ENTREGUE', 'CANCELADO') " +
            "AND p.deletado = false")
    boolean existePedidoEmAndamentoPorCliente(@Param("clienteId") Long clienteId);

    long countByStatusProducao(StatusProducaoEnum status);

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
            @Param("limite") LocalDateTime limite);

    @Query("""
        SELECT p
        FROM PedidoJpaEntity p
        LEFT JOIN ClienteJpaEntity c ON c.id = p.clienteId
        WHERE p.deletado = false
          AND (
              :busca IS NULL
              OR :busca = ''
              OR LOWER(c.nome) LIKE LOWER(CONCAT('%', :busca, '%'))
              OR CAST(p.id AS string) LIKE CONCAT('%', :busca, '%')
          )
          AND (:status IS NULL OR p.statusProducao = :status)
          AND (:eventoId IS NULL OR p.evento.id = :eventoId)
        ORDER BY p.dataEntrega ASC
    """)
    List<PedidoJpaEntity> listarPedidos(
            @Param("busca") String busca,
            @Param("status") StatusProducaoEnum status,
            @Param("eventoId") Long eventoId);

    @Query("""
        SELECT i
        FROM ItemPedidoJpaEntity i
        WHERE i.id = :itemId
          AND i.pedido.id = :pedidoId
    """)
    Optional<ItemPedidoJpaEntity> buscarItemPorId(
            @Param("pedidoId") Long pedidoId,
            @Param("itemId") Long itemId);
}