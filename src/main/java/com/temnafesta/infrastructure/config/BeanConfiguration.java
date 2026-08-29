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
    public ListarMetodosPagamentoUseCase listarMetodosPagamentoUseCase(MetodoPagamentoRepositoryPort metodoPagamentoRepositoryPort) {
        return new ListarMetodosPagamentoUseCase(metodoPagamentoRepositoryPort);
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