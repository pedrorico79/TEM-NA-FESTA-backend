package com.temnafesta.application.usecase;

import com.temnafesta.application.exception.RecursoNaoEncontradoException;
import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.Cliente;
import com.temnafesta.domain.ports.repository.ClienteRepositoryPort;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;

public class DeletarClienteUseCase {

    private final ClienteRepositoryPort clienteRepositoryPort;
    private final PedidoRepositoryPort pedidoRepositoryPort;

    public DeletarClienteUseCase(
            ClienteRepositoryPort clienteRepositoryPort,
            PedidoRepositoryPort pedidoRepositoryPort) {
        this.clienteRepositoryPort = clienteRepositoryPort;
        this.pedidoRepositoryPort = pedidoRepositoryPort;
    }

    public void executar(Long id) {
        Cliente cliente = clienteRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Cliente não encontrado com o ID: " + id));

        if (pedidoRepositoryPort.existePedidoEmAndamentoPorCliente(id)) {
            throw new RegraDeNegocioException(
                    "Não é possível excluir um cliente que possui pedidos em andamento.");
        }

        cliente.deletar();
        clienteRepositoryPort.salvar(cliente);
    }
}
