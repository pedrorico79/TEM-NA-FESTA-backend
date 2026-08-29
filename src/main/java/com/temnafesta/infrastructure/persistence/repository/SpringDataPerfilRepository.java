package com.temnafesta.infrastructure.persistence.repository;

import com.temnafesta.infrastructure.persistence.entity.PerfilJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPerfilRepository extends JpaRepository<PerfilJpaEntity, Long> {
}
