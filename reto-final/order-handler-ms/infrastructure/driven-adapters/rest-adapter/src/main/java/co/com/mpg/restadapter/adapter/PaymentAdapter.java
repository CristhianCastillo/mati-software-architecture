package co.com.mpg.restadapter.adapter;

import co.com.mpg.model.payment.Payment;
import co.com.mpg.model.payment.gateways.PaymentGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@Service
public class PaymentAdapter implements PaymentGateway {

    @Override
    public Mono<Payment> execute(String orderId) {
        return Mono.delay(Duration.ofMillis(200))
                .doOnSubscribe(sub -> log.info("Request :: Rest HTTP Execute Payment Order with Id {}", orderId))
                .thenReturn(Payment.builder()
                        .id("SomePaymentId")
                        .status("Pending")
                        .value("2000.00")
                        .build())
                .doOnSuccess(order -> log.info("Response :: Rest HTTP Execute Payment COMPLETED Order", kv("orderPayment", order)));
    }
}
