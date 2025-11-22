package co.com.mpg.cacheadapter.adapter;

import co.com.mpg.model.order.Order;
import co.com.mpg.model.order.gateways.OrderCacheGateway;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@EnableCaching
public class OrderCacheAdapter implements OrderCacheGateway {

    private final Cache<String, Order> ordersCache;

    public OrderCacheAdapter() {
        this.ordersCache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build();
    }

    @Override
    public Mono<Order> getById(String orderId) {
        return Mono.defer(() -> {
            Order order = this.ordersCache.getIfPresent(orderId);
            if (order == null) {
                log.info("Order NOT found with id={}", orderId);
                return Mono.empty();
            }
            log.info("Order found with id={}", order.getId());
            return Mono.just(order);
        });
    }

    @Override
    public Mono<Order> save(Order order) {
        return Mono.defer(() -> {
            this.ordersCache.put(order.getId(), order);
            return Mono.just(order);
        });
    }

    @Override
    public Mono<Void> delete(Order order) {
        return Mono.defer(() -> {
            this.ordersCache.invalidate(order.getId());
            log.info("Order Cache deleted with id={}", order.getId());
            return Mono.empty();
        });
    }
}
