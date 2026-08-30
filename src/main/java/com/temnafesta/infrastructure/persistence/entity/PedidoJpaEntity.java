package com.temnafesta.infrastructure.persistence.entity;

import com.temnafesta.domain.vo.StatusProducaoEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_pedido", nullable = false)
    private LocalDateTime dataPedido;

    @Column(name = "data_entrega", nullable = false)
    private LocalDateTime dataEntrega;

    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "taxa_entrega", precision = 10, scale = 2)
    private BigDecimal taxaEntrega;

    @Column(columnDefinition = "TEXT")
    private String observacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_producao", nullable = false, length = 50)
    private StatusProducaoEnum statusProducao;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    // Itens: Raiz de agregação controla os filhos. orphanRemoval garante exclusão se o item for removido da lista
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemPedidoJpaEntity> itens = new ArrayList<>();

    // Pagamentos: Controlados pelo Pedido em cascata
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PagamentoJpaEntity> pagamentos = new ArrayList<>();

    // Endereço de Entrega: apenas referência a um endereço existente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "endereco_entrega_id")
    private EnderecoJpaEntity enderecoEntrega;

    // Evento: Apenas referência (não usamos CascadeType.ALL porque o Pedido não deve criar/deletar o Evento)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id")
    private EventoJpaEntity evento;

    @Column(name = "is_ativo", nullable = false)
    private boolean ativo = true;

    @Column(name = "is_deletado", nullable = false)
    private boolean deletado = false;
}