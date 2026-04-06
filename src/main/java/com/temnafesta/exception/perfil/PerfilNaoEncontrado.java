package com.temnafesta.exception.perfil;

public class PerfilNaoEncontrado extends RuntimeException {
    public PerfilNaoEncontrado(Integer id) {
        super("Perfil com id %d não encontrado".formatted(id));
    }
}
