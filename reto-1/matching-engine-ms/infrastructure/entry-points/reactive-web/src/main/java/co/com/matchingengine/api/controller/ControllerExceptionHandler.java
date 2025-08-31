package co.com.matchingengine.api.controller;

import co.com.matchingengine.api.response.generic.GenericErrorResponse;
import co.com.matchingengine.api.response.generic.GenericResponse;
import co.com.matchingengine.model.enums.TechnicalMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Component
@Order(-2)
@Slf4j
@RequiredArgsConstructor
public class ControllerExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (ex instanceof WebExchangeBindException webExchangeBindException) {
            log.error("Invalid parameters error", kv("WebExchangeBindException", webExchangeBindException));
            return this.handlerError(exchange, webExchangeBindException.getBindingResult().getAllErrors(), HttpStatus.BAD_REQUEST,
                    TechnicalMessage.INVALID_REQUEST_DATA.getCode(),
                    TechnicalMessage.INVALID_REQUEST_DATA.getMessage());
        }
        log.error("Internal error", kv("Exception", ex));
        return Mono.error(ex);
    }

    public Mono<Void> handlerError(ServerWebExchange exchange, List<ObjectError> errors, HttpStatus httpStatus, String errorCode, String errorDescription) {
        List<String> details = new ArrayList<>();
        details.add(exchange.getRequest().getPath().value());
        if (Objects.nonNull(errors) && !errors.isEmpty()) {
            errors.forEach(error -> details.add(error.getDefaultMessage()));
        }
        GenericErrorResponse errorResponseDto = new GenericErrorResponse(errorCode, LocalDateTime.now(), errorDescription, details);
        GenericResponse<GenericErrorResponse> error = new GenericResponse<>(GenericResponse.ERROR_CODE, httpStatus.getReasonPhrase(), errorResponseDto);
        try {
            exchange.getResponse().setStatusCode(httpStatus);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            var defaultDataBuffer = new DefaultDataBufferFactory().wrap(objectMapper.writeValueAsBytes(error));
            return exchange.getResponse().writeWith(Mono.just(defaultDataBuffer));
        } catch (JsonProcessingException e) {
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return exchange.getResponse().setComplete();
        }
    }
}


