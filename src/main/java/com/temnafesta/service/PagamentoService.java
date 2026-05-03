package com.temnafesta.service;

import com.temnafesta.dto.pagamento.PagamentoRequestDto;
import com.temnafesta.exception.pagamento.PagamentoNaoEncontrado;
import com.temnafesta.exception.pedido.PedidoNaoEncontrado;
import com.temnafesta.exception.usuario.UsuarioNaoEncontrado;
import com.temnafesta.mapper.PagamentoMapper;
import com.temnafesta.model.Pagamento;
import com.temnafesta.model.Pedido;
import com.temnafesta.model.Usuario;
import com.temnafesta.repository.PagamentoRepository;
import com.temnafesta.repository.PedidoRepository;
import com.temnafesta.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository,
                            PedidoRepository pedidoRepository,
                            UsuarioRepository usuarioRepository) {
        this.pagamentoRepository = pagamentoRepository;
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Pagamento criar(PagamentoRequestDto dto) {
        Pedido pedido = pedidoRepository.findById(dto.getPedidoId())
                .orElseThrow(() -> new PedidoNaoEncontrado(dto.getPedidoId()));

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new UsuarioNaoEncontrado(dto.getUsuarioId()));

        Pagamento pagamento = PagamentoMapper.toEntity(dto);
        pagamento.setPedido(pedido);
        pagamento.setUsuario(usuario);
        pagamento.setDataPagamento(LocalDateTime.now());

        return pagamentoRepository.save(pagamento);
    }

    public Pagamento buscarPorId(Integer id) {
        return pagamentoRepository.findById(id)
                .orElseThrow(() -> new PagamentoNaoEncontrado(id));
    }

    public List<Pagamento> listar() {
        return pagamentoRepository.findAll();
    }

    public List<Pagamento> listarPorPedido(Integer pedidoId) {
        if (!pedidoRepository.existsById(pedidoId)) {
            throw new PedidoNaoEncontrado(pedidoId);
        }
        return pagamentoRepository.findByPedidoId(pedidoId);
    }

    public void deletar(Integer id) {
        if (!pagamentoRepository.existsById(id)) {
            throw new PagamentoNaoEncontrado(id);
        }
        pagamentoRepository.deleteById(id);
    }
}