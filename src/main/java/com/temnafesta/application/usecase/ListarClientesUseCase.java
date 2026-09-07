package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.ListarClientesQuery;
import com.temnafesta.domain.model.Cliente;
import com.temnafesta.domain.ports.repository.ClienteRepositoryPort;

import java.util.List;

public class ListarClientesUseCase {

    private final ClienteRepositoryPort clienteRepositoryPort;

    public ListarClientesUseCase(ClienteRepositoryPort clienteRepositoryPort) {
        this.clienteRepositoryPort = clienteRepositoryPort;
    }

    public List<Cliente> executar(ListarClientesQuery query) {
        // A regra de negócio "somente não deletados" é delegada para a interface do repositório, já que
        // não é possível fazer essa verificação no domínio
        return clienteRepositoryPort.listarNaoDeletados(
                query.termoBusca(),
                query.pagina(),
                query.tamanho()
        );
    }
}