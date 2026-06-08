package com.temnafesta.service;

import com.temnafesta.dto.countPedidos.CountPedidosResponseDto;
import com.temnafesta.dto.pedido.ItemPedidoDto;
import com.temnafesta.dto.pedido.PedidoResponseDto;
import com.temnafesta.event.StatusPedidoAlteradoEvent;
import com.temnafesta.model.StatusProducao;
import com.temnafesta.exception.evento.EventoNaoEncontradoException;
import com.temnafesta.exception.cliente.ClienteNaoEncontrado;
import com.temnafesta.exception.pedido.PedidoNaoEncontrado;
import com.temnafesta.exception.produto.ProdutoNaoEncontrado;
import com.temnafesta.exception.usuario.UsuarioNaoEncontrado;
import com.temnafesta.mapper.PedidoMapper;
import com.temnafesta.mapper.ItemPedidoMapper;
import com.temnafesta.model.*;
import com.temnafesta.repository.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final EventoRepository eventoRepository;
    private final PagamentoRepository pagamentoRepository;
    private final StatusProducaoRepository statusProducaoRepository;
    private final ProdutoRepository produtoRepository;
    private final ApplicationEventPublisher eventPublisher;

    public PedidoService(PedidoRepository pedidoRepository,
                         ClienteRepository clienteRepository,
                         UsuarioRepository usuarioRepository,
                         EventoRepository eventoRepository,
                         PagamentoRepository pagamentoRepository,
                         StatusProducaoRepository statusProducaoRepository,
                         ProdutoRepository produtoRepository,
                         ApplicationEventPublisher eventPublisher) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.eventoRepository = eventoRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.statusProducaoRepository = statusProducaoRepository;
        this.produtoRepository = produtoRepository;
        this.eventPublisher = eventPublisher;
    }

//    public Pedido criar(Pedido pedido, Integer clienteId, Integer usuarioId,
//                        Integer statusProducaoId, Integer campanhaId) {
//
//        Cliente cliente = clienteRepository.findById(clienteId)
//                .orElseThrow(() -> new ClienteNaoEncontrado(clienteId));
//
//        Usuario usuario = usuarioRepository.findById(usuarioId)
//                .orElseThrow(() -> new UsuarioNaoEncontrado(usuarioId));
//
//        Evento evento = eventoRepository.findById(campanhaId)
//                .orElseThrow(() -> new EventoNaoEncontradoException(campanhaId));
//
//        StatusProducao statusProducao = statusProducaoRepository.findById(statusProducaoId)
//                .orElseThrow(() -> new RuntimeException("Status de produção não encontrado"));
//
//        pedido.setCliente(cliente);
//        pedido.setUsuario(usuario);
//        pedido.setStatusProducao(statusProducao);
//        pedido.setEvento(evento);
//        pedido.setDataPedido(LocalDateTime.now());
//
//        return pedidoRepository.save(pedido);
//    }

    @Transactional
    public Pedido criarComProdutos(Pedido pedido, Integer clienteId, Integer usuarioId,
                                   Integer statusProducaoId, Integer eventoId,
                                   List<ItemPedidoDto> itens) {

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ClienteNaoEncontrado(clienteId));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontrado(usuarioId));

        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new EventoNaoEncontradoException(eventoId));

        StatusProducao statusProducao = statusProducaoRepository.findById(statusProducaoId)
                .orElseThrow(() -> new RuntimeException("Status de produção não encontrado"));

        pedido.setCliente(cliente);
        pedido.setUsuario(usuario);
        pedido.setStatusProducao(statusProducao);
        pedido.setEvento(evento);
        pedido.setDataPedido(LocalDateTime.now());

        BigDecimal valorTotal = BigDecimal.ZERO;

        for (ItemPedidoDto item : itens) {
            Produto produto = produtoRepository.findById(item.getProdutoId())
                    .orElseThrow(() -> new ProdutoNaoEncontrado(item.getProdutoId()));

            ItemPedido itemPedido = ItemPedidoMapper.toEntity(item);
            itemPedido.setProduto(produto);
            itemPedido.setPedido(pedido);
            pedido.getProdutos().add(itemPedido);

            BigDecimal subtotal = BigDecimal.valueOf(item.getQuantidade())
                    .multiply(item.getPrecoUnitario());
            valorTotal = valorTotal.add(subtotal);
        }

        pedido.setValorTotal(valorTotal);

        return pedidoRepository.save(pedido);
    }


    public List<PedidoResponseDto> listarTodos() {
        return pedidoRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    //    public List<PedidoResponseDto> listarPedidosValidos() {
//        return pedidoRepository.findApenasPedidosValidos()
//                .stream()
//                .map(this::toDto)
//                .toList();
//    }
//
//    public List<PedidoResponseDto> listarPedidosEmAndamento() {
//        return pedidoRepository.findPedidosEmAndamento()
//                .stream()
//                .map(this::toDto)
//                .toList();
//    }
//
//    public List<PedidoResponseDto> listarPorStatus(Integer statusId) {
//        StatusProducao status = statusProducaoRepository.findById(statusId)
//                .orElseThrow(() -> new RuntimeException("Status de produção não encontrado"));
//
//        return pedidoRepository.findByStatusProducao(status)
//                .stream()
//                .map(this::toDto)
//                .toList();
//    }
    public Map<String, Long> countByStatus() {
        return pedidoRepository.countByStatusRaw()
                .stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1]
                ));
    }

    public Page<PedidoResponseDto> listar(String busca, Integer statusId, Integer eventoId, Pageable pageable) {
        return pedidoRepository.buscarComFiltros(busca, statusId, eventoId, pageable)
                .map(this::toDto);
    }


    public PedidoResponseDto buscarPorId(Integer id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNaoEncontrado(id));
        return toDto(pedido);
    }

    public Pedido atualizar(Integer id, Pedido pedidoAtualizado, Integer clienteId,
                            Integer usuarioId, Integer statusProducaoId, Integer campanhaId,
                            List<ItemPedidoDto> itens) {

        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNaoEncontrado(id));

        StatusProducao statusAnterior = pedidoExistente.getStatusProducao();

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ClienteNaoEncontrado(clienteId));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontrado(usuarioId));

        Evento evento = eventoRepository.findById(campanhaId)
                .orElseThrow(() -> new EventoNaoEncontradoException(campanhaId));

        StatusProducao statusProducao = statusProducaoRepository.findById(statusProducaoId)
                .orElseThrow(() -> new RuntimeException("Status de produção não encontrado"));

        pedidoExistente.setDataEntrega(pedidoAtualizado.getDataEntrega());
//        pedidoExistente.setValorTotal(pedidoAtualizado.getValorTotal());
        pedidoExistente.setObservacao(pedidoAtualizado.getObservacao());
        pedidoExistente.setCliente(cliente);
        pedidoExistente.setUsuario(usuario);
        pedidoExistente.setStatusProducao(statusProducao);
        pedidoExistente.setEvento(evento);

        pedidoExistente.getProdutos().clear();
        BigDecimal valorTotal = BigDecimal.ZERO;

        for (ItemPedidoDto item : itens) {
            Produto produto = produtoRepository.findById(item.getProdutoId())
                    .orElseThrow(() -> new ProdutoNaoEncontrado(item.getProdutoId()));

            ItemPedido itemPedido = ItemPedidoMapper.toEntity(item);
            itemPedido.setProduto(produto);
            itemPedido.setPedido(pedidoExistente);
            pedidoExistente.getProdutos().add(itemPedido);

            valorTotal = valorTotal.add(
                    BigDecimal.valueOf(item.getQuantidade())
                            .multiply(item.getPrecoUnitario())
            );
        }

        pedidoExistente.setValorTotal(valorTotal);

        Pedido salvo = pedidoRepository.save(pedidoExistente);

        if (!statusAnterior.getId().equals(statusProducao.getId())) {
            eventPublisher.publishEvent(
                    new StatusPedidoAlteradoEvent(salvo, statusAnterior, pedidoExistente.getUsuario())
            );
        }

        return salvo;
    }


    public void cancelar(Integer id, Integer usuarioId) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNaoEncontrado(id));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontrado(usuarioId));

        StatusProducao statusAnterior = pedido.getStatusProducao();

        // Busca o status CANCELADO na tabela status_producao
        StatusProducao statusCancelado = statusProducaoRepository.findByNome("CANCELADO")
                .orElseThrow(() -> new RuntimeException("Status CANCELADO não encontrado"));

        // Compara por ID (mais seguro do que equals em entidades)
        if (statusAnterior != null && statusAnterior.getId().equals(statusCancelado.getId())) {
            return;
        }

        pedido.setStatusProducao(statusCancelado);

        Pedido salvo = pedidoRepository.save(pedido);

        eventPublisher.publishEvent(
                new StatusPedidoAlteradoEvent(
                        salvo,
                        statusAnterior,
                        usuario
                )
        );
    }

    public Page<PedidoResponseDto> listarProximasRetiradas(Integer dias, Integer page) {

        LocalDateTime agora = LocalDateTime.now();

        LocalDateTime limite = agora.plusDays(dias);

        Pageable pageable = PageRequest.of(
                page,
                10,
                Sort.by("dataEntrega").ascending()
        );
        return pedidoRepository
                .buscarProximasRetiradas(
                        agora,
                        limite,
                        pageable
                )
                .map(this::toDto);
    }

    public CountPedidosResponseDto contarPedidos(Integer dias) {

        if (dias == null || dias <= 0) {
            throw new IllegalArgumentException(
                    "O parâmetro dias deve ser maior que zero."
            );
        }

        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime limite = agora.plusDays(dias);

        List<Pedido> pedidos =
                pedidoRepository.countPedidos(
                        agora,
                        limite
                );

        long total = pedidos.size();

        long aguardandoInicio = pedidos.stream()
                .filter(p ->
                        "Aguardando Início".equalsIgnoreCase(
                                p.getStatusProducao().getNome()
                        )
                )
                .count();

        long pagamentoPendente = pedidos.stream()
                .filter(p -> {

                    BigDecimal valorPago =
                            pagamentoRepository
                                    .somarPagamentosPorPedido(
                                            p.getId()
                                    );

                    return valorPago.compareTo(
                            p.getValorTotal()
                    ) < 0;

                })
                .count();

        return new CountPedidosResponseDto(
                total,
                aguardandoInicio,
                pagamentoPendente
        );
    }



    private PedidoResponseDto toDto(Pedido pedido) {
        BigDecimal valorPago = pagamentoRepository.somarPagamentosPorPedido(pedido.getId());
        return PedidoMapper.toResponseDto(pedido, valorPago);
    }
}