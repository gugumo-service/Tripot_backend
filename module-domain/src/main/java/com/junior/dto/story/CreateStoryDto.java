package com.junior.dto.story;

import lombok.Builder;

import java.util.List;

@Builder
public record CreateStoryDto(
        String title,
        String content,
        String city,
        String thumbnailImg,
        double latitude,
        double longitude,
        boolean isHidden,
        List<String> imgUrls
) {

}

