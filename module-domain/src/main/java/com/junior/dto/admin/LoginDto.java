package com.junior.dto.admin;

import lombok.Builder;


@Builder
public record LoginDto(
        String username,
        String password
) {
}
