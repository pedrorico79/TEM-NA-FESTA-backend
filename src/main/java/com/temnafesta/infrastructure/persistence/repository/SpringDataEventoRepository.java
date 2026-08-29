package com.temnafesta.infrastructure.persistence.repository;

import com.temnafesta.infrastructure.persistence.entity.EventoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataEventoRepository extends JpaRepository<EventoJpaEntity, Long> {
    List<EventoJpaEntity> findByAtivoTrueAndDeletadoFalse();
    Optional<EventoJpaEntity> findByIdAndDeletadoFalse(Long id);
}