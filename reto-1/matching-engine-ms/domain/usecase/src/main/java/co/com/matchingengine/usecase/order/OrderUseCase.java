package co.com.matchingengine.usecase.order;

import co.com.matchingengine.model.order.Order;
import co.com.matchingengine.model.order.OrderBook;
import co.com.matchingengine.model.order.gateways.OrderGateway;
import co.com.matchingengine.model.quota.QuotaSummary;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class OrderUseCase {

    private final OrderGateway orderGateway;

    public Mono<Void> addOrder(Order order) {
        return orderGateway.addOrder(order);
    }

    public Mono<QuotaSummary> getBestBuyAndSell() {
        return orderGateway.printBestBuyAndSell();
    }

    public Mono<OrderBook> getOrderBook() {
        return orderGateway.printOrderBook();
    }

    public Mono<Void> updateOrder(Order order) {
        return orderGateway.updateOrder(order);
    }

    public Mono<Void> cancelOrder(int orderId) {
        return orderGateway.cancelOrder(orderId);
    }
}
