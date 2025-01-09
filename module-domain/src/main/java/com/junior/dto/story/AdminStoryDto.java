package com.junior.dto.story;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

@Builder
public record AdminStoryDto(

        String title,
        String city,
        Long id,
        String createdUsername,
        Boolean isDeleted
) {

    @QueryProjection
    public AdminStoryDto(String title, String city, Long id, String createdUsername, Boolean isDeleted) {
        this.title = title;
        this.city = city;
        this.id = id;
        this.createdUsername = createdUsername;
        this.isDeleted = isDeleted;
    }
}
