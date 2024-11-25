package com.junior.dto.notice;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NoticeUserDto(
        Long id,
        String title,
        String content,
        LocalDateTime createdDate
) {

    @QueryProjection
    public NoticeUserDto(Long id, String title, String content, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
    }
}
