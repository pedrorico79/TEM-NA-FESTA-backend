package com.temnafesta.service;

import com.temnafesta.dto.historicosStatusPedido.HistoricoStatusPedidoResponseDto;
import com.temnafesta.exception.pedido.PedidoNaoEncontrado;
import com.temnafesta.mapper.HistoricoStatusPedidoMapper;
import com.temnafesta.model.HistoricoStatusPedido;
import com.temnafesta.repository.HistoricoStatusPedidoRepository;
import com.temnafesta.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoricoStatusPedidoService {

    private final HistoricoStatusPedidoRepository repository;
    private final PedidoRepository pedidoRepository;

    public HistoricoStatusPedidoService(
            HistoricoStatusPedidoRepository repository,
            PedidoRepository pedidoRepository) {

        this.repository = repository;
        this.pedidoRepository = pedidoRepository;
    }

    public List<HistoricoStatusPedidoResponseDto> listarPorPedido(
            Integer pedidoId
    ) {

        pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new PedidoNaoEncontrado(pedidoId));

        return repository
                .findByPedidoIdOrderByDataAlteracaoDesc(pedidoId)
                .stream()
                .map(HistoricoStatusPedidoMapper::toResponseDto)
                .toList();
    }
}