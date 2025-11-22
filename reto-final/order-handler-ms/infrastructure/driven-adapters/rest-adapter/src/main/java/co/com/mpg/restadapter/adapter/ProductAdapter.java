package co.com.mpg.restadapter.adapter;

import co.com.mpg.model.product.gateways.ProductGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@Service
public class ProductAdapter implements ProductGateway {

    @Override
    public Mono<Void> reserveProduct(String productId, int count) {
        return Mono.delay(Duration.ofMillis(100))
                .doOnSubscribe(sub -> log.info("Request :: Rest HTTP Execute Product Reservation with Id {}", productId))
                .then()
                .doOnSuccess(order -> log.info("Response :: Rest HTTP Execute Product Reservation COMPLETED Order", kv("productReserved", order)));
    }
}
