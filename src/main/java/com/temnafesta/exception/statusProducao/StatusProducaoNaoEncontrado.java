package com.temnafesta.exception.statusProducao;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StatusProducaoNaoEncontrado extends RuntimeException {
  public StatusProducaoNaoEncontrado(Integer id) {
    super("Status Produção com id %d não encontrado".formatted(id));
  }
}
