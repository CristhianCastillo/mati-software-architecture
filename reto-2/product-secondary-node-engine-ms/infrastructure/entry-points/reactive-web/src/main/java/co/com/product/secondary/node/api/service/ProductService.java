package co.com.product.secondary.node.api.service;

import co.com.product.secondary.node.api.request.ProductReserveRequest;
import co.com.product.secondary.node.api.response.ProductReserveResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ProductService {

    public Mono<ProductReserveResponse> reserveProduct(ProductReserveRequest productReserveRequest) {
        return Mono.defer(() -> {
            return Mono.just(new ProductReserveResponse("RESERVED_SUCCESSFULLY"));
        });
    }
}
