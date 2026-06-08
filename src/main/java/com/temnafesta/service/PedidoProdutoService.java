package com.temnafesta.service;

import com.temnafesta.exception.pedido.PedidoNaoEncontrado;
import com.temnafesta.exception.pedidoproduto.PedidoProdutoNaoEncontrado;
import com.temnafesta.exception.produto.ProdutoNaoEncontrado;

import com.temnafesta.model.Pedido;
import com.temnafesta.model.ItemPedido;
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

    public ItemPedido criar(ItemPedido itemPedido, Integer pedidoId, Integer produtoId) {
        Pedido pedido = buscarPedidoOuLancar(pedidoId);
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ProdutoNaoEncontrado(produtoId));

        itemPedido.setPedido(pedido);
        itemPedido.setProduto(produto);

        return pedidoProdutoRepository.save(itemPedido);
    }


    public List<ItemPedido> listarPorPedido(Integer pedidoId) {
        buscarPedidoOuLancar(pedidoId);
        return pedidoProdutoRepository.findByPedidoId(pedidoId);
    }

    public ItemPedido buscarPorId(Integer pedidoId, Integer id) {
        buscarPedidoOuLancar(pedidoId);
        return pedidoProdutoRepository.findByIdAndPedidoId(id, pedidoId)
                .orElseThrow(() -> new PedidoProdutoNaoEncontrado(id));
    }

    public ItemPedido atualizar(
            Integer id,
            ItemPedido itemPedidoAtualizado,
            Integer pedidoId,
            Integer produtoId
    ) {
        Pedido pedido = buscarPedidoOuLancar(pedidoId);
        buscarPorId(pedidoId, id);
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ProdutoNaoEncontrado(produtoId));

        itemPedidoAtualizado.setPedido(pedido);
        itemPedidoAtualizado.setProduto(produto);
        itemPedidoAtualizado.setId(id);

        return pedidoProdutoRepository.save(itemPedidoAtualizado);
    }

    public void deletar(Integer pedidoId, Integer id) {
        ItemPedido itemPedido = buscarPorId(pedidoId, id);
        pedidoProdutoRepository.deleteById(itemPedido.getId());
    }

    private Pedido buscarPedidoOuLancar(Integer pedidoId) {
        return pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new PedidoNaoEncontrado(pedidoId));
    }

}
