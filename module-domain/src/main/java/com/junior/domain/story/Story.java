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

    // 나만 보기
    @Column(nullable = false)
    boolean isHidden;
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
    @Builder.Default
    @Column(nullable = false)
    private Boolean isDeleted = false;
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
    @Builder.Default
    private Long viewCnt = 0L;
    // 좋아요 수
    @Builder.Default
    private Long likeCnt = 0L;
    @ElementCollection
    @CollectionTable(name = "story_images", joinColumns = @JoinColumn(name = "story_id"))
    @Column(name = "image_url")
    private List<String> imgUrls;

    //FIXME: custom builder 삭제(기본 builder 사용하도록 수정하기, createStoryDto와 from함수를 사용하도록)
    @Builder(builderMethodName = "createStory", builderClassName = "CreateStory")
    public Story(Member member, String title, String content, String city, String thumbnailImg, double latitude, double longitude, boolean isHidden, List<String> imgUrls) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.city = city;
        this.thumbnailImg = thumbnailImg;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isHidden = isHidden;
        this.imgUrls = imgUrls;

//        this.likeMembers = new ArrayList<>();
        this.isDeleted = false;
        this.viewCnt = 0L;
        this.likeCnt = 0L;
    }

    public static Story from(Member member, CreateStoryDto createStoryDto) {
        return Story.builder()
                .member(member)
                .title(createStoryDto.title())
                .content(createStoryDto.content())
                .city(createStoryDto.city())
                .thumbnailImg(createStoryDto.thumbnailImg())
                .latitude(createStoryDto.latitude())
                .longitude(createStoryDto.longitude())
                .isHidden(createStoryDto.isHidden())
                .imgUrls(createStoryDto.imgUrls())
                .build();
    }

    public void updateStory(CreateStoryDto createStoryDto) {
        this.title = createStoryDto.title();
        this.content = createStoryDto.content();
        this.city = createStoryDto.city();
        this.thumbnailImg = createStoryDto.thumbnailImg();
        this.latitude = createStoryDto.latitude();
        this.longitude = createStoryDto.longitude();
        this.isHidden = createStoryDto.isHidden();
        this.imgUrls = createStoryDto.imgUrls();
    }

    public void increaseViewCnt() {
        this.viewCnt += 1;
    }

    public void deleteStory() {
        this.isDeleted = true;
    }

    public void increaseLikeCnt() {
        this.likeCnt += 1;
    }

    public void decreaseLikeCnt() {
        this.likeCnt -= 1;
    }
}
