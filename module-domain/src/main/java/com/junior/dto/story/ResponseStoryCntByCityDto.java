package com.junior.dto.story;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

public record ResponseStoryCntByCityDto(
        String city,
        int cnt
) {
    @QueryProjection
    public ResponseStoryCntByCityDto(String city, int cnt) {
        this.city = city;
        this.cnt = cnt;
    }
}
