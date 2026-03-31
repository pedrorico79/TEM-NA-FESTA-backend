package com.temnafesta.repository;

import com.temnafesta.model.Campanha;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampanhaRepository extends JpaRepository <Campanha, Integer> {
}
