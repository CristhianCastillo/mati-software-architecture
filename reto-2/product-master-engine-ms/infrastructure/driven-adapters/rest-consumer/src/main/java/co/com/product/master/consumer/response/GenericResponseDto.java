package co.com.product.master.consumer.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class GenericResponseDto<T> {

    private static final int SUCCESS_CODE = 0;
    private static final int NO_SESSION_ERROR_CODE = -1;
    public static final int ERROR_CODE = -2;

    private int code;
    private String description;
    private T result;

    public GenericResponseDto(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public GenericResponseDto(int code, String description, T result) {
        this.code = code;
        this.description = description;
        this.result = result;
    }

    public GenericResponseDto() {
        super();
    }

    public static <T> GenericResponseDto<T> success() {
        return new GenericResponseDto<>(SUCCESS_CODE, "Success");
    }

    public static <T> GenericResponseDto<T> success(String description) {
        return new GenericResponseDto<>(SUCCESS_CODE, description);
    }

    public static <T> GenericResponseDto<T> success(T object) {
        return new GenericResponseDto<>(SUCCESS_CODE, "Success", object);
    }

    public static <T> GenericResponseDto<T> error(String description) {
        return new GenericResponseDto<>(ERROR_CODE, description);
    }
}