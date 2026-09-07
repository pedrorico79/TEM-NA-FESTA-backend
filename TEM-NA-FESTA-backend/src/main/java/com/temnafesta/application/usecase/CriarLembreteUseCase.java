package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.CriarLembreteCommand;
import com.temnafesta.domain.model.Lembrete;
import com.temnafesta.domain.ports.repository.LembreteRepositoryPort;

public class CriarLembreteUseCase {

    private final LembreteRepositoryPort lembreteRepositoryPort;

    public CriarLembreteUseCase(LembreteRepositoryPort lembreteRepositoryPort) {
        this.lembreteRepositoryPort = lembreteRepositoryPort;
    }

    public Lembrete executar(CriarLembreteCommand command) {
        Lembrete novoLembrete = new Lembrete(
                null,
                command.descricao(),
                null, // A dataCriacao é setada como LocalDate.now() dentro da própria entidade
                command.dataLimite(),
                command.usuarioId()
        );

        return lembreteRepositoryPort.salvar(novoLembrete);
    }
}