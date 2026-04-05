package com.temnafesta.service;

import com.temnafesta.exception.cliente.ClienteNaoEncontrado;
import com.temnafesta.exception.pedido.PedidoNaoEncontrado;
import com.temnafesta.exception.statusProducao.StatusProducaoNaoEncontrado;
import com.temnafesta.exception.usuario.UsuarioNaoEncontrado;
import org.springframework.stereotype.Service;

import com.temnafesta.model.*;
import com.temnafesta.repository.*;

import java.time.LocalDate;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final StatusProducaoRepository statusRepository;

    public PedidoService(PedidoRepository pedidoRepository,
                         ClienteRepository clienteRepository,
                         UsuarioRepository usuarioRepository,
                         StatusProducaoRepository statusRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.statusRepository = statusRepository;
    }

    public Pedido criar(Pedido pedido, Integer clienteId, Integer usuarioId, Integer statusId) {

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ClienteNaoEncontrado(clienteId));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontrado(usuarioId));

        StatusProducao status = statusRepository.findById(statusId)
                .orElseThrow(() -> new StatusProducaoNaoEncontrado(statusId));

        pedido.setCliente(cliente);
        pedido.setUsuario(usuario);
        pedido.setStatusProducao(status);
        pedido.setDataPedido(LocalDate.now());

        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }

    public Pedido buscarPorId(Integer id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNaoEncontrado(id));
    }

    public Pedido atualizar(Integer id, Pedido pedidoAtualizado,
                            Integer clienteId, Integer usuarioId, Integer statusId) {

        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNaoEncontrado(id));

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ClienteNaoEncontrado(clienteId));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontrado(usuarioId));

        StatusProducao status = statusRepository.findById(statusId)
                .orElseThrow(() -> new StatusProducaoNaoEncontrado(statusId));


        pedidoExistente.setDataEntrega(pedidoAtualizado.getDataEntrega());
        pedidoExistente.setValorTotal(pedidoAtualizado.getValorTotal());
        pedidoExistente.setObservacao(pedidoAtualizado.getObservacao());

        pedidoExistente.setCliente(cliente);
        pedidoExistente.setUsuario(usuario);
        pedidoExistente.setStatusProducao(status);

        return pedidoRepository.save(pedidoExistente);
    }

    public void deletar(Integer id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNaoEncontrado(id));

        pedidoRepository.delete(pedido);
    }
}