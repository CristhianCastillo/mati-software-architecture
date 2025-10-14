package co.com.product.principal.node.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TechnicalMessage {

    INTERNAL_ERROR("PRODUCT-500", "500", "We couldn't do this! Try again"),
    INVALID_REQUEST_DATA("PRODUCT-400", "400", "Parameters from request not valid");

    private final String code;
    private final String externalCode;
    private final String message;
}
