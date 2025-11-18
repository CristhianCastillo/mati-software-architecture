package co.com.mpg.model.product.gateways;

import reactor.core.publisher.Mono;

public interface ProductGateway {
    Mono<Void> reserveProduct(String productId, int count);
}
