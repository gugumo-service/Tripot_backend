package com.junior.dto.jwt;

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
