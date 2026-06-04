package com.temnafesta.repository;

import com.temnafesta.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    List<Cliente> findByIsAtivoTrueAndIsDeletadoFalse();

    List<Cliente> findByIsDeletadoFalse();

    Page<Cliente> findByIsDeletadoFalseAndNomeContainingIgnoreCase(String nome, Pageable pageable);

}
