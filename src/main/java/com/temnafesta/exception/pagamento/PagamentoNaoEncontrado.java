package com.temnafesta.exception.pagamento;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PagamentoNaoEncontrado extends RuntimeException {
    public PagamentoNaoEncontrado(Integer id) {
        super("Pagamento com id %d não encontrado".formatted(id));
    }
}