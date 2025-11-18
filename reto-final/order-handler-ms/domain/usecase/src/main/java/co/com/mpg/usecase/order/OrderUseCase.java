package co.com.mpg.usecase.order;

import co.com.mpg.model.enums.TechnicalMessage;
import co.com.mpg.model.exception.BusinessException;
import co.com.mpg.model.order.Order;
import co.com.mpg.model.order.OrderParam;
import co.com.mpg.model.order.gateways.OrderCacheGateway;
import co.com.mpg.model.order.gateways.OrderEventGateway;
import co.com.mpg.model.order.gateways.OrderGateway;
import co.com.mpg.model.payment.gateways.PaymentGateway;
import co.com.mpg.model.product.gateways.ProductGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class OrderUseCase {

    private final OrderCacheGateway orderCacheGateway;
    private final OrderGateway orderGateway;
    private final OrderEventGateway orderEventGateway;
    private final ProductGateway productGateway;
    private final PaymentGateway paymentGateway;

    public Mono<Order> createOrder(OrderParam orderParam) {
        return this.orderCacheGateway.getById(orderParam.getId())
                .switchIfEmpty(this.processOrder(orderParam))
                .flatMap(order -> Mono.<Order>error(new BusinessException(String.format("Order with Id: %s already exists", orderParam.getId()), TechnicalMessage.ORDER_ALREADY_EXISTS)));
    }

    private Mono<Order> processOrder(OrderParam orderParam) {
        return this.orderCacheGateway.save(Order.builder().build())
                .flatMap(order -> {
                    return this.productGateway.reserveProduct(orderParam.getProductId(), orderParam.getProductCount())
                            .then(this.paymentGateway.execute(orderParam.getId())
                                    .flatMap(payment -> {
                                        Mono<Void> deleteCacheOrderFlow = this.orderCacheGateway.delete(order);
                                        Mono<Void> sendNotificationCreteOrderFlow = this.orderEventGateway.createOrder(order, payment);
                                        return this.orderGateway.save(orderParam)
                                                .then(Mono.zip(deleteCacheOrderFlow, sendNotificationCreteOrderFlow))
                                                .then(Mono.just(order));
                                    }));
                });
    }
}