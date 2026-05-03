package com.temnafesta.service;

import com.temnafesta.dto.pedido.PedidoResponseDto;
import com.temnafesta.event.StatusPedidoAlteradoEvent;
import com.temnafesta.exception.campanha.CampanhaNaoEncontrada;
import com.temnafesta.exception.cliente.ClienteNaoEncontrado;
import com.temnafesta.exception.pedido.PedidoNaoEncontrado;
import com.temnafesta.exception.usuario.UsuarioNaoEncontrado;
import com.temnafesta.mapper.PedidoMapper;
import com.temnafesta.model.*;
import com.temnafesta.repository.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final CampanhaRepository campanhaRepository;
    private final PagamentoRepository pagamentoRepository;
    private final ApplicationEventPublisher eventPublisher;

    public PedidoService(PedidoRepository pedidoRepository, ClienteRepository clienteRepository, UsuarioRepository usuarioRepository, CampanhaRepository campanhaRepository, PagamentoRepository pagamentoRepository, ApplicationEventPublisher eventPublisher) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.campanhaRepository = campanhaRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.eventPublisher = eventPublisher;
    }

    public Pedido criar(Pedido pedido, Integer clienteId, Integer usuarioId,
                        StatusProducao statusProducao, Integer campanhaId) {

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ClienteNaoEncontrado(clienteId));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontrado(usuarioId));

        Campanha campanha = campanhaRepository.findById(campanhaId)
                .orElseThrow(() -> new CampanhaNaoEncontrada(campanhaId));

        pedido.setCliente(cliente);
        pedido.setUsuario(usuario);
        pedido.setStatusProducao(statusProducao);
        pedido.setCampanha(campanha);
        pedido.setDataPedido(LocalDateTime.now());

        return pedidoRepository.save(pedido);
    }

    public List<PedidoResponseDto> listarTodos() {
        return pedidoRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public List<PedidoResponseDto> listarPedidosValidos() {
        return pedidoRepository.findApenasPedidosValidos()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<PedidoResponseDto> listarPedidosEmAndamento() {
        return pedidoRepository.findPedidosEmAndamento()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<PedidoResponseDto> listarPorStatus(StatusProducao status) {
        return pedidoRepository.findByStatusProducao(status)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public PedidoResponseDto buscarPorId(Integer id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNaoEncontrado(id));
        return toDto(pedido);
    }

    public Pedido atualizar(Integer id, Pedido pedidoAtualizado, Integer clienteId,
                            Integer usuarioId, StatusProducao statusProducao, Integer campanhaId) {

        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNaoEncontrado(id));

        StatusProducao statusAnterior = pedidoExistente.getStatusProducao();

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ClienteNaoEncontrado(clienteId));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontrado(usuarioId));

        Campanha campanha = campanhaRepository.findById(campanhaId)
                .orElseThrow(() -> new CampanhaNaoEncontrada(campanhaId));

        pedidoExistente.setDataEntrega(pedidoAtualizado.getDataEntrega());
        pedidoExistente.setValorTotal(pedidoAtualizado.getValorTotal());
        pedidoExistente.setObservacao(pedidoAtualizado.getObservacao());
        pedidoExistente.setCliente(cliente);
        pedidoExistente.setUsuario(usuario);
        pedidoExistente.setStatusProducao(statusProducao);
        pedidoExistente.setCampanha(campanha);
        Pedido salvo = pedidoRepository.save(pedidoExistente);

        if (!statusAnterior.equals(statusProducao)) {
            eventPublisher.publishEvent(
                    new StatusPedidoAlteradoEvent(salvo, statusAnterior, pedidoExistente.getUsuario())
            );
        }

        return salvo;
    }


    public void cancelar(Integer id, Integer usuarioId) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNaoEncontrado(id));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontrado(usuarioId));

        pedido.setStatusProducao(StatusProducao.CANCELADO);

        // add no historico
        HistoricoStatusPedido historico = new HistoricoStatusPedido();
        historico.setStatusProducao(StatusProducao.CANCELADO);
        historico.setDataAlteracao(LocalDateTime.now());
        historico.setPedido(pedido);
        historico.setUsuario(usuario);

        pedido.getHistoricoStatus().add(historico);
        pedidoRepository.save(pedido);
    }


    private PedidoResponseDto toDto(Pedido pedido) {
        BigDecimal valorPago = pagamentoRepository.somarPagamentosPorPedido(pedido.getId());
        return PedidoMapper.toResponseDto(pedido, valorPago);
    }
}