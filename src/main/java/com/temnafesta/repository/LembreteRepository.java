package com.temnafesta.repository;

import com.temnafesta.model.Lembrete;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LembreteRepository extends JpaRepository <Lembrete, Integer> {
}
