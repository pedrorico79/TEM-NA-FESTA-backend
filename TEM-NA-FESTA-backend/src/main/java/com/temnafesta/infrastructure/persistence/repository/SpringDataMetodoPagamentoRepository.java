package com.temnafesta.infrastructure.persistence.repository;

import com.temnafesta.infrastructure.persistence.entity.MetodoPagamentoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataMetodoPagamentoRepository extends JpaRepository<MetodoPagamentoJpaEntity, Long> {
}