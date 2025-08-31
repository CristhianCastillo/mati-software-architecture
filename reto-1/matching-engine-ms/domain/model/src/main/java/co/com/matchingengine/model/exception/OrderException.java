package co.com.matchingengine.model.exception;

import co.com.matchingengine.model.enums.TechnicalMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderException extends RuntimeException {

    private final TechnicalMessage technicalMessage;

    public OrderException(String message, TechnicalMessage technicalMessage) {
        super(message);
        this.technicalMessage = technicalMessage;
    }

    public OrderException(Throwable cause, TechnicalMessage technicalMessage) {
        super(cause);
        this.technicalMessage = technicalMessage;
    }
}
