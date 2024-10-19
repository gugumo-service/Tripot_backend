package com.junior.domain.article;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @Column(length = 255)
    private String title;

    @Column(length = 65535)
    private String content;

    /// string 형식으로 작성했지만 추후 관심지역 방식에 따라 바뀔 수 있음.
    @Column(length = 255)
    private String city;

    // 마커에 사용될 위도(latitude), 경도(longitude)
    private double latitude;
    private double longitude;

    // 조회 수
    private Long viewCnt;
    // 좋아요 수
    private Long likeCnt;

    // 나만 보기
    boolean isHidden;
}
