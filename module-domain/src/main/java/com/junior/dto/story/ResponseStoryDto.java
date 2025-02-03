package com.junior.dto.story;

import com.junior.domain.story.Story;
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
        Long likeCnt,
        boolean isHidden,
        boolean isAuthor,
        boolean isLikeStory,
        LocalDateTime createDate,
        List<String> imgUrls
) {

//    @QueryProjection
//    public ResponseStoryDto(Long id, String title, String content, String thumbnailImg, double latitude, double longitude, String city, Long likeCnt, boolean isHidden, LocalDateTime createDate, List<String> imgUrls) {
//        this.id = id;
//        this.title = title;
//        this.content = content;
//        this.thumbnailImg = thumbnailImg;
//        this.latitude = latitude;
//        this.longitude = longitude;
//        this.city = city;
//        this.likeCnt = likeCnt;
//        this.isHidden = isHidden;
//        this.createDate = createDate;
//        this.imgUrls = imgUrls;
//    }

    public static ResponseStoryDto from(Story story, boolean isLikeStory, boolean isAuthor) {
        return ResponseStoryDto.builder()
                .id(story.getId())
                .title(story.getTitle())
                .content(story.getContent())
                .thumbnailImg(story.getThumbnailImg())
                .latitude(story.getLatitude())
                .longitude(story.getLongitude())
                .city(story.getCity())
                .likeCnt(story.getLikeCnt())
                .isHidden(story.isHidden())
                .isLikeStory(isLikeStory)
                .imgUrls(story.getImgUrls())
                .createDate(story.getCreatedDate())
                .isAuthor(isAuthor)
                .build();
    }

    public static ResponseStoryDto from(Story story) {
        return ResponseStoryDto.builder()
                .id(story.getId())
                .title(story.getTitle())
                .content(story.getContent())
                .thumbnailImg(story.getThumbnailImg())
                .latitude(story.getLatitude())
                .longitude(story.getLongitude())
                .city(story.getCity())
                .likeCnt(story.getLikeCnt())
                .isHidden(story.isHidden())
//                .isLikeStory(isLikeStory)
                .imgUrls(story.getImgUrls())
                .createDate(story.getCreatedDate())
                .build();
    }
}
