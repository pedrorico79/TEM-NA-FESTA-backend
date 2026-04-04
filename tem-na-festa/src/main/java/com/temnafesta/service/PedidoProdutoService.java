package com.temnafesta.service;

import com.temnafesta.exception.pedido.PedidoNaoEncontrado;
import com.temnafesta.exception.pedidoproduto.PedidoProdutoNaoEncontrado;
import com.temnafesta.exception.produto.ProdutoNaoEncontrado;

import com.temnafesta.model.Pedido;
import com.temnafesta.model.PedidoProduto;
import com.temnafesta.model.Produto;
import com.temnafesta.repository.PedidoProdutoRepository;
import com.temnafesta.repository.PedidoRepository;
import com.temnafesta.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoProdutoService {

    private final PedidoProdutoRepository pedidoProdutoRepository;
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoProdutoService(PedidoProdutoRepository pedidoProdutoRepository, PedidoRepository pedidoRepository, ProdutoRepository produtoRepository) {
        this.pedidoProdutoRepository = pedidoProdutoRepository;
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
    }

    public PedidoProduto criar(PedidoProduto pedidoProduto, Integer pedidoId, Integer produtoId){
        Pedido pedido = pedidoRepository.findById(pedidoId).
                orElseThrow(() -> new PedidoNaoEncontrado(pedidoId));
        Produto produto = produtoRepository.findById(produtoId).
                orElseThrow(() -> new ProdutoNaoEncontrado(produtoId));
        pedidoProduto.setPedido(pedido);
        pedidoProduto.setProduto(produto);
        return pedidoProdutoRepository.save(pedidoProduto);
    }

    public List<PedidoProduto> listar(){return pedidoProdutoRepository.findAll();}

    public List<PedidoProduto> listarPorPedido(Integer pedidoId){
        return pedidoProdutoRepository.findByPedidoId(pedidoId);
    }

    public PedidoProduto buscarPorId(Integer id){
        return pedidoProdutoRepository.findById(id)
                .orElseThrow(() -> new PedidoProdutoNaoEncontrado(id));
    }

    public PedidoProduto atualizar(Integer id, PedidoProduto pedidoProdutoAtualizado, Integer pedidoId, Integer produtoId){
        if(!pedidoProdutoRepository.existsById(id)){ throw new PedidoProdutoNaoEncontrado(id);}
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(()->new PedidoNaoEncontrado(pedidoId));
        Produto produto = produtoRepository.findById(produtoId).orElseThrow(()->new ProdutoNaoEncontrado(produtoId));

        pedidoProdutoAtualizado.setPedido(pedido);
        pedidoProdutoAtualizado.setProduto(produto);
        pedidoProdutoAtualizado.setId(id);
        return pedidoProdutoRepository.save(pedidoProdutoAtualizado);
    }

    public void deletar(Integer id) {
        if (!pedidoProdutoRepository.existsById(id)){throw new PedidoProdutoNaoEncontrado(id);}
        pedidoProdutoRepository.deleteById(id);
    }

}
