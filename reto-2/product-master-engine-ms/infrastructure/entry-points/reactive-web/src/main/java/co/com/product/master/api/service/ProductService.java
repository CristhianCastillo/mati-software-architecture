package co.com.product.master.api.service;

import co.com.product.master.api.mapper.ProductApiMapper;
import co.com.product.master.api.request.ProductReserveRequest;
import co.com.product.master.api.response.ProductReserveResponse;
import co.com.product.master.usecase.reserve.ReserveUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ProductService {

    private final ReserveUseCase reserveUseCase;

    public Mono<ProductReserveResponse> reserveProduct(ProductReserveRequest productReserveRequest, String messageId) {
        return Mono.defer(() -> {
            return this.reserveUseCase.reserveProduct(ProductApiMapper.MAPPER.requestToModel(productReserveRequest, messageId))
                    .map(reserveResponse -> ProductApiMapper.MAPPER.modelToResponse(reserveResponse));
        });
    }
}
