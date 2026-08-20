package com.temnafesta.infrastructure.persistence.repository;

import com.temnafesta.infrastructure.persistence.entity.EventoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SpringDataEventoRepository extends JpaRepository<EventoJpaEntity, Long> {
    List<EventoJpaEntity> findByAtivoTrueAndDeletadoFalse();
}