package com.junior.dto.story;

import lombok.Builder;

@Builder
public record GeoRect(
        GeoPointDto geoPointLt,
        GeoPointDto geoPointRb
) {
}
