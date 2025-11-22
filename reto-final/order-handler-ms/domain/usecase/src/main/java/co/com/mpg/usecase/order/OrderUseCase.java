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
                .flatMap(order -> Mono.<Order>error(new BusinessException(String.format("Order with Id: %s already exists", orderParam.getId()), TechnicalMessage.ORDER_ALREADY_EXISTS)))
                .switchIfEmpty(this.processOrder(orderParam));

    }

    private Mono<Order> processOrder(OrderParam orderParam) {
        return this.orderCacheGateway.save(Order.builder()
                        .id(orderParam.getId())
                        .productId(orderParam.getProductId())
                        .productCount(orderParam.getProductCount())
                        .build())
                .flatMap(order -> {
                    return this.productGateway.reserveProduct(orderParam.getProductId(), orderParam.getProductCount())
                            .then(this.paymentGateway.execute(orderParam.getId())
                                    .flatMap(payment -> {
                                        Mono<String> deleteCacheOrderFlow = this.orderCacheGateway.delete(order);
                                        Mono<String> sendNotificationCreteOrderFlow = this.orderEventGateway.createOrder(order, payment);
                                        return this.orderGateway.save(orderParam)
                                                .flatMap(orderSaved -> {
                                                    deleteCacheOrderFlow.subscribe();
                                                    sendNotificationCreteOrderFlow.subscribe();
                                                    return Mono.just(order);
                                                });
                                    }));
                });
    }
}