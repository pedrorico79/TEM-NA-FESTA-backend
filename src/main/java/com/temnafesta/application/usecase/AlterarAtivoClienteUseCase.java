package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.AlterarAtivoClienteCommand;
import com.temnafesta.application.exception.RecursoNaoEncontradoException;
import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.Cliente;
import com.temnafesta.domain.ports.repository.ClienteRepositoryPort;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;

public class AlterarAtivoClienteUseCase {

    private final ClienteRepositoryPort clienteRepositoryPort;
    private final PedidoRepositoryPort pedidoRepositoryPort;

    public AlterarAtivoClienteUseCase(
            ClienteRepositoryPort clienteRepositoryPort,
            PedidoRepositoryPort pedidoRepositoryPort) {
        this.clienteRepositoryPort = clienteRepositoryPort;
        this.pedidoRepositoryPort = pedidoRepositoryPort;
    }

    public Cliente executar(AlterarAtivoClienteCommand command) {
        Cliente cliente = clienteRepositoryPort.buscarPorId(command.id())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Cliente não encontrado com o ID: " + command.id()));

        if (cliente.isAtivo() == command.ativo()) {
            return cliente;
        }

        if (!command.ativo()
                && pedidoRepositoryPort.existePedidoEmAndamentoPorCliente(command.id())) {
            throw new RegraDeNegocioException(
                    "Não é possível desativar um cliente que possui pedidos em andamento.");
        }

        cliente.alterarStatus(command.ativo());
        return clienteRepositoryPort.salvar(cliente);
    }
}
