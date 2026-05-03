package com.temnafesta.service;


import com.temnafesta.exception.produto.ProdutoNaoEncontrado;
import com.temnafesta.model.Produto;
import com.temnafesta.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {
    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public Produto criar(Produto produto) {
        return repository.save(produto);
    }

    public List<Produto> listarAtivos() {
        return repository.findByIsAtivoTrue();
    }

    public List<Produto> listarTodos() {
        return repository.findAll();
    }

    public Produto buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontrado(id));
    }

    public Produto atualizar(Integer id, Produto produtoAtualizado) {

        Produto produto = repository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontrado(id));

        produto.setNome(produtoAtualizado.getNome());
        produto.setDescricao(produtoAtualizado.getDescricao());
        produto.setPrecoVenda(produtoAtualizado.getPrecoVenda());
        produto.setAtivo(produtoAtualizado.getAtivo());

        return repository.save(produto);
    }

    public void desativar(Integer id) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontrado(id));
        produto.setAtivo(false);
        repository.save(produto);
    }

    public void reativar(Integer id){
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontrado(id));
        produto.setAtivo(true);
        repository.save(produto);
    }
}
