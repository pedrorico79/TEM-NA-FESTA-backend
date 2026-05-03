package com.temnafesta.service;


import com.temnafesta.exception.cliente.ClienteComPedidosAtivosException;
import com.temnafesta.exception.cliente.ClienteNaoEncontrado;
import com.temnafesta.exception.endereco.EnderecoNaoEncontrado;
import com.temnafesta.model.Cliente;
import com.temnafesta.model.Endereco;
import com.temnafesta.repository.ClienteRepository;
import com.temnafesta.repository.EnderecoRepository;
import com.temnafesta.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final EnderecoRepository enderecoRepository;
    private final PedidoRepository pedidoRepository;

    public ClienteService(ClienteRepository clienteRepository,
                          EnderecoRepository enderecoRepository,
                          PedidoRepository pedidoRepository) {
        this.clienteRepository = clienteRepository;
        this.enderecoRepository = enderecoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public Cliente criar(Cliente cliente, Integer enderecoId){

        Endereco enderecoEntidade = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new EnderecoNaoEncontrado(enderecoId));
        cliente.setEndereco(enderecoEntidade);
        return clienteRepository.save(cliente);
    }

    public List<Cliente> listarAtivos(){
        return clienteRepository.findByIsAtivoTrue();
    }

    public List<Cliente> listarTodos(){
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Integer id){
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontrado(id));
    }

    public Cliente atualizar(Integer id, Cliente clienteAtualizado, Integer enderecoId){
        if (!clienteRepository.existsById(id)) {
            throw new ClienteNaoEncontrado(id);
        }
        Endereco enderecoEntidade = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new EnderecoNaoEncontrado(enderecoId));

        clienteAtualizado.setEndereco(enderecoEntidade);
        clienteAtualizado.setId(id);
        return clienteRepository.save(clienteAtualizado);
    }

    public void desativar(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontrado(id));
        Boolean temPedidosAtivos = pedidoRepository.existsPedidosAtivosParaCliente(id);
        if (temPedidosAtivos) {
            throw new ClienteComPedidosAtivosException(id);
        }

        cliente.setAtivo(false);
        clienteRepository.save(cliente);
    }

    public void reativar(Integer id){
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontrado(id));
        cliente.setAtivo(Boolean.TRUE);
        clienteRepository.save(cliente);
    }
}
