package com.temnafesta.infrastructure.persistence;

import com.temnafesta.infrastructure.persistence.repository.SpringDataPedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class NormalizarVersaoPedidoRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(NormalizarVersaoPedidoRunner.class);
    private final SpringDataPedidoRepository repository;

    public NormalizarVersaoPedidoRunner(SpringDataPedidoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        int corrigidos = repository.inicializarVersoesNulas();
        if (corrigidos > 0) {
            log.warn("Inicializada a versão de {} pedido(s) legado(s) que estavam sem valor.", corrigidos);
        }
    }
}
