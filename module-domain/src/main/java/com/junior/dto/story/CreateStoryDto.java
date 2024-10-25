package com.junior.dto.story;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class CreateStoryDto {

    private String title;       // 제목
    private String content;     // 내용

    private String city;        // 위도, 경도가 속하는 지역

    private String thumbnailImg; //썸네일 이미지

    private double latitude;    // 위도(latitude)
    private double longitude;   // 경도(longitude)

    boolean isHidden;           // 나만 보기
}
