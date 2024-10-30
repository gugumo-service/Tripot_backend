package com.junior.dto.story;

import lombok.Builder;

@Builder
public record GeoPointDto(
        double latitude,
        double longitude
) {
}
