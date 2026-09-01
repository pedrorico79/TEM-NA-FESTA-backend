package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.AtualizarClienteCommand;
import com.temnafesta.domain.model.Cliente;
import com.temnafesta.domain.model.Endereco;
import com.temnafesta.domain.ports.repository.ClienteRepositoryPort;

import java.time.LocalDate;

public class AtualizarClienteUseCase {
    private final ClienteRepositoryPort clienteRepositoryPort;

    public AtualizarClienteUseCase(ClienteRepositoryPort clienteRepositoryPort) {
        this.clienteRepositoryPort = clienteRepositoryPort;
    }

    public Cliente executar(Long id, AtualizarClienteCommand command){
        Endereco enderecoAtualizado = null;

        if (command.endereco() != null) {
            enderecoAtualizado = new Endereco(
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

        Cliente ClienteAtualizado = new Cliente(
                id,
                command.nome(),
                command.telefone(),
                command.whatsapp(),
                command.instagram(),
                LocalDate.now(),
                command.anotacoes(),
                enderecoAtualizado,
                true,
                false
        );

        return clienteRepositoryPort.atualizar(ClienteAtualizado);
    }
}
