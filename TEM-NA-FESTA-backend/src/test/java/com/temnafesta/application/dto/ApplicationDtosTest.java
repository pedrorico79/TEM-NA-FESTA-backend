package com.temnafesta.application.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ApplicationDtosTest {

    @Test
    void deveConstruirCriarPedidoCommand() {
        CriarPedidoCommand command = new CriarPedidoCommand(1L, 2L,
                LocalDateTime.of(2026, 12, 20, 18, 0), new BigDecimal("10.00"),
                "obs", null, null,
                List.of(new CriarPedidoCommand.ItemCommand(1L, 2, new BigDecimal("50.00"), null)));

        assertEquals(1L, command.clienteId());
        assertEquals(1, command.itens().size());
        assertEquals(2, command.itens().getFirst().quantidade());
    }

    @Test
    void deveConstruirAtualizarPedidoCommand() {
        AtualizarPedidoCommand command = new AtualizarPedidoCommand(1L,
                LocalDateTime.of(2026, 12, 20, 18, 0), BigDecimal.ZERO, null, null,
                List.of(new AtualizarPedidoCommand.ItemCommand(1L, 1, new BigDecimal("20.00"), "x")));

        assertEquals(1L, command.pedidoId());
        assertEquals("x", command.itens().getFirst().observacaoItem());
    }

    @Test
    void deveConstruirCommandsDeClienteEventoLembreteUsuario() {
        CriarEndrecoCommand endereco = new CriarEndrecoCommand(
                "01234-567", "Rua A", "100", null, "Centro", "SP", "SP");
        CriarClienteCommand cliente = new CriarClienteCommand(
                "Maria", "11999999999", null, null, null, endereco);
        CriarEventoCommand evento = new CriarEventoCommand(
                "Festa", LocalDate.of(2026, 6, 10), LocalDate.of(2026, 6, 20));
        CriarLembreteCommand lembrete = new CriarLembreteCommand(
                "Comprar ovos", LocalDate.now().plusDays(1), 10L);
        CriarUsuarioCommand usuario = new CriarUsuarioCommand("Ana", "a@mail.com", "123", 1L);
        AtualizarClienteCommand atualizarCliente = new AtualizarClienteCommand(
                1L, "Maria", null, null, null, null, null);
        AlterarAtivoClienteCommand ativo = new AlterarAtivoClienteCommand(1L, false);
        AtualizarEventoCommand atualizarEvento = new AtualizarEventoCommand(
                "Natal", LocalDate.of(2026, 12, 20), LocalDate.of(2026, 12, 25));

        assertEquals("Maria", cliente.nome());
        assertEquals("01234-567", cliente.endereco().cep());
        assertEquals("Festa", evento.nome());
        assertEquals(10L, lembrete.usuarioId());
        assertEquals(1L, usuario.perfilId());
        assertEquals(1L, atualizarCliente.id());
        assertEquals("Maria", atualizarCliente.nome());
        assertFalse(ativo.ativo());
        assertEquals("Natal", atualizarEvento.nome());
    }
}
