package com.academia.gym.controller;

import com.academia.gym.dto.ErroCampo;
import com.academia.gym.dto.ErrorResponseDTO;
import com.academia.gym.exception.BusinessException;
import com.academia.gym.exception.ConflictException;
import com.academia.gym.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String VALIDATION_ERROR_MESSAGE = "Um ou mais campos estão inválidos";

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(
            NotFoundException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseDTO.notFound(ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseDTO> handleConflict(
            ConflictException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErrorResponseDTO.conflict(ex.getMessage(), request.getRequestURI())
                );
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDTO> handleBusiness(
            BusinessException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(422).body(
                ErrorResponseDTO.unprocessableEntity(
                        ex.getMessage(),
                        request.getRequestURI()
                )
        );
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponseDTO> handleBind(
            BindException ex,
            HttpServletRequest request) {

        List<ErroCampo> erros = getErros(ex.getBindingResult());

        return ResponseEntity.badRequest().body(
                ErrorResponseDTO.badRequest(
                        VALIDATION_ERROR_MESSAGE,
                        request.getRequestURI(),
                        erros
                )
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<ErroCampo> erros = getErros(ex.getBindingResult());

        return ResponseEntity.badRequest().body(
                ErrorResponseDTO.badRequest(
                        VALIDATION_ERROR_MESSAGE,
                        request.getRequestURI(),
                        erros
                )
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        String mensagem = "Parâmetro '" + ex.getName() + "' inválido";

        return ResponseEntity.badRequest().body(
                ErrorResponseDTO.badRequest(
                        mensagem,
                        request.getRequestURI()
                )
        );
    }

    private List<ErroCampo> getErros(BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                .stream()
                .map(e -> new ErroCampo(e.getField(), e.getDefaultMessage()))
                .toList();
    }
}
