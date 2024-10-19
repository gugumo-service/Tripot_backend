package com.junior.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


public record LoginCreateJwtDto (
        Long id,
        String username,
        String role,
        LocalDateTime requestTimeMs

)
{
    @Builder
    public LoginCreateJwtDto {
    }
}
