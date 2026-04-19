package com.temnafesta.repository;

import com.temnafesta.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository <Cliente, Integer> {

    List<Cliente> findByIsAtivoTrue();

    List<Cliente> findByIsAtivoFalse();


}
