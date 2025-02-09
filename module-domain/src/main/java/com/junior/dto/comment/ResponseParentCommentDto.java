package com.junior.dto.comment;

import java.time.LocalDateTime;

//@Builder
//@AllArgsConstructor
public record ResponseParentCommentDto(
        Long id,
        String content,
        Long memberId,
        String nickname,
        String profileImgPath,
        Long childCount,
        LocalDateTime createDate,
        boolean isAuthor
) {

}
