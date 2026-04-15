package com.temnafesta.service;

import com.temnafesta.exception.cliente.ClienteNaoEncontrado;
import com.temnafesta.exception.pedido.PedidoNaoEncontrado;
import com.temnafesta.exception.usuario.UsuarioNaoEncontrado;
import org.springframework.stereotype.Service;

import com.temnafesta.model.*;
import com.temnafesta.repository.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;

    public PedidoService(PedidoRepository pedidoRepository,
                         ClienteRepository clienteRepository,
                         UsuarioRepository usuarioRepository){
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Pedido criar(Pedido pedido, Integer clienteId, Integer usuarioId, StatusProducao statusProducao) {

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ClienteNaoEncontrado(clienteId));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontrado(usuarioId));

        pedido.setCliente(cliente);
        pedido.setUsuario(usuario);
        pedido.setStatusProducao(statusProducao);
        pedido.setDataPedido(LocalDateTime.now());

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
                            Integer clienteId, Integer usuarioId, StatusProducao statusProducao) {

        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNaoEncontrado(id));

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ClienteNaoEncontrado(clienteId));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontrado(usuarioId));




        pedidoExistente.setDataEntrega(pedidoAtualizado.getDataEntrega());
        pedidoExistente.setValorTotal(pedidoAtualizado.getValorTotal());
        pedidoExistente.setObservacao(pedidoAtualizado.getObservacao());

        pedidoExistente.setCliente(cliente);
        pedidoExistente.setUsuario(usuario);
        pedidoExistente.setStatusProducao(statusProducao);

        return pedidoRepository.save(pedidoExistente);
    }

    public void deletar(Integer id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNaoEncontrado(id));

        pedidoRepository.delete(pedido);
    }
}