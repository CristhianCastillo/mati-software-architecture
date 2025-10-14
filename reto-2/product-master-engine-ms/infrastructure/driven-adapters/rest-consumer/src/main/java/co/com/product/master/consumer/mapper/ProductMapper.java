package co.com.product.master.consumer.mapper;

import co.com.product.master.consumer.request.ReserveRequest;
import co.com.product.master.consumer.response.ProductReserveResponseDto;
import co.com.product.master.model.reserve.Reserve;
import co.com.product.master.model.reserve.ReserveResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {

    ProductMapper MAPPER = Mappers.getMapper(ProductMapper.class);

    ReserveRequest modelToRequest(Reserve reserve);

    ReserveResult responseToModel(ProductReserveResponseDto productReserveResponseDto);
}
