package com.temnafesta.service;


import com.temnafesta.exception.cliente.ClienteComPedidosAtivosException;
import com.temnafesta.exception.cliente.ClienteNaoEncontrado;
import com.temnafesta.exception.endereco.EnderecoNaoEncontrado;
import com.temnafesta.model.Cliente;
import com.temnafesta.model.Endereco;
import com.temnafesta.repository.ClienteRepository;
import com.temnafesta.repository.EnderecoRepository;
import com.temnafesta.repository.PedidoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Cliente criar(Cliente cliente, Integer enderecoId) {

        Endereco enderecoEntidade = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new EnderecoNaoEncontrado(enderecoId));
        cliente.setEndereco(enderecoEntidade);
        return clienteRepository.save(cliente);
    }

    public Page<Cliente> listar(String busca, Pageable pageable) {
        String filtro = busca != null ? busca : "";
        return clienteRepository.findByIsDeletadoFalseAndNomeContainingIgnoreCase(filtro, pageable);
    }

    public Cliente buscarPorId(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontrado(id));
    }

    public Cliente atualizar(Integer id, Cliente clienteAtualizado, Integer enderecoId) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontrado(id));

        Endereco enderecoEntidade = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new EnderecoNaoEncontrado(enderecoId));

        cliente.setNome(clienteAtualizado.getNome());
        cliente.setTelefone(clienteAtualizado.getTelefone());
        cliente.setWhatsapp(clienteAtualizado.getWhatsapp());
        cliente.setInstagram(clienteAtualizado.getInstagram());
        cliente.setAnotacoes(clienteAtualizado.getAnotacoes());
        cliente.setEndereco(enderecoEntidade);

        return clienteRepository.save(cliente);
    }

    public void toggleAtivo(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontrado(id));

        if (cliente.getIsAtivo()) {
            Boolean temPedidosAtivos = pedidoRepository.existsPedidosAtivosParaCliente(id);
            if (temPedidosAtivos) throw new ClienteComPedidosAtivosException(id);
        }

        cliente.setIsAtivo(!cliente.getIsAtivo());
        clienteRepository.save(cliente);
    }

    public void deletar(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontrado(id));
        cliente.setIsDeletado(true);
        cliente.setIsAtivo(false);
        clienteRepository.save(cliente);
    }
}
