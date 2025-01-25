package com.junior.dto.qna;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record QnaUserDto(
        Long id,
        String question,
        String answer,
        LocalDateTime createdDate
) {

    @QueryProjection
    public QnaUserDto(Long id, String question, String answer, LocalDateTime createdDate) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.createdDate = createdDate;
    }
}
