package co.com.product.secondary.node.model.exception;

import co.com.product.secondary.node.model.enums.TechnicalMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProductException extends RuntimeException {

    private final TechnicalMessage technicalMessage;

    public ProductException(String message, TechnicalMessage technicalMessage) {
        super(message);
        this.technicalMessage = technicalMessage;
    }

    public ProductException(Throwable cause, TechnicalMessage technicalMessage) {
        super(cause);
        this.technicalMessage = technicalMessage;
    }
}
