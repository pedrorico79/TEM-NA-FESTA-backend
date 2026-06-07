package com.temnafesta.repository;

import com.temnafesta.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository <Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);
    Boolean existsByEmail(String email);

    List<Usuario> findByIsAtivoTrue();
    List<Usuario> findByIsAtivoFalse();

    Page<Usuario> findByNomeContainingIgnoreCaseAndIsAtivoTrue(
            String nome,
            Pageable pageable
    );

}
