package com.junior.dto.story;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

@Builder
public record ResponseStoryListDto(
        String thumbnailImg,
        String title,
        String content,
        Long storyId
) {

    @QueryProjection
    public ResponseStoryListDto(String thumbnailImg, String title, String content, Long storyId) {
        this.thumbnailImg = thumbnailImg;
        this.title = title;
        this.content = content;
        this.storyId = storyId;
    }
}
