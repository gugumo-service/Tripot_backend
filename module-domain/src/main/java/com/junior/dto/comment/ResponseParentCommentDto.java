package com.junior.dto.comment;

import com.junior.domain.member.Member;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

@Builder
public record ResponseParentCommentDto(
        Long id,
        String content,
        Long memberId,
        String nickname,
        String profileImgPath,
        Long childCount
) {

}
