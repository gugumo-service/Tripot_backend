package com.junior.dto.qna;

import lombok.Builder;

@Builder
public record QnaDetailDto(
        Long id,
        String question,
        String answer
) {

}

