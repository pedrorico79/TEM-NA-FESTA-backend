package com.temnafesta.infrastructure.config;

import com.temnafesta.application.usecase.*;
import com.temnafesta.domain.ports.out.PedidoEventPublisherPort;
import com.temnafesta.domain.ports.out.repository.*;
import com.temnafesta.infrastructure.security.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;

@Configuration
public class BeanConfiguration {

    @Bean
    public Clock relogioNegocio() {
        return com.temnafesta.domain.time.RelogioNegocio.sistema();
    }

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
            ProdutoRepositoryPort produtoRepositoryPort,
            PedidoEventPublisherPort pedidoEventPublisherPort,
            Clock relogioNegocio) {

        return new CriarPedidoInternoUseCase(pedidoRepositoryPort, clienteRepositoryPort, produtoRepositoryPort, pedidoEventPublisherPort, relogioNegocio);
    }

    @Bean
    public AlterarStatusPedidoUseCase alterarStatusPedidoUseCase(
            PedidoRepositoryPort pedidoRepositoryPort,
            HistoricoStatusPedidoRepositoryPort historicoStatusPedidoRepositoryPort,
            ClienteRepositoryPort clienteRepositoryPort,
            PedidoEventPublisherPort pedidoEventPublisherPort,
            Clock relogioNegocio) {

        return new AlterarStatusPedidoUseCase(
                pedidoRepositoryPort,
                historicoStatusPedidoRepositoryPort,
                clienteRepositoryPort,
                pedidoEventPublisherPort,
                relogioNegocio);
    }

    @Bean
    public RegistrarPagamentoUseCase registrarPagamentoUseCase(PedidoRepositoryPort pedidoRepositoryPort,
                                                                MetodoPagamentoRepositoryPort metodoPagamentoRepositoryPort,
                                                                Clock relogioNegocio) {
        return new RegistrarPagamentoUseCase(pedidoRepositoryPort, metodoPagamentoRepositoryPort, relogioNegocio);
    }

    @Bean
    public ConfirmarPagamentoUseCase confirmarPagamentoUseCase(PedidoRepositoryPort pedidoRepositoryPort) {
        return new ConfirmarPagamentoUseCase(pedidoRepositoryPort);
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
    public ListarMetodosPagamentoUseCase listarMetodosPagamentoUseCase(MetodoPagamentoRepositoryPort metodoPagamentoRepositoryPort) {
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
    public CriarLembreteUseCase criarLembreteUseCase(LembreteRepositoryPort lembreteRepositoryPort, Clock relogioNegocio) {
        return new CriarLembreteUseCase(lembreteRepositoryPort, relogioNegocio);
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
    public AtualizarLembreteUseCase atualizarLembreteUseCase(LembreteRepositoryPort lembreteRepositoryPort) {
        return new AtualizarLembreteUseCase(lembreteRepositoryPort);
    }

    @Bean
    public CriarClienteUseCase criarClienteUseCase(ClienteRepositoryPort clienteRepositoryPort, Clock relogioNegocio) {
        return new CriarClienteUseCase(clienteRepositoryPort, relogioNegocio);
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
    public AlterarAtivoClienteUseCase alterarAtivoClienteUseCase(
            ClienteRepositoryPort clienteRepositoryPort,
            PedidoRepositoryPort pedidoRepositoryPort) {
        return new AlterarAtivoClienteUseCase(clienteRepositoryPort, pedidoRepositoryPort);
    }

    @Bean
    public DeletarClienteUseCase deletarClienteUseCase(
            ClienteRepositoryPort clienteRepositoryPort,
            PedidoRepositoryPort pedidoRepositoryPort) {
        return new DeletarClienteUseCase(clienteRepositoryPort, pedidoRepositoryPort);
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
            PedidoRepositoryPort pedidoRepositoryPort,
            Clock relogioNegocio) {
        return new ListarProximasRetiradasUseCase(pedidoRepositoryPort, relogioNegocio);
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
    public ExcluirPagamentoUseCase excluirPagamentoUseCase(
            PedidoRepositoryPort pedidoRepositoryPort) {
        return new ExcluirPagamentoUseCase(pedidoRepositoryPort);
    }

    @Bean
    public ListarHistoricoStatusPedidoUseCase listarHistoricoStatusPedidoUseCase(
            PedidoRepositoryPort pedidoRepositoryPort,
            HistoricoStatusPedidoRepositoryPort historicoStatusPedidoRepositoryPort) {
        return new ListarHistoricoStatusPedidoUseCase(pedidoRepositoryPort, historicoStatusPedidoRepositoryPort);
    }

    @Bean
    public ListarStatusProducaoUseCase listarStatusProducaoUseCase() {
        return new ListarStatusProducaoUseCase();
    }

    @Bean
    public BuscarLembretePorIdUseCase buscarLembretePorIdUseCase(LembreteRepositoryPort lembreteRepositoryPort) {
        return new BuscarLembretePorIdUseCase(lembreteRepositoryPort);
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
    public BuscarEventoPorIdUseCase buscarEventoPorIdUseCase(EventoRepositoryPort eventoRepositoryPort) {
        return new BuscarEventoPorIdUseCase(eventoRepositoryPort);
    }

    @Bean
    public ListarUsuarioUseCase listarUsuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort) {
        return new ListarUsuarioUseCase(usuarioRepositoryPort);
    }

    @Bean
    public CriarUsuarioUseCase criarUsuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort,
                                                   PerfilRepositoryPort perfilRepositoryPort,
                                                   PasswordEncoder passwordEncoder,
                                                   Clock relogioNegocio) {
        return new CriarUsuarioUseCase(usuarioRepositoryPort, perfilRepositoryPort, passwordEncoder, relogioNegocio);
    }

    @Bean
    public AtualizarUsuarioUseCase atualizarUsuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort,
                                                           PerfilRepositoryPort perfilRepositoryPort) {
        return new AtualizarUsuarioUseCase(usuarioRepositoryPort, perfilRepositoryPort);
    }

    @Bean
    public AlterarAtivoUsuarioUseCase alterarAtivoUsuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort) {
        return new AlterarAtivoUsuarioUseCase(usuarioRepositoryPort);
    }

    @Bean
    public AlterarSenhaUsuarioUseCase alterarSenhaUsuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort,
                                                                 PasswordEncoder passwordEncoder) {
        return new AlterarSenhaUsuarioUseCase(usuarioRepositoryPort, passwordEncoder);
    }

    @Bean
    public DeletarUsuarioUseCase deletarUsuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort) {
        return new DeletarUsuarioUseCase(usuarioRepositoryPort);
    }

    @Bean
    public ListaKpisUseCase listaKpisUseCase(PedidoRepositoryPort pedidoRepositoryPort, ProdutoRepositoryPort produtoRepositoryPort, EventoRepositoryPort eventoRepositoryPort) {
        return new ListaKpisUseCase(pedidoRepositoryPort, produtoRepositoryPort, eventoRepositoryPort);
    }

    @Bean
    ListaPedidosPorSemanaUseCase listaPedidosPorSemanaUseCase(PedidoRepositoryPort pedidoRepositoryPort) {
        return new ListaPedidosPorSemanaUseCase(pedidoRepositoryPort);
    }

    @Bean
    ListaPedidosPeriodoUseCase listaPedidosPeriodoUseCase(PedidoRepositoryPort pedidoRepositoryPort) {
        return new ListaPedidosPeriodoUseCase(pedidoRepositoryPort);
    }

    @Bean
    ListaProdutosMaisVendidosUseCase listaProdutosMaisVendidosUseCase(ProdutoRepositoryPort produtoRepositoryPort) {
        return new ListaProdutosMaisVendidosUseCase(produtoRepositoryPort);
    }

    @Bean
    ListaComparativoEventosUseCase listaComparativoEventosUseCase(EventoRepositoryPort eventoRepositoryPort) {
        return new ListaComparativoEventosUseCase(eventoRepositoryPort);
    }

    @Bean
    ListaEvolucaoEventoUseCase listaEvolucaoEventoUseCase(EventoRepositoryPort eventoRepositoryPort) {
        return new ListaEvolucaoEventoUseCase(eventoRepositoryPort);
    }

    @Bean
    ListaRelatorioDinamicoUseCase listaRelatorioDinamicoUseCase(PedidoRepositoryPort pedidoRepositoryPort) {
        return new ListaRelatorioDinamicoUseCase(pedidoRepositoryPort);
    }


}
