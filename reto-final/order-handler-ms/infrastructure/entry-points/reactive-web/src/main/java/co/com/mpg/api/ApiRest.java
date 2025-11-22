package co.com.mpg.api;

import co.com.mpg.api.constants.TracingConstants;
import co.com.mpg.api.manager.OrderManager;
import co.com.mpg.api.request.OrderCreateRequest;
import co.com.mpg.api.response.dto.OrderDto;
import co.com.mpg.api.response.generic.GenericResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import static net.logstash.logback.argument.StructuredArguments.kv;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class ApiRest {

    private final OrderManager orderManager;


    @PostMapping("/order")
    public Mono<ResponseEntity<GenericResponse<OrderDto>>> createOrder(@Valid @RequestBody OrderCreateRequest orderCreateRequest,
                                                                       ServerHttpRequest request) {
        return this.orderManager.createOrder(orderCreateRequest)
                .doOnSubscribe(sb -> MDC.put(TracingConstants.ORDER_ORIGIN_PAYMENT_ID, String.valueOf(orderCreateRequest.getId())))
                .doOnSubscribe(subscription -> log.info("Create Order :: Request", kv("createOrderRequest", orderCreateRequest)))
                .doOnSuccess(response -> log.info("Create Order :: Response", kv("createOrderResponse", response)))
                .doOnError(error -> log.error("Create Order :: Response Error", kv("orderError", error)))
                .map(orderCreatedResponse -> new ResponseEntity<>(GenericResponse.success(orderCreatedResponse), HttpStatus.CREATED))
                .contextWrite(Context.of(TracingConstants.ORDER_ORIGIN_PAYMENT_ID, String.valueOf(orderCreateRequest.getId())));
    }
}
