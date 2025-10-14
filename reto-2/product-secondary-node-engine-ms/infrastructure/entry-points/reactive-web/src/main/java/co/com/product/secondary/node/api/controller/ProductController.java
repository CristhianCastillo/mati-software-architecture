package co.com.product.secondary.node.api.controller;

import co.com.product.secondary.node.api.constants.TracingConstants;
import co.com.product.secondary.node.api.request.ProductReserveRequest;
import co.com.product.secondary.node.api.response.ProductReserveResponse;
import co.com.product.secondary.node.api.response.generic.GenericResponse;
import co.com.product.secondary.node.api.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import static net.logstash.logback.argument.StructuredArguments.kv;

@RestController
@RequestMapping(value = "/api/v1/product", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Validated
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PostMapping("/request-reserve")
    public Mono<ResponseEntity<GenericResponse<ProductReserveResponse>>> reserveProduct(@Valid @RequestBody ProductReserveRequest productReserveRequest,
                                                                                        ServerHttpRequest request) {
        return this.productService.reserveProduct(productReserveRequest)
                .doOnSubscribe(sb -> MDC.put(TracingConstants.PRODUCT_ID, String.valueOf(productReserveRequest.getId())))
                .doOnSubscribe(subscription -> log.info("Reserve Product :: Request", kv("productReserveRequest", productReserveRequest)))
                .doOnSuccess(response -> log.info("Reserve Product :: Response", kv("productReserveResponse", response)))
                .doOnError(error -> log.error("Reserve Product :: Response Error", kv("productError", error)))
                .map(productReserveResponse -> new ResponseEntity<>(GenericResponse.success(productReserveResponse), HttpStatus.OK))
                .contextWrite(Context.of(TracingConstants.PRODUCT_ID, String.valueOf(productReserveRequest.getId())));
    }
}
