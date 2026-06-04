package com.temnafesta.exception.metodopagamento;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MetodoPagamentoNaoEncontrado extends RuntimeException {
    public MetodoPagamentoNaoEncontrado(Integer id) {
        super("Metodo de pagamento com id %d não encontrado".formatted(id));
    }
}

