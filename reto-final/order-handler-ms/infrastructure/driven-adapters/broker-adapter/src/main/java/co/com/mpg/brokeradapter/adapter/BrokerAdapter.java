package co.com.mpg.brokeradapter.adapter;

import co.com.mpg.model.order.Order;
import co.com.mpg.model.order.gateways.OrderEventGateway;
import co.com.mpg.model.payment.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@Slf4j
public class BrokerAdapter implements OrderEventGateway {

    @Override
    public Mono<String> createOrder(Order order, Payment payment) {
        return Mono.delay(Duration.ofMillis(100))
                .doOnSubscribe(sub -> log.info("Starting Send Notification for Order with Id {} and Payment with Id {}", order.getId(), payment.getId()))
                .thenReturn("SomeNotificationId")
                .doOnSuccess(su -> log.info("Notification sent for Order with Id {} and Payment with Id {}", order.getId(), payment.getId()));
    }
}
