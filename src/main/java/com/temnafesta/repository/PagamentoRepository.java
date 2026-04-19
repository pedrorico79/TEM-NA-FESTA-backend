package com.temnafesta.repository;

import com.temnafesta.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {

    List<Pagamento> findByPedidoId(Integer pedidoId);

    boolean existsByPedidoId(Integer pedidoId);

    @Query("SELECT COALESCE(SUM(p.valor), 0) FROM Pagamento p WHERE p.pedido.id = :pedidoId")
    BigDecimal somarPagamentosPorPedido(@Param("pedidoId") Integer pedidoId);
}