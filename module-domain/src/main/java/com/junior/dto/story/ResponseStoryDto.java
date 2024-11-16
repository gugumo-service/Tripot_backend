package com.junior.dto.story;

import com.junior.domain.story.Story;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
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

    public static ResponseStoryDto from(Story story) {
        return ResponseStoryDto.builder()
                .id(story.getId())
                .title(story.getTitle())
                .content(story.getContent())
                .thumbnailImg(story.getThumbnailImg())
                .latitude(story.getLatitude())
                .longitude(story.getLongitude())
                .city(story.getCity())
                .likeCnt(story.getLikeCnt())
                .isHidden(story.isHidden())
                .build();
    }
}
