package com.junior.domain.story.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseStoryDto {

    private Long id;            // pk
    private String title;       // 제목
    private String content;     // 내용
    private double latitude;
    private double longitude;
    private String city;        // 위도, 경도가 속하는 지역
    private Long likeCnt;
    boolean isHidden;


    @QueryProjection

    public ResponseStoryDto(Long id, String title, String content, double latitude, double longitude, String city, Long likeCnt, boolean isHidden) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.likeCnt = likeCnt;
        this.isHidden = isHidden;
    }
}
