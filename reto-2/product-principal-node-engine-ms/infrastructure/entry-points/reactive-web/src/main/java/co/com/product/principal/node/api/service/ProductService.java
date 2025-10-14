package co.com.product.principal.node.api.service;

import co.com.product.principal.node.api.config.DbConfig;
import co.com.product.principal.node.api.request.ProductReserveRequest;
import co.com.product.principal.node.api.response.ProductReserveResponse;
import co.com.product.principal.node.model.exception.TechnicalException;
import co.com.product.principal.node.model.enums.TechnicalMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ProductService {

    private final DbConfig dbConfig;

    public Mono<ProductReserveResponse> reserveProduct(ProductReserveRequest productReserveRequest) {
        return Mono.defer(() -> {
            if (this.dbConfig.getDbStatus().equalsIgnoreCase("ENABLED")) {
                return Mono.just(new ProductReserveResponse("RESERVED_SUCCESSFULLY"));
            }
            return Mono.error(new TechnicalException("Database is disabled", TechnicalMessage.INTERNAL_ERROR));
        });
    }
}
