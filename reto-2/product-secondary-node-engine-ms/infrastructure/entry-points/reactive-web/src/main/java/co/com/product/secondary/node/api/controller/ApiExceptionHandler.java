package co.com.product.secondary.node.api.controller;

import co.com.product.secondary.node.api.response.generic.GenericErrorResponse;
import co.com.product.secondary.node.api.response.generic.GenericResponse;
import co.com.product.secondary.node.model.exception.BusinessException;
import co.com.product.secondary.node.model.exception.TechnicalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.kv;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public final ResponseEntity<GenericResponse<GenericErrorResponse>> handleAllBusinessExceptions(ServerWebExchange exchange, BusinessException ex) {
        log.error("Business error", kv("BusinessException", ex));
        List<String> details = new ArrayList<>();
        details.add(exchange.getRequest().getPath().value());
        GenericErrorResponse errorResponseDto = new GenericErrorResponse(String.valueOf(ex.getTechnicalMessage().getCode()), LocalDateTime.now(), ex.getMessage(), details);
        GenericResponse<GenericErrorResponse> error = new GenericResponse<>(GenericResponse.ERROR_CODE, HttpStatus.BAD_REQUEST.getReasonPhrase(), errorResponseDto);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TechnicalException.class)
    public final ResponseEntity<GenericResponse<GenericErrorResponse>> handleAllTechnicalExceptions(ServerWebExchange exchange, TechnicalException ex) {
        log.error("Technical error", kv("TechnicalException", ex));
        List<String> details = new ArrayList<>();
        details.add(exchange.getRequest().getPath().value());
        GenericErrorResponse errorResponseDto = new GenericErrorResponse(String.valueOf(ex.getTechnicalMessage().getCode()), LocalDateTime.now(), ex.getMessage(), details);
        GenericResponse<GenericErrorResponse> error = new GenericResponse<>(GenericResponse.ERROR_CODE, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), errorResponseDto);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}