package co.com.matchingengine.lmaxtrading.adapter;

import co.com.matchingengine.lmaxtrading.db.PriceLevelPQ;
import co.com.matchingengine.model.enums.TechnicalMessage;
import co.com.matchingengine.model.exception.BusinessException;
import co.com.matchingengine.model.order.Order;
import co.com.matchingengine.model.order.OrderBook;
import co.com.matchingengine.model.order.OrderItem;
import co.com.matchingengine.model.order.OrderType;
import co.com.matchingengine.model.order.gateways.OrderGateway;
import co.com.matchingengine.model.quota.Quota;
import co.com.matchingengine.model.quota.QuotaSummary;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

@RequiredArgsConstructor
@Slf4j
@Component
public class OrderAdapter implements OrderGateway {

    private final Map<Integer, Order> orderCache = new HashMap<>();
    private final NavigableMap<Double, PriceLevelPQ> buyPriceLevels = new TreeMap<>();
    private final NavigableMap<Double, PriceLevelPQ> sellPriceLevels = new TreeMap<>();
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> addOrder(Order order) {
        return Mono.defer(() -> {
            if (this.orderCache.containsKey(order.getId())) {
                return Mono.error(new BusinessException("Order already exists", TechnicalMessage.ORDER_ALREADY_EXISTS));
            }
            return this.addOrderAndMatch(order);
        });
    }

    @Override
    public Mono<QuotaSummary> printBestBuyAndSell() {
        return Mono.defer(() -> {
            QuotaSummary quotaSummary = QuotaSummary.builder()
                    .build();
            if (!sellPriceLevels.isEmpty()) {
                Quota sellQuota = Quota.builder()
                        .bestPrice(sellPriceLevels.firstEntry().getValue().getPrice())
                        .totalVolumeActions(sellPriceLevels.firstEntry().getValue().getTotalVolume())
                        .totalOrders(sellPriceLevels.firstEntry().getValue().totalOrders())
                        .build();
                quotaSummary.setBestSellQuota(sellQuota);
            }

            if (!buyPriceLevels.isEmpty()) {
                Quota buyQuota = Quota.builder()
                        .bestPrice(buyPriceLevels.firstEntry().getValue().getPrice())
                        .totalVolumeActions(buyPriceLevels.firstEntry().getValue().getTotalVolume())
                        .totalOrders(buyPriceLevels.firstEntry().getValue().totalOrders())
                        .build();
                quotaSummary.setBestBuyQuota(buyQuota);
            }
            return Mono.just(quotaSummary);
        });
    }

    @Override
    public Mono<OrderBook> printOrderBook() {
        return Mono.defer(() -> {
            OrderBook orderBook = OrderBook.builder()
                    .build();
            if (!sellPriceLevels.isEmpty()) {
                orderBook.setSellOrders(getOrderItemsFromMap(sellPriceLevels));
            }
            if (!buyPriceLevels.isEmpty()) {
                orderBook.setBuyOrders(getOrderItemsFromMap(buyPriceLevels));
            }
            return Mono.just(orderBook);
        });
    }

    @Override
    public Mono<Void> updateOrder(final Order order) {
        return Mono.defer(() -> {
            if (!orderCache.containsKey(order.getId())) {
                return Mono.error(new BusinessException("Order does not exist", TechnicalMessage.ORDER_NOT_EXISTS));
            }
            final Order orderFound = this.orderCache.get(order.getId());
            Order orderToUpdate = new Order(orderFound);

            orderToUpdate.setQuantity(order.getQuantity());
            orderToUpdate.setPrice(order.getPrice());
            return removeAndAddOrder(orderFound, orderToUpdate)
                    .doOnSuccess(voi -> log.info("Order updated: {}", orderFound));
        });
    }

    @Override
    public Mono<Void> cancelOrder(int orderId) {
        return Mono.defer(() -> {
            if (this.orderCache.containsKey(orderId)) {
                final Order order = orderCache.get(orderId);
                return removeOrder(order)
                        .doOnSuccess(voi -> log.info("Order cancelled: {}", order));
            } else {
                log.error("Order ID not found: {}", orderId);
            }
            return Mono.empty();
        });
    }

    private Mono<Void> addOrderAndMatch(final Order order) {
        return Mono.defer(() -> {
            this.orderCache.put(order.getId(), order);
            final double price = order.getPrice();
            if (order.getOrderType() == OrderType.BUY) {
                this.buyPriceLevels.computeIfAbsent(price, pl -> new PriceLevelPQ(price)).addOrder(order);
            } else {
                this.sellPriceLevels.computeIfAbsent(price, pl -> new PriceLevelPQ(price)).addOrder(order);
            }
            return Mono.empty()
                    .doOnSuccess(i -> {
                        log.info("Buy Orders: {}", this.buyPriceLevels);
                        log.info("Sell Orders: {}", this.sellPriceLevels);
                    })
                    .doOnSuccess(i -> this.matchOrders()
                            .subscribeOn(Schedulers.boundedElastic())
                            .subscribe())
                    .then();
        });
    }

    private Mono<Void> matchOrders() {
        return Mono.defer(() -> {
                    while (!buyPriceLevels.isEmpty() && !sellPriceLevels.isEmpty() &&
                            Double.compare(buyPriceLevels.lastKey(), sellPriceLevels.firstKey()) >= 0) {
                        long startTime = System.currentTimeMillis();
                        final Map.Entry<Double, PriceLevelPQ> buyEntry = buyPriceLevels.pollLastEntry(); // O(1)
                        final Map.Entry<Double, PriceLevelPQ> sellEntry = sellPriceLevels.pollFirstEntry(); // O(1)

                        final PriceLevelPQ buyPriceLevel = buyEntry.getValue();
                        final PriceLevelPQ sellPriceLevel = sellEntry.getValue();

                        final Order buyOrder = buyPriceLevel.pollBestOrder();
                        final Order sellOrder = sellPriceLevel.pollBestOrder();

                        log.info("BEFORE MATCH :: Buy Order: {} with Sell Order: {}", buyOrder, sellOrder);

                        final int buyQty = buyOrder.getQuantity();
                        final int sellQty = sellOrder.getQuantity();

                        final int fillQty = Math.min(buyQty, sellQty);
                        buyOrder.setQuantity(buyQty - fillQty);
                        sellOrder.setQuantity(sellQty - fillQty);

                        log.info("Matched Order Quantity: {} ;; {}", fillQty, sellOrder.getPrice());
                        log.info("AFTER MATCH :: Buy Order: {} with Sell Order: {}", buyOrder, sellOrder);

                        if (buyOrder.getQuantity() > 0) {
                            buyPriceLevel.addOrder(buyOrder);
                            buyPriceLevels.put(buyEntry.getKey(), buyPriceLevel);
                        }
                        if (sellOrder.getQuantity() > 0) {
                            sellPriceLevel.addOrder(sellOrder);
                            sellPriceLevels.put(sellEntry.getKey(), sellPriceLevel);
                        }
                        if (!buyPriceLevel.isEmpty()) {
                            buyPriceLevels.putIfAbsent(buyEntry.getKey(), buyPriceLevel);
                        }
                        if (!sellPriceLevel.isEmpty()) {
                            sellPriceLevels.putIfAbsent(sellEntry.getKey(), sellPriceLevel);
                        }
                        long endTime = System.currentTimeMillis() - startTime;
                        log.info("Time taken to match order: {} ms", endTime);
                    }
                    return Mono.empty()
                            .doOnSuccess(i -> {
                                log.info("AFTER MATCH :: Buy Orders: {}", this.buyPriceLevels);
                                log.info("AFTER MATCH :: Sell Orders: {}", this.sellPriceLevels);
                            })
                            .then();
                })
                .doOnSubscribe(sub -> log.info("******Starting order matching..."))
                .doOnSuccess(voi -> log.info("******Order matching completed."))
                .doOnError(error -> log.error("******Error occurred while matching orders.", error))
                .then();
    }

    private List<OrderItem> getOrderItemsFromMap(final NavigableMap<Double, PriceLevelPQ> priceLevels) {
        List<OrderItem> orderLists = new ArrayList<>();
        for (final Double price : priceLevels.descendingKeySet()) {
            OrderItem orderItem = OrderItem.builder().build();
            orderItem.setPrice(price);
            final PriceLevelPQ priceLevel = priceLevels.get(price);
            orderItem.setTotalVolumeActions(priceLevel.getTotalVolume());
            orderItem.setTotalOrders(priceLevel.totalOrders());
            for (final Order order : priceLevel.getOrders()) {
                orderItem.addOrder(order);
            }
            orderLists.add(orderItem);
        }
        return orderLists;
    }

    private Mono<Void> removeOrder(final NavigableMap<Double, PriceLevelPQ> priceLevels, final Order order) {
        return Mono.defer(() -> {
            final PriceLevelPQ priceLevel = priceLevels.get(order.getPrice());
            if (priceLevel != null && !priceLevel.isEmpty()) {
                priceLevel.removeOrder(order);
            }
            if (priceLevel != null && priceLevel.isEmpty()) {
                priceLevels.remove(order.getPrice());
            }
            return Mono.empty();
        });
    }

    private Mono<Void> removeOrder(final Order order) {
        return Mono.defer(() -> {
            this.orderCache.remove(order.getId());
            if (order.getOrderType() == OrderType.BUY) {
                return removeOrder(buyPriceLevels, order);
            } else {
                return removeOrder(sellPriceLevels, order);
            }
        });
    }

    private Mono<Void> removeAndAddOrder(final Order order, final Order orderToAdd) {
        return Mono.defer(() -> removeOrder(order)
                .then(addOrderAndMatch(orderToAdd)));
    }
}
