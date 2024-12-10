package com.junior.dto.qna;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

@Builder
public record QnaAdminDto(
        Long id,
        String question
) {

    @QueryProjection
    public QnaAdminDto(Long id, String question) {
        this.id = id;
        this.question = question;
    }
}
