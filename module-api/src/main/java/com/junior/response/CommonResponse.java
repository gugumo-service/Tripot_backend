package com.junior.response;

import lombok.*;


@Builder
public record CommonResponse<T> (
        String returnCode,
        String returnMessage,
        T info
){


    public static <T> CommonResponse<T> of(String code, String message, T info) {
        return new CommonResponse<>(code, message, info);
    }

}
