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
            log.info("Request :: Cache Get Order with Id {}", orderId);
            Order order = this.ordersCache.getIfPresent(orderId);
            if (order == null) {
                log.info("Response :: Cache Get Order NOT found with Id {}", orderId);
                return Mono.empty();
            }
            log.info("Response :: Cache Get Order FOUND with Id {}", order.getId());
            return Mono.just(order);
        });
    }

    @Override
    public Mono<Order> save(Order order) {
        return Mono.defer(() -> {
            log.info("Request :: Cache Save Order {}", order);
            this.ordersCache.put(order.getId(), order);
            log.info("Response :: Cache Save Order SAVED with Id {}", order.getId());
            return Mono.just(order);
        });
    }

    @Override
    public Mono<String> delete(Order order) {
        return Mono.defer(() -> {
            log.info("Request :: Cache Delete Order with Id {}", order.getId());
            this.ordersCache.invalidate(order.getId());
            log.info("Response :: Cache Delete Order DELETED with Id {}", order.getId());
            return Mono.just(order.getId());
        });
    }
}
