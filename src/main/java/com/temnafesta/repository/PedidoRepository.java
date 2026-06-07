package com.temnafesta.repository;

import com.temnafesta.dto.relatorio.pedidosporsemana.PedidosPorSemanaProjection;
import com.temnafesta.model.Pedido;
import com.temnafesta.model.StatusProducao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
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

    Long countByDataPedidoBetween(LocalDateTime localDateTime, LocalDateTime localDateTime1);

    // Pedidos por status status entrege e periodo
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.statusProducao.id = :statusId AND p.dataPedido BETWEEN :de AND :ate")
    Long countByStatusEPeriodo(@Param("statusId") Integer statusId, @Param("de") LocalDateTime de, @Param("ate") LocalDateTime ate);

    // Faturamento por periodo
    @Query("SELECT SUM(p.valorTotal) FROM Pedido p WHERE p.statusProducao.id = :statusId AND p.dataPedido BETWEEN :de AND :ate")
    BigDecimal somarFaturamentoNoPeriodo(@Param("statusId") Integer statusId, @Param("de") LocalDateTime de, @Param("ate") LocalDateTime ate);

    //com.temnafesta.dto.relatorio.pedidosporsemana;
    //
    //public record PedidosPorSemanaResponseDto

    // Retorna a quantidade de pedidos agrupado por rotulos como "Sem 19" para o grafico de pedidos por semana do relatorio consumir
    @Query(value =
            "SELECT CONCAT('Sem ', t.semana) AS rotulo, t.quantidade " +
                    "FROM ( " +
                    "  SELECT WEEK(p.data_pedido) AS semana, COUNT(p.id) AS quantidade " +
                    "  FROM pedido p " +
                    "  WHERE p.data_pedido BETWEEN :de AND :ate " +
                    "  GROUP BY WEEK(p.data_pedido) " +
                    ") t " +
                    "ORDER BY t.semana ASC",
            nativeQuery = true)
    List<PedidosPorSemanaProjection> buscarPedidosAgrupadosPorSemana(
            @Param("de") LocalDateTime de,
            @Param("ate") LocalDateTime ate
    );
}
