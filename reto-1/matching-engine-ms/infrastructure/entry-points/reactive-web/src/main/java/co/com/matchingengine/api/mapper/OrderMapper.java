package co.com.matchingengine.api.mapper;

import co.com.matchingengine.api.request.CreateOrderRequest;
import co.com.matchingengine.api.request.UpdateOrderRequest;
import co.com.matchingengine.api.response.dto.OrderBookDTO;
import co.com.matchingengine.api.response.dto.QuotaSummaryDTO;
import co.com.matchingengine.model.order.Order;
import co.com.matchingengine.model.order.OrderBook;
import co.com.matchingengine.model.order.OrderType;
import co.com.matchingengine.model.quota.QuotaSummary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {

    OrderMapper MAPPER = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "entryTime", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "orderType", source = "createOrderRequest.orderType", qualifiedByName = "getOrderType")
    Order createOrderRequestToModel(CreateOrderRequest createOrderRequest);

    @Mapping(target = "entryTime", ignore = true)
    @Mapping(target = "orderType", ignore = true)
    Order updateOrderRequestToModel(UpdateOrderRequest createOrderRequest);

    OrderBookDTO modelToOrderBookDTO(OrderBook orderBook);

    QuotaSummaryDTO modelToQuotaSummaryDTO(QuotaSummary quotaSummary);

    @Named("getOrderType")
    default OrderType getOrderType(String orderType) {
        if (orderType == null) {
            return null;
        }
        return OrderType.valueOf(orderType.toUpperCase());
    }
}
