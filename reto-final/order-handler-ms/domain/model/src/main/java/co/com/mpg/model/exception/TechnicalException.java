package co.com.mpg.model.exception;

import co.com.mpg.model.enums.TechnicalMessage;
import lombok.Getter;

@Getter
public class TechnicalException extends OrderException {

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