package co.com.product.principal.node.api.response.generic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class GenericResponse<T> {

    private static final int SUCCESS_CODE = 0;
    private static final int NO_SESSION_ERROR_CODE = -1;
    public static final int ERROR_CODE = -2;

    private int code;

    private String description;

    private T result;

    public GenericResponse(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public GenericResponse(int code, String description, T result) {
        this.code = code;
        this.description = description;
        this.result = result;
    }

    public GenericResponse() {
        super();
    }

    public static <T> GenericResponse<T> success() {
        return new GenericResponse<>(SUCCESS_CODE, "Success");
    }

    public static <T> GenericResponse<T> success(String description) {
        return new GenericResponse<>(SUCCESS_CODE, description);
    }

    public static <T> GenericResponse<T> success(T object) {
        return new GenericResponse<>(SUCCESS_CODE, "Success", object);
    }

    public static <T> GenericResponse<T> error(String description) {
        return new GenericResponse<>(ERROR_CODE, description);
    }
}