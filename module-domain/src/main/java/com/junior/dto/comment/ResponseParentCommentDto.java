package com.junior.dto.comment;

import com.junior.domain.member.Member;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

@Builder
public record ResponseParentCommentDto(
        Long id,
        String content,
        Member member,
        Long childCount
) {

    @QueryProjection
    public ResponseParentCommentDto(Long id, String content, Member member, Long childCount) {
        this.id = id;
        this.content = content;
        this.member = member;
        this.childCount = childCount;
    }
}
