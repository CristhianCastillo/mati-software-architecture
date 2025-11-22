package co.com.mpg.api.manager;

import co.com.mpg.api.mapper.OrderMapper;
import co.com.mpg.api.request.OrderCreateRequest;
import co.com.mpg.api.response.dto.OrderDto;
import co.com.mpg.usecase.order.OrderUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Slf4j
public class OrderManager {

    private final OrderUseCase orderUseCase;

    public Mono<OrderDto> createOrder(OrderCreateRequest orderCreateRequest) {
        return this.orderUseCase.createOrder(OrderMapper.MAPPER.orderRequestToModel(orderCreateRequest))
                .map(OrderMapper.MAPPER::modelToDto);
    }
}
