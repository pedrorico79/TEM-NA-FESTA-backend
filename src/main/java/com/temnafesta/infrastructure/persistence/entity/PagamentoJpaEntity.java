

package com.temnafesta.infrastructure.persistence.entity;
import com.temnafesta.domain.vo.StatusPagamentoEnum;
import com.temnafesta.domain.vo.TipoPagamentoEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "data_pagamento", nullable = false)
    private LocalDateTime dataPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pagamento", nullable = false, length = 30)
    private TipoPagamentoEnum tipoPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_pagamento", nullable = false, length = 30)
    private StatusPagamentoEnum statusPagamento;

    @Column(name = "metodo_pagamento_id", nullable = false)
    private Long metodoPagamentoId;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private PedidoJpaEntity pedido;
}