package com.junior.dto.story;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public record ResponseStoryDto(
        Long id,
        String title,
        String content,
        String thumbnailImg,
        double latitude,
        double longitude,
        String city,
        Long likeCnt,
        boolean isHidden,
        LocalDateTime createDate
) {
    @QueryProjection
    public ResponseStoryDto(Long id, String title, String content, String thumbnailImg, double latitude, double longitude, String city, Long likeCnt, boolean isHidden, LocalDateTime createDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.thumbnailImg = thumbnailImg;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.likeCnt = likeCnt;
        this.isHidden = isHidden;
        this.createDate = createDate;
    }
}
