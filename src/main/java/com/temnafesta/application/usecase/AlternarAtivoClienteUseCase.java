package com.temnafesta.application.usecase;
import com.temnafesta.application.dto.AlternarAtivoClienteCommand;
import com.temnafesta.domain.exception.NaoEncontradoException;
import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.Cliente;
import com.temnafesta.domain.ports.repository.ClienteRepositoryPort;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;

public class AlternarAtivoClienteUseCase {

    private final ClienteRepositoryPort clienteRepositoryPort;
    private final PedidoRepositoryPort pedidoRepositoryPort;

    public AlternarAtivoClienteUseCase(ClienteRepositoryPort clienteRepositoryPort, PedidoRepositoryPort pedidoRepositoryPort) {
        this.clienteRepositoryPort = clienteRepositoryPort;
        this.pedidoRepositoryPort = pedidoRepositoryPort;
    }

    public Cliente executar(AlternarAtivoClienteCommand command) {
        Cliente cliente = clienteRepositoryPort.buscarPorId(command.clienteId())
                .orElseThrow(() -> new NaoEncontradoException("Cliente não encontrado com o ID fornecido."));

        // Regra de Negócio: Bloqueia a desativação se houver pedidos ativos associados ao cliente_id
        if (!command.ativo() && pedidoRepositoryPort.existePedidoEmAndamentoPorCliente(command.clienteId())) {
            throw new RegraDeNegocioException("Não é possível desativar um cliente que possui pedidos em andamento.");
        }

        cliente.alterarStatus(command.ativo());

        return clienteRepositoryPort.salvar(cliente);
    }
}