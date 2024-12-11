package com.junior.dto.story;

import com.junior.domain.member.Member;
import com.junior.domain.story.Story;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ResponseStoryDto(
        Long id,
        String title,
        String content,
        String thumbnailImg,
        double latitude,
        double longitude,
        String city,
        Member author,
        boolean isLikedByUser,
        Long likeCnt,
        boolean isHidden,
        LocalDateTime createDate,
        List<String> imgUrls
) {

    @QueryProjection
    public ResponseStoryDto(Long id, String title, String content, String thumbnailImg, double latitude, double longitude, String city, Member author, boolean isLikedByUser, Long likeCnt, boolean isHidden, LocalDateTime createDate, List<String> imgUrls) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.thumbnailImg = thumbnailImg;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.author = author;
        this.isLikedByUser = isLikedByUser;
        this.likeCnt = likeCnt;
        this.isHidden = isHidden;
        this.createDate = createDate;
        this.imgUrls = imgUrls;
    }

    public static ResponseStoryDto from(Story story, boolean isLikedByUser) {
        return ResponseStoryDto.builder()
                .id(story.getId())
                .title(story.getTitle())
                .content(story.getContent())
                .thumbnailImg(story.getThumbnailImg())
                .latitude(story.getLatitude())
                .longitude(story.getLongitude())
                .city(story.getCity())
                .author(story.getMember())
                .isLikedByUser(isLikedByUser)
                .likeCnt(story.getLikeCnt())
                .author(story.getMember())
                .isHidden(story.isHidden())
                .imgUrls(story.getImgUrls())
                .createDate(story.getCreatedDate())
                .build();
    }

    public boolean isAuthor(Member member) {
        return member.equals(this.author);
    }
}
