package co.com.mpg.model.order.gateways;

import co.com.mpg.model.order.Order;
import co.com.mpg.model.order.OrderParam;
import reactor.core.publisher.Mono;

public interface OrderGateway {
    Mono<Order> save(OrderParam orderParam);
}
