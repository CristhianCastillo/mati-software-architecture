package co.com.matchingengine.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TechnicalMessage {

    ORDER_ALREADY_EXISTS("ORDER-1OO", "400", "The Order already exists"),
    ORDER_NOT_EXISTS("ORDER-2OO", "400", "The Order not exists"),
    INVALID_REQUEST_DATA("ORDER-002", "400", "Parameters from request not valid"),
    ;

    private final String code;
    private final String externalCode;
    private final String message;
}
