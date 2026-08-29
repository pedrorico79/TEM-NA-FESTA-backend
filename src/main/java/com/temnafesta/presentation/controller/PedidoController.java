package com.temnafesta.presentation.controller;

import com.temnafesta.application.dto.AtualizarPedidoCommand;
import com.temnafesta.application.dto.CriarPedidoCommand;
import com.temnafesta.application.usecase.*;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.infrastructure.security.user.UsuarioAutenticado;
import com.temnafesta.presentation.dto.AtualizarPedidoRequestDto;
import com.temnafesta.presentation.dto.AlterarStatusRequestDto;
import com.temnafesta.presentation.dto.CriarPedidoRequestDto;
import com.temnafesta.presentation.dto.PedidoResponseDto;
import com.temnafesta.presentation.mapper.PedidoPresentationMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    private final CriarPedidoInternoUseCase criarPedidoInternoUseCase;
    private final AlterarStatusPedidoUseCase alterarStatusPedidoUseCase;
    private final GerarReciboDigitalUseCase gerarReciboDigitalUseCase;
    private final ListarPedidoPorIdUseCase listarPedidoPorIdUseCase;
    private final AtualizarPedidoUseCase atualizarPedidoUseCase;
    private final PedidoPresentationMapper mapper;

    public PedidoController(CriarPedidoInternoUseCase criarPedidoInternoUseCase, AlterarStatusPedidoUseCase alterarStatusPedidoUseCase, GerarReciboDigitalUseCase gerarReciboDigitalUseCase, ListarPedidoPorIdUseCase listarPedidoPorIdUseCase, AtualizarPedidoUseCase atualizarPedidoUseCase, PedidoPresentationMapper mapper) {
        this.criarPedidoInternoUseCase = criarPedidoInternoUseCase;
        this.alterarStatusPedidoUseCase = alterarStatusPedidoUseCase;
        this.gerarReciboDigitalUseCase = gerarReciboDigitalUseCase;
        this.listarPedidoPorIdUseCase = listarPedidoPorIdUseCase;
        this.atualizarPedidoUseCase = atualizarPedidoUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    @Transactional
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
    @Transactional
    public ResponseEntity<PedidoResponseDto> alterarStatus(@PathVariable Long id,
                                                           @Valid @RequestBody AlterarStatusRequestDto request,
                                                           @AuthenticationPrincipal UsuarioAutenticado usuario) {
        Pedido pedidoAtualizado = alterarStatusPedidoUseCase.executar(id, request.novoStatus(), usuario.getId(), request.observacao());
        return ResponseEntity.ok(mapper.toResponse(pedidoAtualizado));
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<PedidoResponseDto> listarPorId(@PathVariable Long id) {
        Pedido pedido = listarPedidoPorIdUseCase.executar(id);

        PedidoResponseDto response = mapper.toResponse(pedido);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/recibo")
    @Transactional
    public ResponseEntity<String> gerarReciboDigital(@PathVariable Long id) {
        // Retorna o texto formatado para o WhatsApp
        String recibo = gerarReciboDigitalUseCase.executar(id);
        return ResponseEntity.ok(recibo);
    }

    @PutMapping("/{id}")
    @Transactional
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
}