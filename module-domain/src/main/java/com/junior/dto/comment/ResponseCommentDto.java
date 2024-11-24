package com.junior.dto.comment;

import lombok.Builder;

import java.util.List;

@Builder
public record ResponseCommentDto(
        Long id,
        String content,
        List<ResponseCommentDto> child
) {
}
