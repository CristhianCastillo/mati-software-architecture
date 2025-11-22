package co.com.mpg.model.order.gateways;

import co.com.mpg.model.order.Order;
import co.com.mpg.model.payment.Payment;
import reactor.core.publisher.Mono;

public interface OrderEventGateway {
    Mono<String> createOrder(Order order, Payment payment);
}
