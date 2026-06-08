package com.temnafesta.repository;

import com.temnafesta.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedidoProdutoRepository extends JpaRepository<ItemPedido, Integer> {
    List<ItemPedido> findByPedidoId(Integer pedidoId);

    Optional<ItemPedido> findByIdAndPedidoId(Integer id, Integer pedidoId);
}
