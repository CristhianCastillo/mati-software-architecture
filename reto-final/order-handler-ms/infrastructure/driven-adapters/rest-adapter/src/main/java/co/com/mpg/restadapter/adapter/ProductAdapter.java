package co.com.mpg.restadapter.adapter;

import co.com.mpg.model.product.gateways.ProductGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Service
public class ProductAdapter implements ProductGateway {

    @Override
    public Mono<Void> reserveProduct(String productId, int count) {
        return Mono.delay(Duration.ofMillis(100))
                .doOnSubscribe(sub -> log.info("Reserving product with id {}", productId))
                .then()
                .doOnSuccess(res -> log.info("Reservation completed for product with id {}", productId));
    }
}
