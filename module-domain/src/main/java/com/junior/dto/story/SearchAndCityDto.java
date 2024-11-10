package com.junior.dto.story;

import lombok.Builder;

@Builder
public record SearchAndCityDto(
        String city,
        String search
) {

}
