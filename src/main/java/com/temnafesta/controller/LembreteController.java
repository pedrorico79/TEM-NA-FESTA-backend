package com.temnafesta.controller;

import com.temnafesta.model.Lembrete;
import com.temnafesta.service.ClienteService;
import com.temnafesta.service.LembreteService;
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

    @PostMapping
    public ResponseEntity<Lembrete> criar(@RequestBody Lembrete lembrete,
                                          @RequestParam Integer usuarioId) {
        Lembrete novo = lembreteService.criar(lembrete, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    // Atualizar
    @PutMapping("/{id}")
    public ResponseEntity<Lembrete> atualizar(@PathVariable Integer id,
                                              @RequestBody Lembrete lembreteAtualizado,
                                              @RequestParam Integer usuarioId) {
        Lembrete atualizado = lembreteService.atualizar(id, lembreteAtualizado, usuarioId);
        return ResponseEntity.ok(atualizado);
    }

    // Listar todos
    @GetMapping
    public ResponseEntity<List<Lembrete>> listar() {
        List<Lembrete> lembretes = lembreteService.listarTodos();
        return ResponseEntity.ok(lembretes);
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Lembrete> buscarPorId(@PathVariable Integer id) {
        Lembrete lembrete = lembreteService.buscarPorId(id);
        return ResponseEntity.ok(lembrete);
    }

    // Deletar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        lembreteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
