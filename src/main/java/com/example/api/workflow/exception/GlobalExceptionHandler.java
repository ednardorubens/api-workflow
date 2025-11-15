package com.example.api.workflow.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    @SuppressWarnings("java:S2638")
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        var errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> "%s %s".formatted(StringUtils.capitalize(fe.getField()), fe.getDefaultMessage()))
                .sorted()
                .toList();

        var problemDetail = getProblemDetail("Falha na validação de atributos", errors);
        return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
    }

    @ExceptionHandler(TransitionException.class)
    public ProblemDetail handleTransitionException(TransitionException ex) {
        return getProblemDetail("Falha na transição de status", ex.getMessage());
    }

    private ProblemDetail getProblemDetail(String title, String ... errors) {
        return getProblemDetail(title, Arrays.asList(errors));
    }

    private ProblemDetail getProblemDetail(String title, List<String> errors) {
        var problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle(title);
        problemDetail.setProperty("errors", errors);
        return problemDetail;
    }

}
