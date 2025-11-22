package co.com.mpg.api.mapper;

import co.com.mpg.api.request.OrderCreateRequest;
import co.com.mpg.api.response.dto.OrderDto;
import co.com.mpg.model.order.Order;
import co.com.mpg.model.order.OrderParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {

    OrderMapper MAPPER = Mappers.getMapper(OrderMapper.class);

    OrderParam orderRequestToModel(OrderCreateRequest orderCreateRequest);

    OrderDto modelToDto(Order order);
}
