package co.com.matchingengine.api.manager;

import co.com.matchingengine.api.mapper.OrderMapper;
import co.com.matchingengine.api.request.CreateOrderRequest;
import co.com.matchingengine.api.request.UpdateOrderRequest;
import co.com.matchingengine.api.response.dto.OrderBookDTO;
import co.com.matchingengine.api.response.dto.QuotaSummaryDTO;
import co.com.matchingengine.usecase.order.OrderUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

@Service
@AllArgsConstructor
@Slf4j
public class MatchEngineManager {

    private final OrderUseCase orderUseCase;
    private static final AtomicInteger counter = new AtomicInteger(0);

    public Mono<Void> addOrder(CreateOrderRequest createOrderRequest) {
        createOrderRequest.setId(counter.incrementAndGet());
        return this.orderUseCase.addOrder(OrderMapper.MAPPER.createOrderRequestToModel(createOrderRequest));
    }

    public Mono<OrderBookDTO> getOrderBook() {
        return this.orderUseCase.getOrderBook()
                .map(OrderMapper.MAPPER::modelToOrderBookDTO);
    }

    public Mono<QuotaSummaryDTO> getBestBuyAndSell() {
        return this.orderUseCase.getBestBuyAndSell()
                .map(OrderMapper.MAPPER::modelToQuotaSummaryDTO);
    }

    public Mono<Void> updateOrder(UpdateOrderRequest updateOrderRequest) {
        return this.orderUseCase.updateOrder(OrderMapper.MAPPER.updateOrderRequestToModel(updateOrderRequest));
    }

    public Mono<Void> cancelOrder(int orderId) {
        return this.orderUseCase.cancelOrder(orderId);
    }
}
