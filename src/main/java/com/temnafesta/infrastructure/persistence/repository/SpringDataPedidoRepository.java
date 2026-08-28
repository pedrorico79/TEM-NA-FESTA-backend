package com.temnafesta.infrastructure.persistence.repository;

import com.temnafesta.domain.vo.StatusProducaoEnum;
import com.temnafesta.infrastructure.persistence.entity.PedidoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

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
}