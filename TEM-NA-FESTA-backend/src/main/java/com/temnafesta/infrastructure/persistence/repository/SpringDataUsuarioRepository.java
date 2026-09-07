package com.temnafesta.infrastructure.persistence.repository;

import com.temnafesta.infrastructure.persistence.entity.UsuarioJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataUsuarioRepository extends JpaRepository<UsuarioJpaEntity, Long> {
    Optional<UsuarioJpaEntity> findByEmail(String email);
    Page<UsuarioJpaEntity> findByDeletadoFalseAndNomeContainingIgnoreCase(String nome, Pageable pageable);
    Page<UsuarioJpaEntity> findByDeletadoFalse(Pageable pageable);
    Optional<UsuarioJpaEntity> findByIdAndDeletadoFalse(Long id);
}