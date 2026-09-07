package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.CriarPedidoCommand;
import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.Cliente;
import com.temnafesta.domain.model.ItemPedido;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.model.Produto;
import com.temnafesta.domain.ports.repository.ClienteRepositoryPort;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;
import com.temnafesta.domain.ports.repository.ProdutoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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
    private CriarPedidoInternoUseCase criarPedidoInternoUseCase;

    private CriarPedidoCommand commandValido;
    private Cliente clienteValido;
    private Produto produtoValido;

    @BeforeEach
    void setUp() {
        clienteValido = new Cliente(
                1L, "João Silva", "11999999999", "11999999999", "@joao",
                LocalDateTime.now().toLocalDate(), "Cliente VIP", 1L, true, false
        );

        produtoValido = new Produto(
                1L, "Bolo de Chocolate", "Bolo caseiro de chocolate", new BigDecimal("50.00"),
                true, false
        );

        CriarPedidoCommand.ItemCommand itemCommand = new CriarPedidoCommand.ItemCommand(
                1L, 2, new BigDecimal("50.00"), "Sem glúten"
        );

        commandValido = new CriarPedidoCommand(
                1L, // clienteId
                1L, // usuarioId
                LocalDateTime.now().plusDays(2), // dataEntrega
                new BigDecimal("10.00"), // taxaEntrega
                "Pedido para aniversário", // observacao
                1L, // eventoId
                1L, // enderecoEntregaId
                List.of(itemCommand)
        );
    }

    @Nested
    @DisplayName("Cenários de sucesso")
    class CenariosDeSucesso {

        @Test
        @DisplayName("Deve criar pedido interno com sucesso quando todos os dados são válidos")
        void deveCriarPedidoInternoComSucesso() {
            when(clienteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(clienteValido));
            when(produtoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(produtoValido));
            when(pedidoRepositoryPort.salvar(any(Pedido.class))).thenAnswer(invocation -> {
                Pedido pedido = invocation.getArgument(0);
                return new Pedido(
                        1L, pedido.getDataPedido(), pedido.getDataEntrega(), pedido.getTaxaEntrega(),
                        pedido.getObservacao(), pedido.getClienteId(), pedido.getUsuarioId(),
                        pedido.getEventoId(), pedido.getEnderecoEntregaId(),
                        pedido.getItens(), pedido.getPagamentos()
                );
            });

            Pedido resultado = criarPedidoInternoUseCase.executar(commandValido);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(1L);
            assertThat(resultado.getClienteId()).isEqualTo(1L);
            assertThat(resultado.getUsuarioId()).isEqualTo(1L);
            assertThat(resultado.getDataEntrega()).isEqualTo(commandValido.dataEntrega());
            assertThat(resultado.getTaxaEntrega()).isEqualByComparingTo(commandValido.taxaEntrega());
            assertThat(resultado.getObservacao()).isEqualTo(commandValido.observacao());
            assertThat(resultado.getEventoId()).isEqualTo(commandValido.eventoId());
            assertThat(resultado.getEnderecoEntregaId()).isEqualTo(commandValido.enderecoEntregaId());
            assertThat(resultado.getStatusProducao().name()).isEqualTo("RASCUNHO");
            assertThat(resultado.getItens()).hasSize(1);

            ItemPedido itemSalvo = resultado.getItens().getFirst();
            assertThat(itemSalvo.getProdutoId()).isEqualTo(1L);
            assertThat(itemSalvo.getQuantidade()).isEqualTo(2);
            assertThat(itemSalvo.getPrecoUnitario()).isEqualByComparingTo(new BigDecimal("50.00"));
            assertThat(itemSalvo.getObservacaoItem()).isEqualTo("Sem glúten");

            verify(clienteRepositoryPort).buscarPorId(1L);
            verify(produtoRepositoryPort).buscarPorId(1L);
            verify(pedidoRepositoryPort).salvar(any(Pedido.class));
        }

        @Test
        @DisplayName("Deve criar pedido com múltiplos itens")
        void deveCriarPedidoComMultiplosItens() {
            Produto produto2 = new Produto(
                    2L, "Torta de Limão", "Torta gelada de limão", new BigDecimal("40.00"),
                    true, false
            );

            CriarPedidoCommand.ItemCommand item1 = new CriarPedidoCommand.ItemCommand(
                    1L, 1, new BigDecimal("50.00"), ""
            );
            CriarPedidoCommand.ItemCommand item2 = new CriarPedidoCommand.ItemCommand(
                    2L, 3, new BigDecimal("40.00"), "Com cobertura extra"
            );

            CriarPedidoCommand commandMultiplosItens = new CriarPedidoCommand(
                    1L, 1L, LocalDateTime.now().plusDays(1),
                    new BigDecimal("5.00"), "Pedido com 2 itens", null, 1L,
                    List.of(item1, item2)
            );

            when(clienteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(clienteValido));
            when(produtoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(produtoValido));
            when(produtoRepositoryPort.buscarPorId(2L)).thenReturn(Optional.of(produto2));
            when(pedidoRepositoryPort.salvar(any(Pedido.class))).thenAnswer(invocation -> {
                Pedido pedido = invocation.getArgument(0);
                return new Pedido(
                        1L, pedido.getDataPedido(), pedido.getDataEntrega(), pedido.getTaxaEntrega(),
                        pedido.getObservacao(), pedido.getClienteId(), pedido.getUsuarioId(),
                        pedido.getEventoId(), pedido.getEnderecoEntregaId(),
                        pedido.getItens(), pedido.getPagamentos()
                );
            });

            Pedido resultado = criarPedidoInternoUseCase.executar(commandMultiplosItens);

            assertThat(resultado.getItens()).hasSize(2);
            assertThat(resultado.getValorTotal()).isEqualByComparingTo(new BigDecimal("175.00")); // 1*50 + 3*40 + 5 taxa

            verify(produtoRepositoryPort).buscarPorId(1L);
            verify(produtoRepositoryPort).buscarPorId(2L);
        }

        @Test
        @DisplayName("Deve criar pedido sem evento sazonal (eventoId null)")
        void deveCriarPedidoSemEventoSazonal() {
            CriarPedidoCommand commandSemEvento = new CriarPedidoCommand(
                    1L, 1L, LocalDateTime.now().plusDays(1),
                    BigDecimal.ZERO, "Retirada na loja", null, 1L,
                    List.of(new CriarPedidoCommand.ItemCommand(1L, 1, new BigDecimal("30.00"), ""))
            );

            when(clienteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(clienteValido));
            when(produtoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(produtoValido));
            when(pedidoRepositoryPort.salvar(any(Pedido.class))).thenAnswer(invocation -> {
                Pedido pedido = invocation.getArgument(0);
                return new Pedido(
                        1L, pedido.getDataPedido(), pedido.getDataEntrega(), pedido.getTaxaEntrega(),
                        pedido.getObservacao(), pedido.getClienteId(), pedido.getUsuarioId(),
                        pedido.getEventoId(), pedido.getEnderecoEntregaId(),
                        pedido.getItens(), pedido.getPagamentos()
                );
            });

            Pedido resultado = criarPedidoInternoUseCase.executar(commandSemEvento);

            assertThat(resultado.getEventoId()).isNull();
            assertThat(resultado.getTaxaEntrega()).isEqualByComparingTo(BigDecimal.ZERO);
        }

        @Test
        @DisplayName("Deve criar pedido com taxa de entrega zero (retirada na loja)")
        void deveCriarPedidoComTaxaEntregaZero() {
            CriarPedidoCommand commandRetirada = new CriarPedidoCommand(
                    1L, 1L, LocalDateTime.now().plusDays(1),
                    BigDecimal.ZERO, "Retirada na confeitaria", 1L, null,
                    List.of(new CriarPedidoCommand.ItemCommand(1L, 1, new BigDecimal("25.00"), ""))
            );

            when(clienteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(clienteValido));
            when(produtoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(produtoValido));
            when(pedidoRepositoryPort.salvar(any(Pedido.class))).thenAnswer(invocation -> {
                Pedido pedido = invocation.getArgument(0);
                return new Pedido(
                        1L, pedido.getDataPedido(), pedido.getDataEntrega(), pedido.getTaxaEntrega(),
                        pedido.getObservacao(), pedido.getClienteId(), pedido.getUsuarioId(),
                        pedido.getEventoId(), pedido.getEnderecoEntregaId(),
                        pedido.getItens(), pedido.getPagamentos()
                );
            });

            Pedido resultado = criarPedidoInternoUseCase.executar(commandRetirada);

            assertThat(resultado.getTaxaEntrega()).isEqualByComparingTo(BigDecimal.ZERO);
            assertThat(resultado.getEnderecoEntregaId()).isNull();
        }
    }

    @Nested
    @DisplayName("Cenários de erro - Cliente")
    class CenariosDeErroCliente {

        @Test
        @DisplayName("Deve lançar exceção quando cliente não existe")
        void deveLancarExcecaoQuandoClienteNaoExiste() {
            when(clienteRepositoryPort.buscarPorId(999L)).thenReturn(Optional.empty());

            CriarPedidoCommand commandClienteInexistente = new CriarPedidoCommand(
                    999L, 1L, LocalDateTime.now().plusDays(1),
                    new BigDecimal("10.00"), "", 1L, 1L,
                    List.of(new CriarPedidoCommand.ItemCommand(1L, 1, new BigDecimal("30.00"), ""))
            );

            assertThatThrownBy(() -> criarPedidoInternoUseCase.executar(commandClienteInexistente))
                    .isInstanceOf(RegraDeNegocioException.class)
                    .hasMessageContaining("Cliente não encontrado com o ID: 999");
        }
    }

    @Nested
    @DisplayName("Cenários de erro - Produto")
    class CenariosDeErroProduto {

        @Test
        @DisplayName("Deve lançar exceção quando produto não existe")
        void deveLancarExcecaoQuandoProdutoNaoExiste() {
            when(clienteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(clienteValido));
            when(produtoRepositoryPort.buscarPorId(999L)).thenReturn(Optional.empty());

            CriarPedidoCommand commandProdutoInexistente = new CriarPedidoCommand(
                    1L, 1L, LocalDateTime.now().plusDays(1),
                    new BigDecimal("10.00"), "", 1L, 1L,
                    List.of(new CriarPedidoCommand.ItemCommand(999L, 1, new BigDecimal("30.00"), ""))
            );

            assertThatThrownBy(() -> criarPedidoInternoUseCase.executar(commandProdutoInexistente))
                    .isInstanceOf(RegraDeNegocioException.class)
                    .hasMessageContaining("Produto não encontrado com o ID: 999");
        }

        @Test
        @DisplayName("Deve lançar exceção quando um dos múltiplos produtos não existe")
        void deveLancarExcecaoQuandoUmDosMultiplosProdutosNaoExiste() {
            Produto produtoExistente = new Produto(
                    1L, "Bolo", "Desc", new BigDecimal("50.00"), true, false
            );

            when(clienteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(clienteValido));
            when(produtoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(produtoExistente));
            when(produtoRepositoryPort.buscarPorId(999L)).thenReturn(Optional.empty());

            CriarPedidoCommand commandMultiplosProdutos = new CriarPedidoCommand(
                    1L, 1L, LocalDateTime.now().plusDays(1),
                    new BigDecimal("10.00"), "", 1L, 1L,
                    List.of(
                            new CriarPedidoCommand.ItemCommand(1L, 1, new BigDecimal("50.00"), ""),
                            new CriarPedidoCommand.ItemCommand(999L, 1, new BigDecimal("30.00"), "")
                    )
            );

            assertThatThrownBy(() -> criarPedidoInternoUseCase.executar(commandMultiplosProdutos))
                    .isInstanceOf(RegraDeNegocioException.class)
                    .hasMessageContaining("Produto não encontrado com o ID: 999");
        }
    }

    @Nested
    @DisplayName("Cenários de validação de dados")
    class CenariosDeValidacaoDados {

        @Test
        @DisplayName("Deve calcular valor total corretamente incluindo taxa de entrega")
        void deveCalcularValorTotalCorretamente() {
            when(clienteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(clienteValido));
            when(produtoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(produtoValido));
            when(pedidoRepositoryPort.salvar(any(Pedido.class))).thenAnswer(invocation -> {
                Pedido pedido = invocation.getArgument(0);
                return new Pedido(
                        1L, pedido.getDataPedido(), pedido.getDataEntrega(), pedido.getTaxaEntrega(),
                        pedido.getObservacao(), pedido.getClienteId(), pedido.getUsuarioId(),
                        pedido.getEventoId(), pedido.getEnderecoEntregaId(),
                        pedido.getItens(), pedido.getPagamentos()
                );
            });

            // 2 itens * R$ 50,00 = R$ 100,00 + R$ 10,00 taxa = R$ 110,00
            Pedido resultado = criarPedidoInternoUseCase.executar(commandValido);

            assertThat(resultado.getValorTotal()).isEqualByComparingTo(new BigDecimal("110.00"));
        }

        @Test
        @DisplayName("Deve definir status inicial como RASCUNHO")
        void deveDefinirStatusInicialComoRascunho() {
            when(clienteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(clienteValido));
            when(produtoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(produtoValido));
            when(pedidoRepositoryPort.salvar(any(Pedido.class))).thenAnswer(invocation -> {
                Pedido pedido = invocation.getArgument(0);
                return new Pedido(
                        1L, pedido.getDataPedido(), pedido.getDataEntrega(), pedido.getTaxaEntrega(),
                        pedido.getObservacao(), pedido.getClienteId(), pedido.getUsuarioId(),
                        pedido.getEventoId(), pedido.getEnderecoEntregaId(),
                        pedido.getItens(), pedido.getPagamentos()
                );
            });

            Pedido resultado = criarPedidoInternoUseCase.executar(commandValido);

            assertThat(resultado.getStatusProducao().name()).isEqualTo("RASCUNHO");
        }

        @Test
        @DisplayName("Deve definir dataPedido como momento atual quando null")
        void deveDefinirDataPedidoComoMomentoAtual() {
            when(clienteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(clienteValido));
            when(produtoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(produtoValido));
            when(pedidoRepositoryPort.salvar(any(Pedido.class))).thenAnswer(invocation -> {
                Pedido pedido = invocation.getArgument(0);
                return new Pedido(
                        1L, pedido.getDataPedido(), pedido.getDataEntrega(), pedido.getTaxaEntrega(),
                        pedido.getObservacao(), pedido.getClienteId(), pedido.getUsuarioId(),
                        pedido.getEventoId(), pedido.getEnderecoEntregaId(),
                        pedido.getItens(), pedido.getPagamentos()
                );
            });

            Pedido resultado = criarPedidoInternoUseCase.executar(commandValido);

            assertThat(resultado.getDataPedido()).isNotNull();
            assertThat(resultado.getDataPedido()).isBeforeOrEqualTo(LocalDateTime.now().plusSeconds(1));
        }
    }
}