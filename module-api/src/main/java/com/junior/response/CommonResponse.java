package com.junior.response;


import com.junior.exception.StatusCode;
import lombok.*;


@Getter
@Builder
public class CommonResponse<T> {
    private String customCode;
    private String customMessage;
    private T data;

    public static <T> CommonResponse<T> of(String code, String message, T info) {
        return new CommonResponse<>(code, message, info);
    }

    public static<T> CommonResponse<T> success(StatusCode statusCode, T data) {
        return new CommonResponse<>(statusCode.getCustomCode(), statusCode.getCustomMessage(), data);
    }

    public static<T> CommonResponse<T> fail(StatusCode statusCode) {
        return new CommonResponse<>(statusCode.getCustomCode(), statusCode.getCustomMessage(), null);
    }
}
