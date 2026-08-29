package com.temnafesta.presentation.handler;

import com.temnafesta.domain.exception.NaoEncontradoException;
import com.temnafesta.domain.exception.RegraDeNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Captura casos onde um recurso (ID) não foi encontrado no banco de dados.
     * Retorna HTTP 404 (Not Found).
     */
    @ExceptionHandler(NaoEncontradoException.class)
    public ResponseEntity<ErroResponse> handleNaoEncontradoException(NaoEncontradoException ex) {
        ErroResponse erroResponse = new ErroResponse(
                HttpStatus.NOT_FOUND.value(),
                "Recurso Não Encontrado",
                List.of(ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erroResponse);
    }

    /**
     * Captura as violações da nossa Máquina de Estados e validações puras de Domínio.
     * Retorna HTTP 422 (Unprocessable Entity).
     */
    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ErroResponse> handleRegraDeNegocioException(RegraDeNegocioException ex) {
        ErroResponse erroResponse = new ErroResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Violação de Regra de Negócio",
                List.of(ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(erroResponse);
    }

    /**
     * Captura erros das anotações do Jakarta Validation (@NotBlank, @NotNull, etc) nos DTOs.
     * Retorna HTTP 400 (Bad Request).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> erros = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    String campo = ((FieldError) error).getField();
                    String mensagem = error.getDefaultMessage();
                    return campo + ": " + mensagem;
                })
                .collect(Collectors.toList());

        ErroResponse erroResponse = new ErroResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de Validação nos Dados Enviados",
                erros
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroResponse);
    }

    /**
     * Captura IllegalArgumentException e IllegalStateException.
     * Retorna HTTP 400 (Bad Request).
     */
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ErroResponse> handleIllegalExceptions(RuntimeException ex) {
        ErroResponse erroResponse = new ErroResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Requisição Inválida",
                List.of(ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroResponse);
    }

    /**
     * Fallback para qualquer outro erro não mapeado (evita quebrar o padrão de resposta).
     * Retorna HTTP 500 (Internal Server Error).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleGenericException(Exception ex) {
        ErroResponse erroResponse = new ErroResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro Interno do Servidor",
                List.of("Ocorreu um erro inesperado. Por favor, contate o suporte.")
        );

        // Aqui é recomendado adicionar um log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erroResponse);
    }
}