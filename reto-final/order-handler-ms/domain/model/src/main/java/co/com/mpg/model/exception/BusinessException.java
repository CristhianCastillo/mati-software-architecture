package co.com.mpg.model.exception;

import co.com.mpg.model.enums.TechnicalMessage;
import lombok.Getter;

@Getter
public class BusinessException extends OrderException {

    public BusinessException(TechnicalMessage technicalMessage) {
        super(technicalMessage);
    }

    public BusinessException(String message, TechnicalMessage technicalMessage) {
        super(message, technicalMessage);
    }
}