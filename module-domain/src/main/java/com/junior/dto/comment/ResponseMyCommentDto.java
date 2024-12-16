package com.junior.dto.comment;

import com.junior.domain.story.Story;

public record ResponseMyCommentDto(
        Story story,
        String content
) {
}
