package com.junior.domain.story;

import com.junior.domain.base.BaseEntity;
import com.junior.domain.member.Member;
import com.junior.dto.story.CreateStoryDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Story extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "story_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(length = 65535, nullable = false)
    private String content;

    @Column(nullable = false)
    private Boolean isDeleted;

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
    @Column(nullable = false)
    boolean isHidden;

    @Builder(builderMethodName = "createStory", builderClassName = "CreateStory")
    public Story(Member member, String title, String content, String city, String thumbnailImg, double latitude, double longitude, boolean isHidden) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.city = city;
        this.thumbnailImg = thumbnailImg;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isHidden = isHidden;

        this.isDeleted = false;
        this.viewCnt = 0L;
        this.likeCnt = 0L;
    }

    public void updateStory(CreateStoryDto createStoryDto) {
        this.title = createStoryDto.title();
        this.content = createStoryDto.content();
        this.city = createStoryDto.city();
        this.latitude = createStoryDto.latitude();
        this.longitude = createStoryDto.longitude();
        this.thumbnailImg = createStoryDto.thumbnailImg();
    }
}
