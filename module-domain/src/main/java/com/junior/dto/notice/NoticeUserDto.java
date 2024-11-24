package com.junior.dto.notice;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

@Builder
public record NoticeUserDto(
        Long id,
        String title,
        String content
) {

    @QueryProjection
    public NoticeUserDto(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
