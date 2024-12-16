package com.junior.service.story;

import com.junior.domain.like.Like;
import com.junior.domain.member.Member;
import com.junior.domain.story.Story;
import com.junior.dto.story.*;
import com.junior.exception.LikeNotFoundException;
import com.junior.exception.StatusCode;
import com.junior.exception.StoryNotFoundException;
import com.junior.repository.story.StoryRepository;
import com.junior.security.UserPrincipal;
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
public class StoryService {

    private final StoryRepository storyRepository;

    @Transactional
    public void createStory(UserPrincipal userPrincipal, CreateStoryDto createStoryDto) {

        Member member = userPrincipal.getMember();

        Story story = Story.from(member, createStoryDto);

        storyRepository.save(story);
    }


//    public Slice<ResponseStoryDto> findAllStories_old(Long cursorId, int size) {
//        Pageable pageable = PageRequest.of(0, size);
//
//        return storyRepository.findAllStories_old(cursorId, pageable);
//    }

    public Slice<ResponseStoryListDto> findAllStories(Long cursorId, int size, String city) {

        Pageable pageable = PageRequest.of(0, size);

        return storyRepository.findAllStories(cursorId, pageable, city);
    }

//    public Slice<ResponseStoryDto> findStoriesByMemberAndCity(UserPrincipal userPrincipal, Long cursorId, int size, String city) {
//
//        Member member = userPrincipal.getMember();
//
//        Pageable pageable = PageRequest.of(0, size);
//
//        return storyRepository.findStoriesByMemberAndCity(cursorId, pageable, city, member);
//    }

    @Transactional
    public void editStory(UserPrincipal userPrincipal, Long storyId, CreateStoryDto createStoryDto) {

        Member findMember = userPrincipal.getMember();

        Story findStory = storyRepository.findStoryByIdAndMember(storyId, findMember);
        
        // 더티 체킹을 통해 수정쿼리가 자동으로 발생
        findStory.updateStory(createStoryDto);
    }

//    public Slice<ResponseStoryDto> findStoriesByMemberAndMapWithPaging(UserPrincipal userPrincipal, Long cursorId, int size, GeoPointDto geoPointLt, GeoPointDto geoPointRb) {
//
//        Member findMember = userPrincipal.getMember();
//
//        Pageable pageable = PageRequest.of(0, size);
//
//        return storyRepository.findStoriesByMemberAndMapWithPaging(cursorId, pageable, geoPointLt, geoPointRb, findMember);
//    }

    public Slice<ResponseStoryListDto> findStoriesByMemberAndCityAndSearch(UserPrincipal userPrincipal, Long cursorId, int size, String city, String search) {

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

    public List<ResponseStoryListDto> findStoriesByMemberAndMap(UserPrincipal userPrincipal, GeoPointDto geoPointLt, GeoPointDto geoPointRb) {
        Member findMember = userPrincipal.getMember();

        return storyRepository.findStoryByMap(findMember, geoPointLt, geoPointRb);
    }

    @Transactional
    public ResponseStoryDto findStoryById(UserPrincipal userPrincipal, Long storyId) {
        Member findMember = userPrincipal.getMember();

        Story findStory = storyRepository.findById(storyId)
                .orElseThrow(()->new StoryNotFoundException(StatusCode.STORY_NOT_FOUND));


        boolean isNotAuthor = findStory.isHidden() && findStory.isAuthor(findMember);

        if(isNotAuthor) {
            throw new StoryNotFoundException(StatusCode.STORY_NOT_PERMISSION);
        }

//        if(findStory.isHidden()) {
//            if(!findStory.getMember().getId().equals(findMember.getId())) {
//                throw new StoryNotFoundException(StatusCode.STORY_NOT_PERMISSION);
//            }
//        }

        boolean isLikedUser = findStory.getLikeMembers().stream()
                        .anyMatch(like -> like.getMember().getId().equals(findMember.getId()));

        findStory.increaseViewCnt();

        return ResponseStoryDto.from(findStory, isLikedUser);
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

            findStory.addLikeMember(like);
        }
        else {
            List<Like> likeMembers = findStory.getLikeMembers();

            Like existingLike = likeMembers.stream()
                    .filter(like -> like.getMember().equals(findMember))
                    .findFirst()
                    .orElseThrow(() -> new LikeNotFoundException(StatusCode.LIKE_NOT_FOUND));

            findStory.removeLikeMember(existingLike);
        }
    }

    public Slice<ResponseStoryListDto> getLikeStories(UserPrincipal userPrincipal, Long cursorId, int size) {
        Member findMember = userPrincipal.getMember();

        Pageable pageable = PageRequest.of(0, size);

        return storyRepository.findLikeStories(findMember, pageable, cursorId);
    }
}
