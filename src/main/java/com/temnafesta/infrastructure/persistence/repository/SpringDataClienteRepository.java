package com.temnafesta.infrastructure.persistence.repository;

import com.temnafesta.infrastructure.persistence.entity.ClienteJpaEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataClienteRepository extends JpaRepository<ClienteJpaEntity, Long> {
    List<ClienteJpaEntity> findByNomeContainingIgnoreCaseOrTelefoneContainingIgnoreCaseAndDeletadoFalse(String termoBusca, String termoBusca1, PageRequest pageable);
}