package com.junior.dto.comment;

import com.junior.domain.member.Member;

public record ResponseChildCommentDto(
        Long id,
        String content,
        Long memberId,
        String nickname,
        String profileImgPath
) {

}
