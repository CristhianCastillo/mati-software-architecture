package co.com.product.master.model.exception;

import co.com.product.master.model.enums.TechnicalMessage;
import lombok.Getter;

@Getter
public class TechnicalException extends ProductException {

    public TechnicalException(TechnicalMessage technicalMessage) {
        super(technicalMessage);
    }

    public TechnicalException(Throwable cause, TechnicalMessage technicalMessage) {
        super(cause, technicalMessage);
    }

    public TechnicalException(String message, TechnicalMessage technicalMessage) {
        super(message, technicalMessage);
    }
}