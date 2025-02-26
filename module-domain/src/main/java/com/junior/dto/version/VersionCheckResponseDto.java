package com.junior.dto.version;

import lombok.Builder;

@Builder
public record VersionCheckResponseDto(
        boolean forceUpdate,
        boolean requireUpdate
) {
}
