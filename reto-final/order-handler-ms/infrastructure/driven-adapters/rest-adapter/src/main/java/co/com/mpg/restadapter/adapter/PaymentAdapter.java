package co.com.mpg.restadapter.adapter;

import co.com.mpg.model.payment.Payment;
import co.com.mpg.model.payment.gateways.PaymentGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Service
public class PaymentAdapter implements PaymentGateway {

    @Override
    public Mono<Payment> execute(String orderId) {
        return Mono.delay(Duration.ofMillis(200))
                .doOnSubscribe(sub -> log.info("Starting Payment order with id {}", orderId))
                .thenReturn(Payment.builder()
                        .id("SomePaymentId")
                        .status("Pending")
                        .value("2000.00")
                        .build())
                .doOnSuccess(res -> log.info("Payment completed for order with id {}", orderId));
    }
}
