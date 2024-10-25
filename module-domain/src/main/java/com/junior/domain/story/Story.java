package com.junior.domain.story;

import com.junior.domain.member.Member;
import com.junior.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Story extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "story_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

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

    // 썸네일 이미지(대표 이미지)
    @Column(length = 255)
    private String thumbnailImg;

    // 조회 수
    private Long viewCnt;
    // 좋아요 수
    private Long likeCnt;

    // 나만 보기
    boolean isHidden;

    @Builder(builderMethodName = "createStory", builderClassName = "CreateStory")
    public Story(String title, String content, String city, String thumbnailImg, double latitude, double longitude, boolean isHidden) {
        this.title = title;
        this.content = content;
        this.city = city;
        this.thumbnailImg = thumbnailImg;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isHidden = isHidden;

        this.viewCnt = 0L;
        this.likeCnt = 0L;
    }
}
