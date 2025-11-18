package co.com.mpg.model.order.gateways;

import co.com.mpg.model.order.Order;
import reactor.core.publisher.Mono;

public interface OrderCacheGateway {
    Mono<Order> getById(String orderId);

    Mono<Order> save(Order order);

    Mono<Void> delete(Order order);
}
