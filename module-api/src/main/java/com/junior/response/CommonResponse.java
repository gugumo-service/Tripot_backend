package com.junior.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonResponse<T> {
    private String returnCode;
    private String returnMessage;
    private T info;
}
