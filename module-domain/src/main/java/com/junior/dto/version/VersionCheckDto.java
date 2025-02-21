package com.junior.dto.version;

import lombok.Builder;

@Builder
public record VersionCheckDto(
        String version
) {
}
