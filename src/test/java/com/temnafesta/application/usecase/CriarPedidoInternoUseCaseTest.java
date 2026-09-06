package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.CriarPedidoCommand;
import com.temnafesta.application.exception.RecursoNaoEncontradoException;
import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.Cliente;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.model.Produto;
import com.temnafesta.domain.ports.repository.ClienteRepositoryPort;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;
import com.temnafesta.domain.ports.repository.ProdutoRepositoryPort;
import com.temnafesta.domain.vo.StatusProducaoEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CriarPedidoInternoUseCaseTest {

    @Mock
    private PedidoRepositoryPort pedidoRepositoryPort;
    @Mock
    private ClienteRepositoryPort clienteRepositoryPort;
    @Mock
    private ProdutoRepositoryPort produtoRepositoryPort;

    @InjectMocks
    private CriarPedidoInternoUseCase useCase;

    private static Cliente clienteAtivo() {
        return new Cliente(1L, "Maria", "11999999999", null, null, null, null, null, true, false);
    }

    private static Produto produtoAtivo() {
        return new Produto(1L, "Bolo de Chocolate", null, new BigDecimal("50.00"), true, false);
    }

    private static CriarPedidoCommand command() {
        return new CriarPedidoCommand(1L, 2L, LocalDateTime.now().plusDays(7),
                new BigDecimal("10.00"), "Sem lactose", null, null,
                List.of(new CriarPedidoCommand.ItemCommand(1L, 2, new BigDecimal("50.00"), null)));
    }

    @Test
    void deveCriarPedidoEmRascunhoComTotalRecalculado() {
        when(clienteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(clienteAtivo()));
        when(produtoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(produtoAtivo()));
        when(pedidoRepositoryPort.salvar(any(Pedido.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Pedido resultado = useCase.executar(command());

        ArgumentCaptor<Pedido> captor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoRepositoryPort).salvar(captor.capture());
        Pedido salvo = captor.getValue();
        assertEquals(StatusProducaoEnum.RASCUNHO, salvo.getStatusProducao());
        assertEquals(1, salvo.getItens().size());
        assertTrue(salvo.getValorTotal().compareTo(new BigDecimal("110.00")) == 0);
        assertEquals(resultado, salvo);
    }

    @Test
    void naoDeveCriarQuandoClienteNaoEncontrado() {
        when(clienteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> useCase.executar(command()));
        verify(pedidoRepositoryPort, never()).salvar(any());
    }

    @Test
    void naoDeveCriarQuandoClienteInativo() {
        Cliente inativo = new Cliente(1L, "Maria", null, null, null, null, null, null, false, false);
        when(clienteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(inativo));

        assertThrows(RegraDeNegocioException.class, () -> useCase.executar(command()));
        verify(pedidoRepositoryPort, never()).salvar(any());
    }

    @Test
    void naoDeveCriarQuandoProdutoNaoEncontrado() {
        when(clienteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(clienteAtivo()));
        when(produtoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.empty());

        assertThrows(RegraDeNegocioException.class, () -> useCase.executar(command()));
        verify(pedidoRepositoryPort, never()).salvar(any());
    }

    @Test
    void naoDeveCriarQuandoProdutoInativo() {
        Produto inativo = new Produto(1L, "Bolo", null, new BigDecimal("50.00"), false, false);
        when(clienteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(clienteAtivo()));
        when(produtoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(inativo));

        assertThrows(RegraDeNegocioException.class, () -> useCase.executar(command()));
        verify(pedidoRepositoryPort, never()).salvar(any());
    }
}
