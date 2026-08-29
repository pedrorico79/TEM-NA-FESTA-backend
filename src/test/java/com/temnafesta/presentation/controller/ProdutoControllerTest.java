package com.temnafesta.presentation.controller;

import com.temnafesta.application.dto.CriarProdutoCommand;
import com.temnafesta.application.usecase.CriarProdutoUseCase;
import com.temnafesta.application.usecase.ListarProdutosUseCase;
import com.temnafesta.domain.model.Produto;
import com.temnafesta.presentation.dto.CriarProdutoRequestDto;
import com.temnafesta.presentation.dto.ProdutoResponseDto;
import com.temnafesta.presentation.mapper.ProdutoPresentationMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoControllerTest {

    @Mock
    private ListarProdutosUseCase listarProdutosUseCase;

    @Mock
    private CriarProdutoUseCase criarProdutoUseCase;

    @Mock
    private ProdutoPresentationMapper mapper;

    @InjectMocks
    private ProdutoController produtoController;

    @Test
    void deveRetornarProdutosMapeados() {
        Produto produto = new Produto(
                1L,
                "Bolo de Chocolate",
                "Bolo com cobertura de ganache",
                new BigDecimal("49.90"),
                true,
                false);
        ProdutoResponseDto produtoResponse = new ProdutoResponseDto(
                1L,
                "Bolo de Chocolate",
                "Bolo com cobertura de ganache",
                new BigDecimal("49.90"),
                true);
        when(listarProdutosUseCase.executar("bolo")).thenReturn(List.of(produto));
        when(mapper.toResponse(produto)).thenReturn(produtoResponse);

        ResponseEntity<List<ProdutoResponseDto>> resposta = produtoController.listar("bolo");

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertNotNull(resposta.getBody());
        assertEquals(List.of(produtoResponse), resposta.getBody());
    }

    @Test
    void deveCadastrarProdutoERetornarStatusCriado() {
        CriarProdutoRequestDto request = new CriarProdutoRequestDto(
                "Bolo de Chocolate",
                "Bolo com cobertura de ganache",
                new BigDecimal("49.90"),
                null);
        CriarProdutoCommand command = new CriarProdutoCommand(
                "Bolo de Chocolate",
                "Bolo com cobertura de ganache",
                new BigDecimal("49.90"),
                null);
        Produto produtoSalvo = new Produto(
                1L,
                "Bolo de Chocolate",
                "Bolo com cobertura de ganache",
                new BigDecimal("49.90"),
                true,
                false);
        ProdutoResponseDto response = new ProdutoResponseDto(
                1L,
                "Bolo de Chocolate",
                "Bolo com cobertura de ganache",
                new BigDecimal("49.90"),
                true);
        when(mapper.toCommand(request)).thenReturn(command);
        when(criarProdutoUseCase.executar(command)).thenReturn(produtoSalvo);
        when(mapper.toResponse(produtoSalvo)).thenReturn(response);

        ResponseEntity<ProdutoResponseDto> resposta = produtoController.criar(request);

        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertEquals(response, resposta.getBody());
    }
}
