package com.junior.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
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
