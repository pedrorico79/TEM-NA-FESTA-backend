package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.CriarClienteCommand;
import com.temnafesta.domain.model.Cliente;
import com.temnafesta.domain.model.Endereco;
import com.temnafesta.domain.ports.repository.ClienteRepositoryPort;

import java.time.LocalDate;

public class CriarClienteUseCase {

    private final ClienteRepositoryPort clienteRepositoryPort;

    public CriarClienteUseCase(ClienteRepositoryPort clienteRepositoryPort) {
        this.clienteRepositoryPort = clienteRepositoryPort;
    }

    public Cliente executar(CriarClienteCommand command) {
        Endereco endereco = null;

        if (command.endereco() != null) {
            endereco = new Endereco(
                    null,
                    command.endereco().cep(),
                    command.endereco().logradouro(),
                    command.endereco().numero(),
                    command.endereco().complemento(),
                    command.endereco().bairro(),
                    command.endereco().cidade(),
                    command.endereco().estado()
            );
        }

        Cliente novoCliente = new Cliente(
                null,
                command.nome(),
                command.telefone(),
                command.whatsapp(),
                command.instagram(),
                LocalDate.now(),
                command.anotacoes(),
                endereco,
                true,
                false
        );

        return clienteRepositoryPort.salvar(novoCliente);
    }
}