package com.temnafesta.infrastructure.config;

import com.temnafesta.application.usecase.*;
import com.temnafesta.domain.ports.repository.*;
import com.temnafesta.infrastructure.security.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfiguration {

    @Bean
    public AutenticarUsuarioUseCase autenticarUsuarioUseCase(
            UsuarioRepositoryPort usuarioRepositoryPort,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider) {
        return new AutenticarUsuarioUseCase(usuarioRepositoryPort, passwordEncoder, jwtTokenProvider);
    }

    @Bean
    public CriarPedidoInternoUseCase criarPedidoInternoUseCase(
            PedidoRepositoryPort pedidoRepositoryPort,
            ClienteRepositoryPort clienteRepositoryPort,
            ProdutoRepositoryPort produtoRepositoryPort) {
        return new CriarPedidoInternoUseCase(pedidoRepositoryPort, clienteRepositoryPort, produtoRepositoryPort);
    }

    @Bean
    public AlterarStatusPedidoUseCase alterarStatusPedidoUseCase(
            PedidoRepositoryPort pedidoRepositoryPort,
            HistoricoStatusPedidoRepositoryPort historicoStatusPedidoRepositoryPort) {
        return new AlterarStatusPedidoUseCase(pedidoRepositoryPort, historicoStatusPedidoRepositoryPort);
    }

    @Bean
    public RegistrarPagamentoUseCase registrarPagamentoUseCase(PedidoRepositoryPort pedidoRepositoryPort) {
        return new RegistrarPagamentoUseCase(pedidoRepositoryPort);
    }

    @Bean
    public GerarReciboDigitalUseCase gerarReciboDigitalUseCase(
            PedidoRepositoryPort pedidoRepositoryPort,
            ClienteRepositoryPort clienteRepositoryPort) {
        return new GerarReciboDigitalUseCase(pedidoRepositoryPort, clienteRepositoryPort);
    }

    @Bean
    public ListarEventosAtivosUseCase listarEventosAtivosUseCase(EventoRepositoryPort eventoRepositoryPort) {
        return new ListarEventosAtivosUseCase(eventoRepositoryPort);
    }

    @Bean
    public ListarMetodosPagamentoUseCase listarMetodosPagamentoUseCase(
            MetodoPagamentoRepositoryPort metodoPagamentoRepositoryPort) {
        return new ListarMetodosPagamentoUseCase(metodoPagamentoRepositoryPort);
    }

    @Bean
    public ListarProdutosUseCase listarProdutosUseCase(ProdutoRepositoryPort produtoRepositoryPort) {
        return new ListarProdutosUseCase(produtoRepositoryPort);
    }

    @Bean
    public CriarProdutoUseCase criarProdutoUseCase(ProdutoRepositoryPort produtoRepositoryPort) {
        return new CriarProdutoUseCase(produtoRepositoryPort);
    }

    @Bean
    public AtualizarProdutoUseCase atualizarProdutoUseCase(ProdutoRepositoryPort produtoRepositoryPort) {
        return new AtualizarProdutoUseCase(produtoRepositoryPort);
    }

    @Bean
    public DeletarProdutoUseCase deletarProdutoUseCase(ProdutoRepositoryPort produtoRepositoryPort) {
        return new DeletarProdutoUseCase(produtoRepositoryPort);
    }

    @Bean
    public AlterarAtivoProdutoUseCase alterarAtivoProdutoUseCase(ProdutoRepositoryPort produtoRepositoryPort) {
        return new AlterarAtivoProdutoUseCase(produtoRepositoryPort);
    }

    @Bean
    public CriarLembreteUseCase criarLembreteUseCase(LembreteRepositoryPort lembreteRepositoryPort) {
        return new CriarLembreteUseCase(lembreteRepositoryPort);
    }

    @Bean
    public ListarLembretesUsuarioUseCase listarLembretesUsuarioUseCase(LembreteRepositoryPort lembreteRepositoryPort) {
        return new ListarLembretesUsuarioUseCase(lembreteRepositoryPort);
    }

    @Bean
    public DeletarLembreteUseCase deletarLembreteUseCase(LembreteRepositoryPort lembreteRepositoryPort) {
        return new DeletarLembreteUseCase(lembreteRepositoryPort);
    }

    @Bean
    public CriarClienteUseCase criarClienteUseCase(ClienteRepositoryPort clienteRepositoryPort) {
        return new CriarClienteUseCase(clienteRepositoryPort);
    }

    @Bean
    public ListarClientesUseCase listarClientesUseCase(ClienteRepositoryPort clienteRepositoryPort) {
        return new ListarClientesUseCase(clienteRepositoryPort);
    }

    @Bean
    public BuscarClientePorIdUseCase buscarClientePorIdUseCase(ClienteRepositoryPort clienteRepositoryPort) {
        return new BuscarClientePorIdUseCase(clienteRepositoryPort);
    }

    @Bean
    public AtualizarClienteUseCase atualizarClienteUseCase(ClienteRepositoryPort clienteRepositoryPort) {
        return new AtualizarClienteUseCase(clienteRepositoryPort);
    }

    @Bean
    public AlternarAtivoClienteUseCase alternarAtivoClienteUseCase(
            ClienteRepositoryPort clienteRepositoryPort,
            PedidoRepositoryPort pedidoRepositoryPort) {
        return new AlternarAtivoClienteUseCase(clienteRepositoryPort, pedidoRepositoryPort);
    }

    @Bean
    public ListarPedidoPorIdUseCase listarPedidoPorIdUseCase(
            PedidoRepositoryPort pedidoRepositoryPort) {
        return new ListarPedidoPorIdUseCase(pedidoRepositoryPort);
    }

    @Bean
    public AtualizarPedidoUseCase atualizarPedidoUseCase(PedidoRepositoryPort pedidoRepositoryPort, ProdutoRepositoryPort produtoRepositoryPort) {
        return new AtualizarPedidoUseCase(pedidoRepositoryPort, produtoRepositoryPort);
    }

    @Bean
    public ExcluirPedidoUseCase excluirPedidoUseCase(PedidoRepositoryPort pedidoRepositoryPort) {
        return new ExcluirPedidoUseCase(pedidoRepositoryPort);
    }

    @Bean
    public ContarPorStatusUseCase contarPorStatusUseCase(
            PedidoRepositoryPort pedidoRepositoryPort) {
        return new ContarPorStatusUseCase(pedidoRepositoryPort);
    }

    @Bean
    public ListarProximasRetiradasUseCase listarProximasRetiradasUseCase(
            PedidoRepositoryPort pedidoRepositoryPort) {
        return new ListarProximasRetiradasUseCase(pedidoRepositoryPort);
    }

    @Bean
    public ListarPedidosUseCase listarPedidosUseCase(
            PedidoRepositoryPort pedidoRepositoryPort) {

        return new ListarPedidosUseCase(pedidoRepositoryPort);
    }

    @Bean
    public ListarItemPedidoPorIdUseCase listarItemPedidoPorIdUseCase(
            PedidoRepositoryPort pedidoRepositoryPort) {

        return new ListarItemPedidoPorIdUseCase(pedidoRepositoryPort);
    }

    @Bean
    public ListarPagamentosPedidoUseCase listarPagamentosPedidoUseCase(
            PedidoRepositoryPort pedidoRepositoryPort) {
        return new ListarPagamentosPedidoUseCase(pedidoRepositoryPort);
    }

    @Bean
    public CriarEventoUseCase criarEventoUseCase(EventoRepositoryPort eventoRepositoryPort) {
        return new CriarEventoUseCase(eventoRepositoryPort);
    }

    @Bean
    public AtualizarEventoUseCase atualizarEventoUseCase(EventoRepositoryPort eventoRepositoryPort) {
        return new AtualizarEventoUseCase(eventoRepositoryPort);
    }

    @Bean
    public DeletarEventoUseCase deletarEventoUseCase(EventoRepositoryPort eventoRepositoryPort) {
        return new DeletarEventoUseCase(eventoRepositoryPort);
    }

    @Bean
    public AlterarStatusEventoUseCase alterarStatusEventoUseCase(EventoRepositoryPort eventoRepositoryPort) {
        return new AlterarStatusEventoUseCase(eventoRepositoryPort);
    }

    @Bean
    public ListarUsuarioUseCase listarUsuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort) {
        return new ListarUsuarioUseCase(usuarioRepositoryPort);
    }

    @Bean
    public CriarUsuarioUseCase criarUsuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort,
                                                   PerfilRepositoryPort perfilRepositoryPort,
                                                   PasswordEncoder passwordEncoder) {
        return new CriarUsuarioUseCase(usuarioRepositoryPort, perfilRepositoryPort, passwordEncoder);
    }

    @Bean
    public AtualizarUsuarioUseCase atualizarUsuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort,
                                                           PerfilRepositoryPort perfilRepositoryPort) {
        return new AtualizarUsuarioUseCase(usuarioRepositoryPort, perfilRepositoryPort);
    }

    @Bean
    public DeletarUsuarioUseCase deletarUsuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort) {
        return new DeletarUsuarioUseCase(usuarioRepositoryPort);
    }
}