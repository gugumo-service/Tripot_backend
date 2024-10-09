package com.junior.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;


public record ReissueDto(
        String refreshToken
        ) {
}
