package com.temnafesta.presentation.handler;

import com.temnafesta.domain.exception.NaoEncontradoException;
import com.temnafesta.application.exception.RecursoNaoEncontradoException;
import com.temnafesta.application.exception.FalhaAutenticacaoException;
import com.temnafesta.application.exception.TentativasLoginExcedidasException;
import com.temnafesta.domain.exception.RegraDeNegocioException;
import org.springframework.dao.DataIntegrityViolationException;
import org.hibernate.PropertyValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.sql.SQLException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(FalhaAutenticacaoException.class)
    public ResponseEntity<ErroResponse> handleFalhaAutenticacao(FalhaAutenticacaoException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErroResponse(
                HttpStatus.UNAUTHORIZED.value(), "Não autenticado", List.of(ex.getMessage())));
    }

    @ExceptionHandler(TentativasLoginExcedidasException.class)
    public ResponseEntity<ErroResponse> handleTentativasLoginExcedidas(TentativasLoginExcedidasException ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .header(HttpHeaders.RETRY_AFTER, Long.toString(ex.getSegundosParaTentarNovamente()))
                .body(new ErroResponse(HttpStatus.TOO_MANY_REQUESTS.value(), "Muitas tentativas", List.of(ex.getMessage())));
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> handleRecursoNaoEncontradoException(
            RecursoNaoEncontradoException ex) {
        ErroResponse erroResponse = new ErroResponse(
                HttpStatus.NOT_FOUND.value(),
                "Recurso Não Encontrado",
                List.of(ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erroResponse);
    }

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
                    if (error instanceof FieldError fieldError) {
                        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
                    }

                    return error.getDefaultMessage();
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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        if (encontrarCausa(ex, PropertyValueException.class) != null) {
            String detalhe = "O pedido está com dados de controle desatualizados. Atualize a página e tente novamente.";
            log.error("Falha ao persistir entidade com valor obrigatório ausente.", ex);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErroResponse(
                    HttpStatus.CONFLICT.value(), "Não foi possível atualizar o pedido", List.of(detalhe)));
        }

        SQLException sqlException = encontrarSqlException(ex);
        String sqlState = sqlException == null ? null : sqlException.getSQLState();
        int codigoFornecedor = sqlException == null ? 0 : sqlException.getErrorCode();
        String detalhe = detalheIntegridade(sqlState, codigoFornecedor);

        log.error("Operação rejeitada por uma restrição de integridade. sqlState={}, vendorCode={}",
                sqlState, codigoFornecedor, ex);

        ErroResponse erroResponse = new ErroResponse(
                HttpStatus.CONFLICT.value(),
                "Conflito de dados",
                List.of(detalhe)
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erroResponse);
    }

    private <T extends Throwable> T encontrarCausa(Throwable erro, Class<T> tipo) {
        Throwable atual = erro;
        while (atual != null) {
            if (tipo.isInstance(atual)) {
                return tipo.cast(atual);
            }
            atual = atual.getCause();
        }
        return null;
    }

    private SQLException encontrarSqlException(Throwable erro) {
        Throwable atual = erro;
        while (atual != null) {
            if (atual instanceof SQLException sqlException) {
                SQLException encadeada = sqlException;
                while (encadeada != null) {
                    if (sqlStateConhecido(encadeada.getSQLState()) || codigoBancoConhecido(encadeada.getErrorCode())) {
                        return encadeada;
                    }
                    encadeada = encadeada.getNextException();
                }
                return sqlException;
            }
            atual = atual.getCause();
        }
        return null;
    }

    private boolean sqlStateConhecido(String sqlState) {
        return "23505".equals(sqlState) || "23503".equals(sqlState)
                || "23502".equals(sqlState) || "23514".equals(sqlState);
    }

    private boolean codigoBancoConhecido(int codigoFornecedor) {
        return codigoFornecedor == 1062 || codigoFornecedor == 1451 || codigoFornecedor == 1452
                || codigoFornecedor == 1048 || codigoFornecedor == 3819 || codigoFornecedor == 4025;
    }

    private String detalheIntegridade(String sqlState, int codigoFornecedor) {
        if ("23505".equals(sqlState) || codigoFornecedor == 1062) {
            return "Já existe um registro com esses dados. Revise as informações e tente novamente.";
        }
        if ("23503".equals(sqlState) || codigoFornecedor == 1451 || codigoFornecedor == 1452) {
            return "Um dado relacionado não existe mais ou não pode ser alterado. Atualize a tela e tente novamente.";
        }
        if ("23502".equals(sqlState) || "23514".equals(sqlState)
                || codigoFornecedor == 1048 || codigoFornecedor == 3819 || codigoFornecedor == 4025) {
            return "Algum dado obrigatório ou regra de validação impediu a operação. Revise as informações e tente novamente.";
        }
        if (sqlState != null && sqlState.startsWith("23")) {
            return "O banco recusou a operação por uma restrição de integridade dos dados. Atualize as informações e tente novamente.";
        }
        return "O banco recusou a operação por um conflito de integridade que não pôde ser identificado. Atualize as informações e tente novamente. Se persistir, contate o suporte.";
    }

    /**
     * Fallback para qualquer outro erro não mapeado (evita quebrar o padrão de resposta).
     * Retorna HTTP 500 (Internal Server Error).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleGenericException(Exception ex) {
        ex.printStackTrace();
        ErroResponse erroResponse = new ErroResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro Interno do Servidor",
                List.of("Ocorreu um erro inesperado. Por favor, contate o suporte.")
        );

        // Aqui é recomendado adicionar um log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erroResponse);
    }
}
