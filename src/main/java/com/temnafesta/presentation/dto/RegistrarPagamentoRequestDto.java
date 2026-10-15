package com.temnafesta.presentation.dto;

import com.temnafesta.domain.vo.TipoPagamentoEnum;
import com.temnafesta.domain.vo.StatusPagamentoEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record RegistrarPagamentoRequestDto(
        @NotNull(message = "O valor do pagamento é obrigatório.")
        @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero.")
        @Digits(integer = 8, fraction = 2, message = "O valor deve ter até 8 dígitos inteiros e 2 casas decimais.")
        BigDecimal valor,

        @NotNull(message = "O tipo de pagamento (SINAL, QUITACAO, INTEGRAL) é obrigatório.")
        TipoPagamentoEnum tipoPagamento,

        @NotNull(message = "O ID do método de pagamento é obrigatório.")
        Long metodoPagamentoId,

        StatusPagamentoEnum statusPagamento
) {}
