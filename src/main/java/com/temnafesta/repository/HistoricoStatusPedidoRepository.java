package com.temnafesta.repository;

import com.temnafesta.model.HistoricoStatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HistoricoStatusPedidoRepository extends JpaRepository<HistoricoStatusPedido, Integer> { }
