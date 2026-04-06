package com.temnafesta.repository;

import com.temnafesta.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository <Cliente, Integer> {
}
