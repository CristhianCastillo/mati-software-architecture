package co.com.matchingengine.api.controller;

import co.com.matchingengine.api.constants.TracingConstants;
import co.com.matchingengine.api.manager.MatchEngineManager;
import co.com.matchingengine.api.request.CreateOrderRequest;
import co.com.matchingengine.api.request.UpdateOrderRequest;
import co.com.matchingengine.api.response.dto.OrderBookDTO;
import co.com.matchingengine.api.response.dto.QuotaSummaryDTO;
import co.com.matchingengine.api.response.generic.GenericResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static net.logstash.logback.argument.StructuredArguments.kv;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Validated
@Slf4j
public class MatchEngineController {

    private final MatchEngineManager matchEngine;

    @PostMapping(path = "/order")
    public Mono<ResponseEntity<GenericResponse<String>>> addOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        return this.matchEngine.addOrder(createOrderRequest)
                .doOnSubscribe(sb -> MDC.put(TracingConstants.ORDER_ID, String.valueOf(createOrderRequest.getId())))
                .doOnSubscribe(subscription -> log.info("Create Order :: Request", kv("orderRequest", createOrderRequest)))
                .doOnSuccess(response -> log.info("Created Order :: Response Success", kv("orderId", createOrderRequest.getId())))
                .doOnError(error -> log.error("Create Order :: Response Error", kv("orderError", error)))
                .then(Mono.just(new ResponseEntity<>(GenericResponse.success("Order Created!"), HttpStatus.CREATED)));
    }

    @GetMapping(path = "/orders")
    public Mono<ResponseEntity<GenericResponse<OrderBookDTO>>> getOrdersBook() {
        return this.matchEngine.getOrderBook()
                .doOnSubscribe(subscription -> log.info("Get Orders Book :: Request"))
                .doOnSuccess(response -> log.info("Get Orders Book :: Response Success", kv("book", response)))
                .doOnError(error -> log.error("Get Orders :: Response Error", kv("orderBookResponseError", error)))
                .map(orderBook -> new ResponseEntity<>(GenericResponse.success(orderBook), HttpStatus.OK));
    }

    @GetMapping(path = "/quota/level1")
    public Mono<ResponseEntity<GenericResponse<QuotaSummaryDTO>>> getQuotaSummary() {
        return this.matchEngine.getBestBuyAndSell()
                .doOnSubscribe(subscription -> log.info("Get Best Buy and Sell :: Request"))
                .doOnSuccess(response -> log.info("Get Best Buy and Sell :: Response Success", kv("response", response)))
                .doOnError(error -> log.error("Get Best Buy and Sell :: Response Error", kv("responseError", error)))
                .map(quotaSummary -> new ResponseEntity<>(GenericResponse.success(quotaSummary), HttpStatus.OK));
    }

    @PutMapping(path = "/order")
    public Mono<ResponseEntity<GenericResponse<String>>> updateOrder(@Valid @RequestBody UpdateOrderRequest updateOrderRequest) {
        return this.matchEngine.updateOrder(updateOrderRequest)
                .doOnSubscribe(sb -> MDC.put(TracingConstants.ORDER_ID, String.valueOf(updateOrderRequest.getId())))
                .doOnSubscribe(subscription -> log.info("Update Order :: Request", kv("orderRequest", updateOrderRequest)))
                .doOnSuccess(response -> log.info("Updated Order :: Response Success", kv("orderId", updateOrderRequest.getId())))
                .doOnError(error -> log.error("Update Order :: Response Error", kv("orderError", error)))
                .then(Mono.just(new ResponseEntity<>(GenericResponse.success("Order Updated!"), HttpStatus.OK)));
    }

    @DeleteMapping(path = "/order/{id}")
    public Mono<ResponseEntity<GenericResponse<String>>> cancelOrder(@PathVariable("id") int id) {
        return this.matchEngine.cancelOrder(id)
                .doOnSubscribe(sb -> MDC.put(TracingConstants.ORDER_ID, String.valueOf(id)))
                .doOnSubscribe(subscription -> log.info("Cancel Order :: Request", kv("orderId", id)))
                .doOnSuccess(response -> log.info("Cancel Order :: Response Success", kv("orderId", id)))
                .doOnError(error -> log.error("Cancel Order :: Response Error", kv("orderError", error)))
                .then(Mono.just(new ResponseEntity<>(GenericResponse.success("Order Cancelled!"), HttpStatus.OK)));
    }
}
