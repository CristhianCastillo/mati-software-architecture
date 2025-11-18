package co.com.mpg.model.payment.gateways;

import co.com.mpg.model.payment.Payment;
import reactor.core.publisher.Mono;

public interface PaymentGateway {
    Mono<Payment> execute(String orderId);
}
