package com.temnafesta.listener;

import com.temnafesta.event.StatusPedidoAlteradoEvent;
import com.temnafesta.model.HistoricoStatusPedido;
import com.temnafesta.repository.HistoricoStatusPedidoRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class HistoricoStatusPedidoListener {

    private final HistoricoStatusPedidoRepository historicoRepository;

    public HistoricoStatusPedidoListener(HistoricoStatusPedidoRepository historicoRepository) {
        this.historicoRepository = historicoRepository;
    }

    @EventListener
    public void onStatusAlterado(StatusPedidoAlteradoEvent event) {
        HistoricoStatusPedido historico = new HistoricoStatusPedido();
        historico.setPedido(event.getPedido());
        historico.setUsuario(event.getUsuario());
        historico.setStatusProducao(event.getPedido().getStatusProducao());
        historico.setDataAlteracao(LocalDateTime.now());
        historicoRepository.save(historico);
    }
}