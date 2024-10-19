package com.junior.response;


import lombok.*;

@Getter
@Builder
public class CommonResponse<T> {
    private String returnCode;
    private String returnMessage;
    private T info;



    public static <T> CommonResponse<T> of(String code, String message, T info) {
        return new CommonResponse<>(code, message, info);
    }
}
