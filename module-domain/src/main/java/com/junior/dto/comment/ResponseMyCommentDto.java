package com.junior.dto.comment;

import com.junior.domain.story.Story;

import java.time.LocalDateTime;

public record ResponseMyCommentDto(
        Long storyId,
        String content,
        LocalDateTime createDate,
        String storyTitle
) {
}
