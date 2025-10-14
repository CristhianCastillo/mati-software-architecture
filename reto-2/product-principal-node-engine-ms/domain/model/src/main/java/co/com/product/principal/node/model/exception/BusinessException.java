package co.com.product.principal.node.model.exception;

import co.com.product.principal.node.model.enums.TechnicalMessage;
import lombok.Getter;

@Getter
public class BusinessException extends ProductException {

    public BusinessException(TechnicalMessage technicalMessage) {
        super(technicalMessage);
    }

    public BusinessException(String message, TechnicalMessage technicalMessage) {
        super(message, technicalMessage);
    }
}