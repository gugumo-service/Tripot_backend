package com.junior.dto.version;

import lombok.Builder;

@Builder
public record VersionDto(
        String version,
        Boolean forceUpdate
) {
}
