package co.com.product.principal.node.api.controller;

import co.com.product.principal.node.api.constants.TracingConstants;
import co.com.product.principal.node.api.request.PropertyRequest;
import co.com.product.principal.node.api.response.dto.PropertyDto;
import co.com.product.principal.node.api.response.generic.GenericResponse;
import co.com.product.principal.node.api.service.PropertyService;
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
@RequestMapping(value = "/api/v1/properties", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Validated
@Slf4j
public class PropertiesController {

    private final PropertyService propertyService;

    @PostMapping()
    public Mono<ResponseEntity<GenericResponse<PropertyDto>>> updateProperty(@Valid @RequestBody PropertyRequest propertyRequest,
                                                                             ServerHttpRequest request) {
        return Mono.defer(() -> {
                    this.propertyService.updateProperty(propertyRequest.getKey(), propertyRequest.getValue());
                    return Mono.just(new PropertyDto("Property updated successfully"));
                }).doOnSubscribe(sb -> MDC.put(TracingConstants.PROPERTY_CONFIG_KEY, String.valueOf(propertyRequest.getKey())))
                .doOnSubscribe(subscription -> log.info("Update Property :: Request", kv("propertyRequest", propertyRequest)))
                .doOnSuccess(response -> log.info("Update Property :: Response", kv("propertyResponse", response)))
                .doOnError(error -> log.error("Update Property :: Response Error", kv("propertyError", error)))
                .map(propertyDto -> new ResponseEntity<>(GenericResponse.success(propertyDto), HttpStatus.OK))
                .contextWrite(Context.of(TracingConstants.PROPERTY_CONFIG_KEY, String.valueOf(propertyRequest.getKey())));
    }
}
