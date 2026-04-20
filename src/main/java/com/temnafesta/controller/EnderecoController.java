package com.temnafesta.controller;

import com.temnafesta.dto.endereco.EnderecoRequestDto;
import com.temnafesta.dto.endereco.EnderecoResponseDto;
import com.temnafesta.mapper.EnderecoMapper;
import com.temnafesta.model.Endereco;
import com.temnafesta.service.EnderecoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enderecos")
@Tag(name = "Endereços", description = "Endereços vinculados aos clientes")
public class EnderecoController {

    private final EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    @Operation(summary = "Lista todos os endereços")
    @ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso")
    @GetMapping
    public ResponseEntity<List<EnderecoResponseDto>> listar(){
        List<Endereco> enderecos = enderecoService.listar();
        List<EnderecoResponseDto> responseList = EnderecoMapper.toResponseList(enderecos);
        return ResponseEntity.ok(responseList);
    }

    @Operation(summary = "Busca endereço por ID")
    @ApiResponse(responseCode = "200", description = "Endereço encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<EnderecoResponseDto> buscarPorId(@PathVariable Integer id){
        Endereco endereco = enderecoService.buscarPorId(id);
        EnderecoResponseDto response = EnderecoMapper.toResponse(endereco);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cria um novo endereço")
    @ApiResponse(responseCode = "201", description = "Endereço criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @PostMapping
    public ResponseEntity<EnderecoResponseDto> criar(@RequestBody @Valid EnderecoRequestDto requestDto){
        Endereco entity = enderecoService.criar(requestDto);
        EnderecoResponseDto response = EnderecoMapper.toResponse(entity);
        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "Atualiza um endereço existente")
    @ApiResponse(responseCode = "200", description = "Endereço atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<EnderecoResponseDto> atualizarPorId(@PathVariable Integer id, @RequestBody @Valid EnderecoRequestDto requestDto){
        Endereco entity = enderecoService.atualizar(id, requestDto);
        EnderecoResponseDto response = EnderecoMapper.toResponse(entity);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Remove um endereço")
    @ApiResponse(responseCode = "204", description = "Endereço removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable Integer id){
        enderecoService.deletar(id);
        return ResponseEntity.status(204).build();
    }
}