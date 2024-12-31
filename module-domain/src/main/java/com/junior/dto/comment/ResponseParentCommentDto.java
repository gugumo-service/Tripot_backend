package com.junior.dto.comment;

import com.junior.domain.member.Member;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;

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
        LocalDateTime createDate
) {

}
