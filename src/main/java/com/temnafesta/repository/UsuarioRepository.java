package com.temnafesta.repository;

import com.temnafesta.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository <Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);
    Boolean existsByEmail(String email);

    List<Usuario> findByIsDeletadoFalse(); // lista ativos e inativos, não deletados
    List<Usuario> findByIsAtivoTrueAndIsDeletadoFalse(); //lista ativos, não deletados
    // List<Usuario> findByIsAtivoFalseAndIsDeletadoFalse(); // lista inativos, não deletados

    Page<Usuario> findByNomeContainingIgnoreCaseAndIsDeletadoFalse(
            String nome,
            Pageable pageable
    );

}
