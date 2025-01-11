package com.junior.service.story;

import com.junior.domain.like.Like;
import com.junior.domain.member.Member;
import com.junior.domain.notification.NotificationType;
import com.junior.domain.story.Story;
import com.junior.dto.story.*;
import com.junior.exception.StatusCode;
import com.junior.exception.StoryNotFoundException;
import com.junior.repository.story.LikeRepository;
import com.junior.repository.story.StoryRepository;
import com.junior.security.UserPrincipal;
import com.junior.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberStoryService {
    private final StoryRepository storyRepository;
    private final LikeRepository likeRepository;

    private final NotificationService notificationService;

    @Transactional
    public void createStory(UserPrincipal userPrincipal, CreateStoryDto createStoryDto) {

        Member member = userPrincipal.getMember();

        Story story = Story.from(member, createStoryDto);

        storyRepository.save(story);
    }

    @Transactional
    public void editStory(UserPrincipal userPrincipal, Long storyId, CreateStoryDto createStoryDto) {

        Member findMember = userPrincipal.getMember();

        Story findStory = storyRepository.findStoryByIdAndMember(storyId, findMember)
                .orElseThrow(() -> new StoryNotFoundException(StatusCode.STORY_NOT_FOUND));

        // 더티 체킹을 통해 수정쿼리가 자동으로 발생
        findStory.updateStory(createStoryDto);
    }

    // findStoriesByMemberAndCityAndSearch
    public Slice<ResponseStoryListDto> findStoriesByFilter(UserPrincipal userPrincipal, Long cursorId, int size, String city, String search) {

        Member findMember = userPrincipal.getMember();

        Pageable pageable = PageRequest.of(0, size);

        return storyRepository.findStoriesByMemberAndCityAndSearch(cursorId, pageable, findMember, city, search);
    }

    public List<ResponseStoryCntByCityDto> getStoryCntByCity(UserPrincipal userPrincipal) {
        Member findMember = userPrincipal.getMember();

        List<ResponseStoryCntByCityDto> storyCntByCity = storyRepository.getStoryCntByCity(findMember);

        int totalStoryCnt = storyCntByCity.stream()
                .mapToInt(ResponseStoryCntByCityDto::cnt)
                .sum();

        ResponseStoryCntByCityDto allCityDto = new ResponseStoryCntByCityDto("all", totalStoryCnt);

        storyCntByCity.add(allCityDto);

        return storyCntByCity;
    }

    public List<ResponseStoryListDto> findStoriesByMap(UserPrincipal userPrincipal, GeoPointDto geoPointLt, GeoPointDto geoPointRb) {
        Member findMember = userPrincipal.getMember();

        return storyRepository.findStoryByMap(findMember, geoPointLt, geoPointRb);
    }

    @Transactional
    public ResponseStoryDto findStoryById(UserPrincipal userPrincipal, Long storyId) {
        Member findMember = userPrincipal.getMember();

        Story findStory = storyRepository.findById(storyId)
                .orElseThrow(()->new StoryNotFoundException(StatusCode.STORY_NOT_FOUND));

        Boolean isLikeStory = likeRepository.isLikeStory(findMember, findStory);

        boolean isNotAuthor = findStory.isHidden() && !findStory.getMember().getId().equals(findMember.getId());

        if(isNotAuthor) {
            throw new StoryNotFoundException(StatusCode.STORY_NOT_PERMISSION);
        }

        findStory.increaseViewCnt();

        return ResponseStoryDto.from(findStory, isLikeStory);
    }

    @Transactional
    public void clickLike(UserPrincipal userPrincipal, Long storyId) {
        Member findMember = userPrincipal.getMember();

        Story findStory = storyRepository.findById(storyId)
                .orElseThrow(() -> new StoryNotFoundException(StatusCode.STORY_NOT_FOUND));

        // is clicked like?
        Boolean isLiked = storyRepository.isLikedMember(findMember, findStory);

        if(!isLiked) {
            Like like = Like.builder()
                    .member(findMember)
                    .story(findStory)
                    .build();

            likeRepository.save(like);

            findStory.increaseLikeCnt();

            //알림 저장
            notificationService.saveNotification(userPrincipal, findMember.getProfileImage(), findStory.getTitle(), findStory.getId(), NotificationType.LIKED);
        }
        else {
            Like findLike = likeRepository.findLikeByMemberAndStory(findMember, findStory);
            likeRepository.delete(findLike);

            findStory.decreaseLikeCnt();
        }
    }

    public Slice<ResponseStoryListDto> getLikeStories(UserPrincipal userPrincipal, Long cursorId, int size) {
        Member findMember = userPrincipal.getMember();

        Pageable pageable = PageRequest.of(0, size);

        return storyRepository.findLikeStories(findMember, pageable, cursorId);
    }
}