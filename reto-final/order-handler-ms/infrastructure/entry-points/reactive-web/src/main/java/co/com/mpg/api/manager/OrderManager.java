package co.com.mpg.api.manager;

import co.com.mpg.api.mapper.OrderMapper;
import co.com.mpg.api.request.OrderCreateRequest;
import co.com.mpg.api.response.dto.OrderDto;
import co.com.mpg.usecase.order.OrderUseCase;
import io.micrometer.core.instrument.Timer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Slf4j
public class OrderManager {

    private final OrderUseCase orderUseCase;
    private final Timer processOrderTimer;

    public Mono<OrderDto> createOrder(OrderCreateRequest orderCreateRequest) {
        Timer.Sample sample = Timer.start();
        return orderUseCase.createOrder(OrderMapper.MAPPER.orderRequestToModel(orderCreateRequest))
                .doOnTerminate(() -> sample.stop(processOrderTimer))
                .map(OrderMapper.MAPPER::modelToDto);
    }
}
