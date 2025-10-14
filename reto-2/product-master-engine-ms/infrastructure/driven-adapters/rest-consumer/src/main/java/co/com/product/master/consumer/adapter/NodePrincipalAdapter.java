package co.com.product.master.consumer.adapter;

import co.com.product.master.consumer.mapper.ProductMapper;
import co.com.product.master.consumer.response.GenericErrorResponseDto;
import co.com.product.master.consumer.response.GenericResponseDto;
import co.com.product.master.consumer.response.ProductReserveResponseDto;
import co.com.product.master.model.enums.TechnicalMessage;
import co.com.product.master.model.exception.BusinessException;
import co.com.product.master.model.exception.ProductException;
import co.com.product.master.model.exception.TechnicalException;
import co.com.product.master.model.reserve.Reserve;
import co.com.product.master.model.reserve.ReserveResult;
import co.com.product.master.model.reserve.gateways.PrincipalReserveGateway;
import io.micrometer.core.instrument.Counter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RequiredArgsConstructor
public class NodePrincipalAdapter implements PrincipalReserveGateway {

    private final WebClient clientWebClient;
    private final String defaultPath;
    private final Counter principalNodeReserveProductsCount;
    private final Counter principalNodeReserveProductsFailedCount;

    @Override
    public Mono<ReserveResult> reserveProduct(Reserve reserve) {
        return this.clientWebClient
                .post()
                .uri(this.defaultPath + "/request-reserve")
                .header("message-id", reserve.getMessageId())
                .bodyValue(ProductMapper.MAPPER.modelToRequest(reserve))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handlerError)
                .bodyToMono(new ParameterizedTypeReference<GenericResponseDto<ProductReserveResponseDto>>() {
                })
                .doOnSuccess(productReserveResponse -> {
                    log.info("PRINCIPAL :: Product Reserve Response", kv("productReserveResponse", productReserveResponse));
                    this.principalNodeReserveProductsCount.increment();
                })
                .map(response ->
                        ProductMapper.MAPPER.responseToModel(response.getResult()))
                .onErrorMap(Predicate.not(ProductException.class::isInstance),
                        exception -> new TechnicalException(exception, TechnicalMessage.INTERNAL_ERROR))
                .doOnSubscribe(subscription -> log.info("PRINCIPAL :: Product Reserve Request",
                        kv("reserveProductRequest", reserve)));
    }

    private Mono<ProductException> handlerError(ClientResponse response) {
        HttpStatusCode httpStatusCode = response.statusCode();
        if (httpStatusCode.equals(HttpStatus.UNAUTHORIZED)) {
            return Mono.error(new BusinessException(TechnicalMessage.UNAUTHORIZED_ERROR));
        }
        return response.bodyToMono(new ParameterizedTypeReference<GenericResponseDto<GenericErrorResponseDto>>() {
                })
                .flatMap(this::mapErrorResponse);
    }

    private Mono<ProductException> mapErrorResponse(GenericResponseDto<GenericErrorResponseDto> apiErrorResponse) {
        log.error("PRINCIPAL :: Client Error Response", kv("clientErrorResponse", apiErrorResponse));
        if (apiErrorResponse.getCode() == GenericResponseDto.ERROR_CODE) {
            if (apiErrorResponse.getResult().getCode().equalsIgnoreCase("PRODUCT-500")) {
                this.principalNodeReserveProductsFailedCount.increment();
                return Mono.error(new TechnicalException(apiErrorResponse.getResult().getMessage(),
                        TechnicalMessage.CLIENT_GENERIC_ERROR));
            }
        }
        return Mono.error(new TechnicalException(TechnicalMessage.INTERNAL_ERROR));
    }
}
