package co.com.product.master.model.exception;

import co.com.product.master.model.enums.TechnicalMessage;
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