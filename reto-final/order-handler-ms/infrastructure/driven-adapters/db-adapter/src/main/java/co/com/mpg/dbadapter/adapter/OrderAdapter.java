package co.com.mpg.dbadapter.adapter;

import co.com.mpg.model.order.Order;
import co.com.mpg.model.order.OrderParam;
import co.com.mpg.model.order.gateways.OrderGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@Slf4j
public class OrderAdapter implements OrderGateway {

    @Override
    public Mono<Order> save(OrderParam orderParam) {
        return Mono.delay(Duration.ofMillis(100))
                .doOnSubscribe(sub -> log.info("Starting Save order with id {}", orderParam.getId()))
                .thenReturn(Order.builder()
                        .id(orderParam.getId())
                        .productCount(orderParam.getProductCount())
                        .productId(orderParam.getProductId())
                        .build())
                .doOnSuccess(orderSaved -> log.info("Order saved with id {}", orderSaved.getId()));
    }
}
