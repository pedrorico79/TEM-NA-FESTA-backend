package com.temnafesta.repository;

import com.temnafesta.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository <Endereco, Integer> {
}
