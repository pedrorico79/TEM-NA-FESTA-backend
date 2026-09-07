package com.temnafesta.infrastructure.persistence.repository;

import com.temnafesta.infrastructure.persistence.entity.HistoricoStatusPedidoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SpringDataHistoricoStatusPedidoRepository extends JpaRepository<HistoricoStatusPedidoJpaEntity, Long> {
    List<HistoricoStatusPedidoJpaEntity> findByPedidoId(Long pedidoId);
}