package com.temnafesta.repository;

import com.temnafesta.model.Pedido;
import com.temnafesta.model.StatusProducao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;

import java.util.List;

public interface PedidoRepository extends JpaRepository <Pedido, Integer> {


    // retorna true para contagem de pedidos > 0, false para = 0
    // where -> cliente ID
    // e o Status não seja CANCELADO nem ENTREGUE (puxa apenas NAO_INICIADO, EM_PRODUCAO, PRONTO)
    // -----> Não deixa desativar cliente se ele tem pedidos pendentes
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM Pedido p WHERE p.cliente.id = :clienteId " +
            "AND p.statusProducao.nome NOT IN ('CANCELADO', 'ENTREGUE')")
    Boolean existsPedidosAtivosParaCliente(Integer clienteId);



    // Pedidos Não cancelados ( válidos)
    @Query("SELECT p FROM Pedido p WHERE p.statusProducao.nome <> 'CANCELADO'")
    List<Pedido> findApenasPedidosValidos();


    // Pedidos em andamento ( não cancelados nem entregues)
    @Query("SELECT p FROM Pedido p WHERE p.statusProducao.nome NOT IN ('CANCELADO', 'ENTREGUE')")
    List<Pedido> findPedidosEmAndamento();


    // Pedidos concluidos (passar parametro -> entregue)
    List<Pedido> findByStatusProducao(StatusProducao status);

    @Query("""
    SELECT p
    FROM Pedido p
    WHERE p.dataEntrega BETWEEN :inicio AND :fim
      AND p.statusProducao.nome NOT IN ('CANCELADO', 'ENTREGUE')
    ORDER BY p.dataEntrega ASC
""")
    Page<Pedido> buscarProximasRetiradas(
            LocalDateTime inicio,
            LocalDateTime fim,
            Pageable pageable
    );


    @Query("""
    SELECT p
    FROM Pedido p
    WHERE p.dataEntrega BETWEEN :inicio AND :fim
      AND p.statusProducao.nome NOT IN ('CANCELADO', 'ENTREGUE')
""")
    List<Pedido> countPedidos(
            LocalDateTime inicio,
            LocalDateTime fim
    );

}
