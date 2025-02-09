package com.junior.dto.comment;

import java.time.LocalDateTime;

public record ResponseChildCommentDto(
        Long id,
        String content,
        Long memberId,
        String nickname,
        String profileImgPath,
        LocalDateTime createDate,
        boolean isAuthor
) {

}
