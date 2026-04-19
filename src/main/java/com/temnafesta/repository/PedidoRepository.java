package com.temnafesta.repository;

import com.temnafesta.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository <Pedido, Integer> {

    Boolean existsByClienteIdAndIsAtivoTrue(Integer clienteId);

}
