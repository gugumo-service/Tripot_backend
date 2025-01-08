package com.junior.dto.story;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

@Builder
public record AdminStoryDto(
        String thumbnailImg,
        String title,
        String content,
        String city,
        Long storyId,
        double latitude,
        double longitude,
        Boolean isDeleted
) {

    @QueryProjection
    public AdminStoryDto(String thumbnailImg, String title, String content, String city, Long storyId, double latitude, double longitude, Boolean isDeleted) {
        this.thumbnailImg = thumbnailImg;
        this.title = title;
        this.content = content;
        this.city = city;
        this.storyId = storyId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isDeleted = isDeleted;
    }
}
