package co.com.product.master.api.mapper;

import co.com.product.master.api.request.ProductReserveRequest;
import co.com.product.master.api.response.ProductReserveResponse;
import co.com.product.master.model.reserve.Reserve;
import co.com.product.master.model.reserve.ReserveResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductApiMapper {

    ProductApiMapper MAPPER = Mappers.getMapper(ProductApiMapper.class);

    @Mapping(target = "messageId", source = "messageId")
    @Mapping(target = "id", source = "productReserveRequest.id")
    @Mapping(target = "name", source = "productReserveRequest.name")
    @Mapping(target = "count", source = "productReserveRequest.count")
    @Mapping(target = "clientId", source = "productReserveRequest.clientId")
    Reserve requestToModel(ProductReserveRequest productReserveRequest, String messageId);

    ProductReserveResponse modelToResponse(ReserveResult reserveResult);
}
