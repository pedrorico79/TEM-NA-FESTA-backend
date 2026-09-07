package com.temnafesta.presentation.dto;

import com.temnafesta.domain.vo.TipoPagamentoEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record RegistrarPagamentoRequestDto(
        @NotNull(message = "O valor do pagamento é obrigatório.")
        @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero.")
        BigDecimal valor,

        @NotNull(message = "O tipo de pagamento (SINAL, QUITACAO, INTEGRAL) é obrigatório.")
        TipoPagamentoEnum tipoPagamento,

        @NotNull(message = "O ID do método de pagamento é obrigatório.")
        Long metodoPagamentoId,

        @NotNull(message = "O ID do usuário que registrou o pagamento é obrigatório.")
        Long usuarioId
) {}