package com.temnafesta.infrastructure.persistence.repository;

import com.temnafesta.infrastructure.persistence.entity.EnderecoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataEnderecoRepository extends JpaRepository<EnderecoJpaEntity, Long> {
}
