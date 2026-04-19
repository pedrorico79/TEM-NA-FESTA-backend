package com.temnafesta.exception.cliente;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ClienteComPedidosAtivosException extends RuntimeException {
    public ClienteComPedidosAtivosException(Integer idCliente) {
        super(
                String.format("Não é possível desativar cliente(id %d) com pedidos ativos",idCliente)
        );
    }
}
