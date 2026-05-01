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

    public PedidoProdutoService(
            PedidoProdutoRepository pedidoProdutoRepository,
            PedidoRepository pedidoRepository,
            ProdutoRepository produtoRepository
    ) {
        this.pedidoProdutoRepository = pedidoProdutoRepository;
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
    }

    public PedidoProduto criar(PedidoProduto pedidoProduto, Integer pedidoId, Integer produtoId) {
        Pedido pedido = buscarPedidoOuLancar(pedidoId);
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ProdutoNaoEncontrado(produtoId));

        pedidoProduto.setPedido(pedido);
        pedidoProduto.setProduto(produto);

        return pedidoProdutoRepository.save(pedidoProduto);
    }


    public List<PedidoProduto> listarPorPedido(Integer pedidoId) {
        buscarPedidoOuLancar(pedidoId);
        return pedidoProdutoRepository.findByPedidoId(pedidoId);
    }

    public PedidoProduto buscarPorId(Integer pedidoId, Integer id) {
        buscarPedidoOuLancar(pedidoId);
        return pedidoProdutoRepository.findByIdAndPedidoId(id, pedidoId)
                .orElseThrow(() -> new PedidoProdutoNaoEncontrado(id));
    }

    public PedidoProduto atualizar(
            Integer id,
            PedidoProduto pedidoProdutoAtualizado,
            Integer pedidoId,
            Integer produtoId
    ) {
        Pedido pedido = buscarPedidoOuLancar(pedidoId);
        buscarPorId(pedidoId, id);
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ProdutoNaoEncontrado(produtoId));

        pedidoProdutoAtualizado.setPedido(pedido);
        pedidoProdutoAtualizado.setProduto(produto);
        pedidoProdutoAtualizado.setId(id);

        return pedidoProdutoRepository.save(pedidoProdutoAtualizado);
    }

    public void deletar(Integer pedidoId, Integer id) {
        PedidoProduto pedidoProduto = buscarPorId(pedidoId, id);
        pedidoProdutoRepository.deleteById(pedidoProduto.getId());
    }

    private Pedido buscarPedidoOuLancar(Integer pedidoId) {
        return pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new PedidoNaoEncontrado(pedidoId));
    }

}
