package com.temnafesta.infrastructure.persistence.repository;

import com.temnafesta.infrastructure.persistence.entity.LembreteJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SpringDataLembreteRepository extends JpaRepository<LembreteJpaEntity, Long> {
    List<LembreteJpaEntity> findByUsuarioId(Long usuarioId);
}