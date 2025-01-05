package com.junior.dto.story;

import com.junior.domain.story.Story;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record AdminStoryDetailDto(
        Long id,
        String title,
        String content,
        String thumbnailImg,
        double latitude,
        double longitude,
        String city,
        Long likeCnt,
        LocalDateTime createDate,
        List<String> imgUrls
) {



    public static AdminStoryDetailDto from(Story story) {
        return AdminStoryDetailDto.builder()
                .id(story.getId())
                .title(story.getTitle())
                .content(story.getContent())
                .thumbnailImg(story.getThumbnailImg())
                .latitude(story.getLatitude())
                .longitude(story.getLongitude())
                .city(story.getCity())
                .likeCnt(story.getLikeCnt())
                .imgUrls(story.getImgUrls())
                .createDate(story.getCreatedDate())
                .build();
    }
}
