package com.temnafesta.controller;

import com.temnafesta.model.Lembrete;
import com.temnafesta.service.ClienteService;
import com.temnafesta.service.LembreteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/lembretes")
public class LembreteController {

    private final LembreteService lembreteService;

    public LembreteController(LembreteService lembreteService) {
        this.lembreteService = lembreteService;
    }

    @Operation(summary = "Cria um novo lembrete")
    @ApiResponse(responseCode = "201", description = "Lembrete criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @PostMapping
    public ResponseEntity<Lembrete> criar(@RequestBody Lembrete lembrete,
                                          @RequestParam Integer usuarioId) {
        Lembrete novo = lembreteService.criar(lembrete, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    // Atualizar
    @Operation(summary = "Atualiza um lembrete existente")
    @ApiResponse(responseCode = "200", description = "Lembrete atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Lembrete não encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<Lembrete> atualizar(@PathVariable Integer id,
                                              @RequestBody Lembrete lembreteAtualizado,
                                              @RequestParam Integer usuarioId) {
        Lembrete atualizado = lembreteService.atualizar(id, lembreteAtualizado, usuarioId);
        return ResponseEntity.ok(atualizado);
    }

    // Listar todos
    @Operation(summary = "Lista todos os lembretes")
    @ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso")
    @GetMapping
    public ResponseEntity<List<Lembrete>> listar() {
        List<Lembrete> lembretes = lembreteService.listarTodos();
        return ResponseEntity.ok(lembretes);
    }

    // Buscar por ID
    @Operation(summary = "Busca lembrete por ID")
    @ApiResponse(responseCode = "200", description = "Lembrete encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Lembrete não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<Lembrete> buscarPorId(@PathVariable Integer id) {
        Lembrete lembrete = lembreteService.buscarPorId(id);
        return ResponseEntity.ok(lembrete);
    }

    // Deletar
    @Operation(summary = "Remove um lembrete")
    @ApiResponse(responseCode = "204", description = "Lembrete removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Lembrete não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        lembreteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
