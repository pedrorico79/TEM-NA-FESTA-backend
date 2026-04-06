package com.temnafesta.exception.statusProducao;

public class StatusProducaoNaoEncontrado extends RuntimeException {
  public StatusProducaoNaoEncontrado(Integer id) {
    super("Status Produção com id %d não encontrado".formatted(id));
  }
}
