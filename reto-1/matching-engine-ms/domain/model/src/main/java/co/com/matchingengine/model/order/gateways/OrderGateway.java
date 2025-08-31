package co.com.matchingengine.model.order.gateways;

import co.com.matchingengine.model.order.Order;
import co.com.matchingengine.model.order.OrderBook;
import co.com.matchingengine.model.quota.QuotaSummary;
import reactor.core.publisher.Mono;

public interface OrderGateway {

    Mono<Void> addOrder(Order order);

    Mono<QuotaSummary> printBestBuyAndSell();

    Mono<OrderBook> printOrderBook();

    Mono<Void> updateOrder(Order order);

    Mono<Void> cancelOrder(int orderId);
}
