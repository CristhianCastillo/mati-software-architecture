package co.com.mpg.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TechnicalMessage {

    INTERNAL_ERROR("ORDER-HANDLER-500", "500", "We couldn't do this! Try again"),
    INVALID_REQUEST_DATA("ORDER-HANDLER-400", "400", "Parameters from request not valid"),
    ORDER_ALREADY_EXISTS("ORDER-ALREADY-EXISTS-400", "400", "The order already exists with the id enter");

    private final String code;
    private final String externalCode;
    private final String message;
}
