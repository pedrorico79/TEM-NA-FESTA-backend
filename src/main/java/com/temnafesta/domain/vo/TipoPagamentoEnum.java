package com.temnafesta.domain.vo;

public enum TipoPagamentoEnum {
    SINAL,      // Entrada (ex: 50%) [cite: 15, 80]
    QUITACAO,   // Pagamento do saldo restante [cite: 80]
    INTEGRAL    // Pagamento de 100% no ato do pedido [cite: 80]
}