package com.temnafesta.presentation.handler;

import com.temnafesta.domain.exception.NaoEncontradoException;
import com.temnafesta.application.exception.FalhaAutenticacaoException;
import com.temnafesta.application.exception.RequisicaoInvalidaException;
import com.temnafesta.application.exception.SemPermissaoException;
import com.temnafesta.application.exception.TentativasLoginExcedidasException;
import com.temnafesta.domain.exception.RegraDeNegocioException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.hibernate.PropertyValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
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

    @ExceptionHandler(RequisicaoInvalidaException.class)
    public ResponseEntity<ErroResponse> handleRequisicaoInvalidaException(RequisicaoInvalidaException ex) {
        return resposta(HttpStatus.BAD_REQUEST, "Requisição Inválida", ex.getMessage());
    }

    @ExceptionHandler(SemPermissaoException.class)
    public ResponseEntity<ErroResponse> handleSemPermissaoException(SemPermissaoException ex) {
        return resposta(HttpStatus.FORBIDDEN, "Acesso Negado", ex.getMessage());
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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> handleHttpMessageNotReadableException() {
        return resposta(
                HttpStatus.BAD_REQUEST,
                "Requisição Inválida",
                "O corpo da requisição está malformado ou contém um valor inválido."
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErroResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex) {
        return resposta(
                HttpStatus.BAD_REQUEST,
                "Parâmetro Obrigatório Ausente",
                "O parâmetro '" + ex.getParameterName() + "' é obrigatório."
        );
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErroResponse> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        return resposta(
                HttpStatus.BAD_REQUEST,
                "Cabeçalho Obrigatório Ausente",
                "O cabeçalho '" + ex.getHeaderName() + "' é obrigatório."
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        return resposta(
                HttpStatus.BAD_REQUEST,
                "Parâmetro Inválido",
                "O valor informado para o parâmetro '" + ex.getName() + "' é inválido."
        );
    }

    @ExceptionHandler({HandlerMethodValidationException.class, ConstraintViolationException.class})
    public ResponseEntity<ErroResponse> handleParameterValidationException() {
        return resposta(
                HttpStatus.BAD_REQUEST,
                "Erro de Validação nos Parâmetros",
                "Um ou mais parâmetros da requisição são inválidos."
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErroResponse> handleHttpRequestMethodNotSupportedException() {
        return resposta(
                HttpStatus.METHOD_NOT_ALLOWED,
                "Método Não Permitido",
                "O método HTTP utilizado não é permitido para esta rota."
        );
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErroResponse> handleHttpMediaTypeNotSupportedException() {
        return resposta(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                "Tipo de Conteúdo Não Suportado",
                "O tipo de conteúdo enviado não é suportado por esta rota."
        );
    }

    @ExceptionHandler({NoResourceFoundException.class, NoHandlerFoundException.class})
    public ResponseEntity<ErroResponse> handleRotaNaoEncontradaException() {
        return resposta(
                HttpStatus.NOT_FOUND,
                "Rota Não Encontrada",
                "A rota informada não existe."
        );
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

    private ResponseEntity<ErroResponse> resposta(HttpStatus status, String erro, String detalhe) {
        return ResponseEntity.status(status).body(new ErroResponse(
                status.value(),
                erro,
                List.of(detalhe)
        ));
    }

    /**
     * Fallback para qualquer outro erro não mapeado (evita quebrar o padrão de resposta).
     * Retorna HTTP 500 (Internal Server Error).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleGenericException(Exception ex) {
        log.error("Erro inesperado ao processar requisição.", ex);
        ErroResponse erroResponse = new ErroResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro Interno do Servidor",
                List.of("Ocorreu um erro inesperado. Por favor, contate o suporte.")
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erroResponse);
    }
}
