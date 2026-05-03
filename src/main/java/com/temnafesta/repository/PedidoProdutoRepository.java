package com.temnafesta.repository;

import com.temnafesta.model.PedidoProduto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedidoProdutoRepository extends JpaRepository<PedidoProduto, Integer> {
    List<PedidoProduto> findByPedidoId(Integer pedidoId);

    Optional<PedidoProduto> findByIdAndPedidoId(Integer id, Integer pedidoId);
}
