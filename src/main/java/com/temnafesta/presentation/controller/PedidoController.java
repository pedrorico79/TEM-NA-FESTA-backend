package com.temnafesta.presentation.controller;

import com.temnafesta.application.dto.AtualizarPedidoCommand;
import com.temnafesta.application.dto.CriarPedidoCommand;
import com.temnafesta.application.dto.relatorio.PedidosPorSemanaOutput;
import com.temnafesta.application.usecase.*;
import com.temnafesta.domain.model.HistoricoStatusPedido;
import com.temnafesta.domain.model.ItemPedido;
import com.temnafesta.domain.model.Pagamento;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.vo.StatusProducaoEnum;
import com.temnafesta.infrastructure.security.user.UsuarioAutenticado;
import com.temnafesta.presentation.dto.*;
import com.temnafesta.presentation.mapper.PedidoPresentationMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    private final CriarPedidoInternoUseCase criarPedidoInternoUseCase;
    private final AlterarStatusPedidoUseCase alterarStatusPedidoUseCase;
    private final GerarReciboDigitalUseCase gerarReciboDigitalUseCase;
    private final ListarPedidoPorIdUseCase listarPedidoPorIdUseCase;
    private final AtualizarPedidoUseCase atualizarPedidoUseCase;
    private final ExcluirPedidoUseCase excluirPedidoUseCase;
    private final ContarPorStatusUseCase contarPorStatusUseCase;
    private final ListarProximasRetiradasUseCase listarProximasRetiradasUseCase;
    private final ListarPedidosUseCase listarPedidosUseCase;
    private final ListarItemPedidoPorIdUseCase listarItemPedidoPorIdUseCase;
    private final ListarPagamentosPedidoUseCase listarPagamentosPedidoUseCase;
    private final ListarHistoricoStatusPedidoUseCase listarHistoricoStatusPedidoUseCase;
    private final ListaPedidosPorSemanaUseCase listaPedidosPorSemanaUseCase;
    private final PedidoPresentationMapper mapper;

    public PedidoController(CriarPedidoInternoUseCase criarPedidoInternoUseCase, AlterarStatusPedidoUseCase alterarStatusPedidoUseCase, GerarReciboDigitalUseCase gerarReciboDigitalUseCase, ListarPedidoPorIdUseCase listarPedidoPorIdUseCase, AtualizarPedidoUseCase atualizarPedidoUseCase, ExcluirPedidoUseCase excluirPedidoUseCase, ContarPorStatusUseCase contarPorStatusUseCase, ListarProximasRetiradasUseCase listarProximasRetiradasUseCase, ListarPedidosUseCase listarPedidosUseCase, ListarItemPedidoPorIdUseCase listarItemPedidoPorIdUseCase, ListarPagamentosPedidoUseCase listarPagamentosPedidoUseCase, ListarHistoricoStatusPedidoUseCase listarHistoricoStatusPedidoUseCase, ListaPedidosPorSemanaUseCase listaPedidosPorSemanaUseCase, PedidoPresentationMapper mapper) {
        this.criarPedidoInternoUseCase = criarPedidoInternoUseCase;
        this.alterarStatusPedidoUseCase = alterarStatusPedidoUseCase;
        this.gerarReciboDigitalUseCase = gerarReciboDigitalUseCase;
        this.listarPedidoPorIdUseCase = listarPedidoPorIdUseCase;
        this.atualizarPedidoUseCase = atualizarPedidoUseCase;
        this.excluirPedidoUseCase = excluirPedidoUseCase;
        this.contarPorStatusUseCase = contarPorStatusUseCase;
        this.listarProximasRetiradasUseCase = listarProximasRetiradasUseCase;
        this.listarPedidosUseCase = listarPedidosUseCase;
        this.listarItemPedidoPorIdUseCase = listarItemPedidoPorIdUseCase;
        this.listarPagamentosPedidoUseCase = listarPagamentosPedidoUseCase;
        this.listarHistoricoStatusPedidoUseCase = listarHistoricoStatusPedidoUseCase;
        this.listaPedidosPorSemanaUseCase = listaPedidosPorSemanaUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<PedidoResponseDto> criarPedido(@Valid @RequestBody CriarPedidoRequestDto request,
                                                        @AuthenticationPrincipal UsuarioAutenticado usuario) {
        // 1. Converte DTO HTTP para Command Interno
        CriarPedidoCommand command = mapper.toCommand(usuario.getId(), request);

        // 2. Executa a Regra de Negócio Pura
        Pedido pedidoSalvo = criarPedidoInternoUseCase.executar(command);

        // 3. Converte o resultado de volta para DTO HTTP
        PedidoResponseDto response = mapper.toResponse(pedidoSalvo);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoResponseDto> alterarStatus(@PathVariable Long id,
                                                           @Valid @RequestBody AlterarStatusRequestDto request,
                                                           @AuthenticationPrincipal UsuarioAutenticado usuario) {
        Pedido pedidoAtualizado = alterarStatusPedidoUseCase.executar(id, request.novoStatus(), usuario.getId(), request.observacao());
        return ResponseEntity.ok(mapper.toResponse(pedidoAtualizado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDto> listarPorId(@PathVariable Long id) {
        Pedido pedido = listarPedidoPorIdUseCase.executar(id);

        PedidoResponseDto response = mapper.toResponse(pedido);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/recibo")
    public ResponseEntity<String> gerarReciboDigital(@PathVariable Long id) {
        // Retorna o texto formatado para o WhatsApp
        String recibo = gerarReciboDigitalUseCase.executar(id);
        return ResponseEntity.ok(recibo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponseDto> atualizarPedido(@PathVariable Long id,
                                                             @Valid @RequestBody AtualizarPedidoRequestDto request) {
        // 1. Converte DTO HTTP para Command Interno
        AtualizarPedidoCommand command = mapper.toCommand(id, request);

        // 2. Executa a Regra de Negócio Pura
        Pedido pedidoAtualizado = atualizarPedidoUseCase.executar(command);

        // 3. Converte o resultado de volta para DTO HTTP
        PedidoResponseDto response = mapper.toResponse(pedidoAtualizado);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "cancela pedido (soft delete)", description = "Altera o status para CANCELADO e marca o pedido como deletado logicamente.")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        excluirPedidoUseCase.executar(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count-by-status")
    public ResponseEntity<Map<String, Long>> contarPorStatus() {
        Map<String, Long> contagens = new LinkedHashMap<>();
        contarPorStatusUseCase.executar().forEach((status, quantidade) -> contagens.put(status.name(), quantidade));
        contagens.put("PAGAMENTOS_PENDENTES", contarPorStatusUseCase.contarPagamentosPendentes());
        return ResponseEntity.ok(contagens);
    }

    @GetMapping("/proximas-retiradas")
    public ResponseEntity<List<PedidoResponseDto>> listarProximasRetiradas(
            @RequestParam int dias) {

        List<Pedido> pedidos =
                listarProximasRetiradasUseCase.executar(dias);

        List<PedidoResponseDto> response = pedidos.stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<PedidoResponseDto>> listarPedidos(
            @RequestParam(required = false) String busca,
            @RequestParam(required = false) StatusProducaoEnum status,
            @RequestParam(required = false) Long evento,
            Pageable pageable) {

        Page<Pedido> pedidos = listarPedidosUseCase.executar(
                busca,
                status,
                evento,
                pageable
        );

        Page<PedidoResponseDto> response = pedidos.map(mapper::toResponse);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{pedidoId}/itens/{itemId}")
    public ResponseEntity<ItemPedidoResponseDto> listarItemPorId(
            @PathVariable Long pedidoId,
            @PathVariable Long itemId) {

        ItemPedido item =
                listarItemPedidoPorIdUseCase.executar(pedidoId, itemId);

        return ResponseEntity.ok(mapper.toResponse(item));
    }

    @GetMapping("/{id}/pagamentos")
    public ResponseEntity<List<PagamentoResponseDto>> listarPagamentos(
            @PathVariable Long id) {

        List<Pagamento> pagamentos =
                listarPagamentosPedidoUseCase.executar(id);

        List<PagamentoResponseDto> response = pagamentos.stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/historico-status")
    @Operation(summary = "Lista o histórico de status de um pedido")
    public ResponseEntity<List<HistoricoStatusPedidoResponseDto>> listarHistoricoStatus(
            @PathVariable Long id) {

        List<HistoricoStatusPedido> historico =
                listarHistoricoStatusPedidoUseCase.executar(id);

        List<HistoricoStatusPedidoResponseDto> response = historico.stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/count-pedidos-por-semana")
    @Operation(summary = "Retorna a quantidade de pedidos por semana com base nas queryParams")
    public ResponseEntity<List<PedidosPorSemanaResponseDto>> countPedidosPorSemana(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate de,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ate) {

        List<PedidosPorSemanaOutput> outputs = listaPedidosPorSemanaUseCase.execute(de, ate);
        List<PedidosPorSemanaResponseDto> response = mapper.toPedidosPorSemanaResponse(outputs);

        return ResponseEntity.ok(response);
    }
}
