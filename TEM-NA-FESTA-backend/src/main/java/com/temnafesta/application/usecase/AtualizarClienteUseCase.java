package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.AtualizarClienteCommand;
import com.temnafesta.application.exception.RecursoNaoEncontradoException;
import com.temnafesta.domain.model.Cliente;
import com.temnafesta.domain.model.Endereco;
import com.temnafesta.domain.ports.repository.ClienteRepositoryPort;

public class AtualizarClienteUseCase {
    private final ClienteRepositoryPort clienteRepositoryPort;

    public AtualizarClienteUseCase(ClienteRepositoryPort clienteRepositoryPort) {
        this.clienteRepositoryPort = clienteRepositoryPort;
    }

    public Cliente executar(AtualizarClienteCommand command) {
        Cliente clienteExistente = clienteRepositoryPort.buscarPorId(command.id())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Cliente não encontrado com o ID: " + command.id()));

        Endereco enderecoAtualizado = clienteExistente.getEndereco();

        if (command.endereco() != null) {
            Long enderecoId = clienteExistente.getEndereco() == null
                    ? null
                    : clienteExistente.getEndereco().getId();

            enderecoAtualizado = new Endereco(
                    enderecoId,
                    command.endereco().cep(),
                    command.endereco().logradouro(),
                    command.endereco().numero(),
                    command.endereco().complemento(),
                    command.endereco().bairro(),
                    command.endereco().cidade(),
                    command.endereco().estado()
            );
        }

        String nome = command.nome() == null ? null : command.nome().trim();
        Cliente clienteAtualizado = new Cliente(
                clienteExistente.getId(),
                nome,
                command.telefone(),
                command.whatsapp(),
                command.instagram(),
                clienteExistente.getDataCadastro(),
                command.anotacoes(),
                enderecoAtualizado,
                clienteExistente.isAtivo(),
                clienteExistente.isDeletado()
        );

        return clienteRepositoryPort.salvar(clienteAtualizado);
    }
}
