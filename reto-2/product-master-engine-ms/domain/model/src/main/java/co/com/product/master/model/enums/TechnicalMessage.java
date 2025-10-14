package co.com.product.master.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TechnicalMessage {

    INTERNAL_ERROR("PRODUCT-MASTER-500", "500", "We couldn't do this! Try again"),
    UNAUTHORIZED_ERROR("PRODUCT-MASTER-401", "401", "No authorization to access the resource"),
    SERVICE_UNAVAILABLE_ERROR("PRODUCT-MASTER-503", "503", "Oups!!! The service is currently unable to handle the request"),
    CLIENT_GENERIC_ERROR("PRODUCT-MASTER-005", "PRODUCT-MASTER-005", "Error, on Service client"),
    INVALID_REQUEST_DATA("PRODUCT-MASTER-400", "400", "Parameters from request not valid");

    private final String code;
    private final String externalCode;
    private final String message;
}
