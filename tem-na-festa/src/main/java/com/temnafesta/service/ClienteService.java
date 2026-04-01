package com.temnafesta.service;


import com.temnafesta.exception.cliente.ClienteNaoEncontrado;
import com.temnafesta.exception.endereco.EnderecoNaoEncontrado;
import com.temnafesta.model.Cliente;
import com.temnafesta.model.Endereco;
import com.temnafesta.repository.ClienteRepository;
import com.temnafesta.repository.EnderecoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final EnderecoRepository enderecoRepository;

    public ClienteService(ClienteRepository clienteRepository, EnderecoRepository enderecoRepository) {
        this.clienteRepository = clienteRepository;
        this.enderecoRepository = enderecoRepository;
    }

    public Cliente criar(Cliente cliente, Integer enderecoId){

        Endereco enderecoEntidade = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new EnderecoNaoEncontrado(enderecoId));
        cliente.setEndereco(enderecoEntidade);
        return clienteRepository.save(cliente);
    }

    public List<Cliente> listar(){
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

    public void deletar(Integer id) {
        if (!clienteRepository.existsById(id)) {
            throw new ClienteNaoEncontrado(id);
        }

        clienteRepository.deleteById(id);
    }
}
