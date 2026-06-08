package com.temnafesta.repository;

import com.temnafesta.model.HistoricoStatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface HistoricoStatusPedidoRepository extends JpaRepository<HistoricoStatusPedido, Integer> {

    List<HistoricoStatusPedido> findByPedidoIdOrderByDataAlteracaoDesc(
            Integer pedidoId
    );
}
