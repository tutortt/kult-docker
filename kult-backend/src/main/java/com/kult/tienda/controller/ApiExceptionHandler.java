package com.kult.tienda.controller;

import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ApiExceptionHandler {
    /** Expone el motivo del conflicto para que el cliente distinga correo de identificación. */
    @ExceptionHandler(ResponseStatusException.class)
    public ProblemDetail manejarEstadoResponse(ResponseStatusException exception) {
        return ProblemDetail.forStatusAndDetail(exception.getStatusCode(), exception.getReason());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ProblemDetail manejarArchivoGrande(MaxUploadSizeExceededException exception) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.CONTENT_TOO_LARGE, "La imagen supera el tamaño máximo permitido (5 MB)");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail manejarConflictoDeIntegridad(DataIntegrityViolationException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                "La operación viola una restricción de la base de datos (registro duplicado o relación en uso)");
    }

    /** Devuelve los mensajes de validación en el campo `detail` del ProblemDetail. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail manejarValidacion(MethodArgumentNotValidException exception) {
        String detalle = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .distinct()
                .collect(Collectors.joining("; "));

        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detalle);
    }
}
