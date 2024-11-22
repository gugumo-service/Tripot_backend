package com.junior.dto.comment;

import lombok.Builder;

@Builder
public record CreateCommentDto(
        Long storyId,
        Long parentCommentId,
        String content
) {
}
