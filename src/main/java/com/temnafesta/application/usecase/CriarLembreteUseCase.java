package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.CriarLembreteCommand;
import com.temnafesta.domain.model.Lembrete;
import com.temnafesta.domain.ports.out.repository.LembreteRepositoryPort;
import com.temnafesta.domain.ports.out.repository.LembreteRepositoryPort;
import java.time.Clock;
import java.time.LocalDate;

public class CriarLembreteUseCase {

    private final LembreteRepositoryPort lembreteRepositoryPort;
    private final Clock relogioNegocio;

    public CriarLembreteUseCase(LembreteRepositoryPort lembreteRepositoryPort) {
        this(lembreteRepositoryPort, com.temnafesta.domain.time.RelogioNegocio.sistema());
    }

    public CriarLembreteUseCase(LembreteRepositoryPort lembreteRepositoryPort, Clock relogioNegocio) {
        this.lembreteRepositoryPort = lembreteRepositoryPort;
        this.relogioNegocio = relogioNegocio != null
                ? relogioNegocio : com.temnafesta.domain.time.RelogioNegocio.sistema();
    }

    public Lembrete executar(CriarLembreteCommand command) {
        Lembrete novoLembrete = new Lembrete(
                null,
                command.descricao(),
                LocalDate.now(relogioNegocio),
                command.dataLimite(),
                command.usuarioId()
        );

        return lembreteRepositoryPort.salvar(novoLembrete);
    }
}