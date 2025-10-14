package co.com.product.master.usecase.reserve;

import co.com.product.master.model.exception.TechnicalException;
import co.com.product.master.model.reserve.Reserve;
import co.com.product.master.model.reserve.ReserveResult;
import co.com.product.master.model.reserve.gateways.PrincipalReserveGateway;
import co.com.product.master.model.reserve.gateways.SecondaryReserveGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ReserveUseCase {
    
    private final PrincipalReserveGateway principalReserveGateway;
    private final SecondaryReserveGateway secondaryReserveGateway;

    public Mono<ReserveResult> reserveProduct(Reserve reserve) {
        return this.principalReserveGateway.reserveProduct(reserve)
                .onErrorResume(TechnicalException.class,
                        e -> this.secondaryReserveGateway.reserveProduct(reserve));
    }
}
