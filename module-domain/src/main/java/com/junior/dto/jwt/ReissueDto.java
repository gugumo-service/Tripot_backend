package com.junior.dto.jwt;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;


public record ReissueDto(
        String refreshToken
        ) {
}
