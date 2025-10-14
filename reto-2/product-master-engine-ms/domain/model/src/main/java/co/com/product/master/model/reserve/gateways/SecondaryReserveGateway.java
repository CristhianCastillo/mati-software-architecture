package co.com.product.master.model.reserve.gateways;

import co.com.product.master.model.reserve.Reserve;
import co.com.product.master.model.reserve.ReserveResult;
import reactor.core.publisher.Mono;

public interface SecondaryReserveGateway {
    Mono<ReserveResult> reserveProduct(Reserve reserve);
}
