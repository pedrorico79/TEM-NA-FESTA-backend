package com.temnafesta.repository;

import com.temnafesta.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {

    List<Pagamento> findByPedidoId(Integer pedidoId);

    boolean existsByPedidoId(Integer pedidoId);
}