package com.academia.gym.controller;

import com.academia.gym.dto.ErroCampo;
import com.academia.gym.dto.ErrorResponseDTO;
import com.academia.gym.exception.BusinessException;
import com.academia.gym.exception.ConflictException;
import com.academia.gym.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.academia.gym.dto.ErroCampo.erro;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String VALIDATION_ERROR_MESSAGE = "Um ou mais campos estão inválidos";

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(
            NotFoundException ex,
            HttpServletRequest request) {

        return buildResponse(HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseDTO> handleConflict(
            ConflictException ex,
            HttpServletRequest request) {

        return buildResponse(HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getRequestURI());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDTO> handleBusiness(
            BusinessException ex,
            HttpServletRequest request) {

        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY,
                ex.getMessage(),
                request.getRequestURI(),
                ex.getErros());
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponseDTO> handleBind(
            BindException ex,
            HttpServletRequest request) {

        return buildValidationError(ex.getBindingResult(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        return buildValidationError(ex.getBindingResult(), request);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodNotAllowed(
            HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request) {

        String mensagem = "Método " + ex.getMethod() + " não é permitido para este endpoint";

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ErrorResponseDTO.of(
                        HttpStatus.METHOD_NOT_ALLOWED.value(),
                        mensagem,
                        request.getRequestURI(),
                        List.of(new ErroCampo("method", mensagem, "METHOD_NOT_ALLOWED"))
                ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrity(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        String mensagem = ex.getMostSpecificCause().getMessage();
        String lower = mensagem != null ? mensagem.toLowerCase() : "";

        List<ErroCampo> erros;

        if (lower.contains("email")) {
            erros = List.of(erro("email", "Email já está em uso", "EMAIL_DUPLICADO"));
        } else if (lower.contains("cpf")) {
            erros = List.of(erro("cpf", "CPF já está em uso", "CPF_DUPLICADO"));
        } else {
            erros = List.of(erro("database", "Violação de integridade", "DATA_INTEGRITY"));
        }

        return buildResponse(HttpStatus.CONFLICT,
                "Violação de integridade",
                request.getRequestURI(),
                erros);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleJsonError(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        List<ErroCampo> erros = new ArrayList<>();

        Throwable cause = ex.getCause();

        if (cause instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException ife) {

            String campo = ife.getPath().isEmpty()
                    ? "request"
                    : ife.getPath().get(ife.getPath().size() - 1).getFieldName();

            erros.add(erro(campo, "Valor inválido para o campo '" + campo + "'", "INVALID_FORMAT"));

        } else {
            erros.add(erro("request", "JSON inválido", "JSON_INVALIDO"));
        }

        return buildResponse(HttpStatus.BAD_REQUEST,
                VALIDATION_ERROR_MESSAGE,
                request.getRequestURI(),
                erros);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        String mensagem = "Parâmetro '" + ex.getName() + "' inválido";

        return buildResponse(HttpStatus.BAD_REQUEST,
                mensagem,
                request.getRequestURI(),
                List.of(erro(ex.getName(), mensagem, "TYPE_MISMATCH")));
    }

    private ResponseEntity<ErrorResponseDTO> buildResponse(
            HttpStatus status,
            String message,
            String path) {

        return ResponseEntity.status(status)
                .body(ErrorResponseDTO.of(status.value(), message, path));
    }

    private ResponseEntity<ErrorResponseDTO> buildResponse(
            HttpStatus status,
            String message,
            String path,
            List<ErroCampo> erros) {

        return ResponseEntity.status(status)
                .body(ErrorResponseDTO.of(status.value(), message, path, erros));
    }

    private ResponseEntity<ErrorResponseDTO> buildValidationError(
            BindingResult bindingResult,
            HttpServletRequest request) {

        List<ErroCampo> erros = getErros(bindingResult);

        return buildResponse(HttpStatus.BAD_REQUEST,
                VALIDATION_ERROR_MESSAGE,
                request.getRequestURI(),
                erros);
    }



    private List<ErroCampo> getErros(BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        e -> new ErroCampo(e.getField(), e.getDefaultMessage(), "VALIDATION_ERROR"),
                        (e1, e2) -> e1
                ))
                .values()
                .stream()
                .toList();
    }
}
