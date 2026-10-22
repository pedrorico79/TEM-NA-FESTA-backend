package com.temnafesta.application.exception;

public class TentativasLoginExcedidasException extends RuntimeException {
    private final long segundosParaTentarNovamente;

    public TentativasLoginExcedidasException(long segundosParaTentarNovamente) {
        super("Muitas tentativas de acesso. Aguarde antes de tentar novamente.");
        this.segundosParaTentarNovamente = segundosParaTentarNovamente;
    }

    public long getSegundosParaTentarNovamente() {
        return segundosParaTentarNovamente;
    }
}
