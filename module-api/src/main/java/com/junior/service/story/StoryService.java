package com.junior.service.story;

import com.junior.domain.member.Member;
import com.junior.domain.story.Story;
import com.junior.dto.story.CreateStoryDto;
import com.junior.dto.story.GeoPointDto;
import com.junior.dto.story.ResponseStoryCntByCityDto;
import com.junior.dto.story.ResponseStoryDto;
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


    public Slice<ResponseStoryDto> findAllStories(Long cursorId, int size) {
        Pageable pageable = PageRequest.of(0, size);

        return storyRepository.findAllStories(cursorId, pageable);
    }

    public Slice<ResponseStoryDto> findStoriesByMemberAndCity(UserPrincipal userPrincipal, Long cursorId, int size, String city) {

        Member member = userPrincipal.getMember();

        Pageable pageable = PageRequest.of(0, size);

        return storyRepository.findStoriesByMemberAndCity(cursorId, pageable, city, member);
    }

    @Transactional
    public void editStory(UserPrincipal userPrincipal, Long storyId, CreateStoryDto createStoryDto) {

        Member findMember = userPrincipal.getMember();

        Story findStory = storyRepository.findStoryByIdAndMember(storyId, findMember);
        
        // 더티 체킹을 통해 수정쿼리가 자동으로 발생
        findStory.updateStory(createStoryDto);
    }

    public Slice<ResponseStoryDto> findStoriesByMemberAndMapWithPaging(UserPrincipal userPrincipal, Long cursorId, int size, GeoPointDto geoPointLt, GeoPointDto geoPointRb) {

        Member findMember = userPrincipal.getMember();

        Pageable pageable = PageRequest.of(0, size);

        return storyRepository.findStoriesByMemberAndMapWithPaging(cursorId, pageable, geoPointLt, geoPointRb, findMember);
    }

    public Slice<ResponseStoryDto> findStoriesByMemberAndCityAndSearch(UserPrincipal userPrincipal, Long cursorId, int size, String city, String search) {

        Member findMember = userPrincipal.getMember();

        Pageable pageable = PageRequest.of(0, size);

        return storyRepository.findStoriesByMemberAndCityAndSearch(cursorId, pageable, findMember, city, search);
    }

    public List<ResponseStoryCntByCityDto> getStoryCntByCity(UserPrincipal userPrincipal) {
        Member findMember = userPrincipal.getMember();

        return storyRepository.getStoryCntByCity(findMember);
    }

    public List<ResponseStoryDto> findStoriesByMemberAndMap(UserPrincipal userPrincipal, GeoPointDto geoPointLt, GeoPointDto geoPointRb) {
        Member findMember = userPrincipal.getMember();

        return storyRepository.findStoryByMap(findMember, geoPointLt, geoPointRb);
    }

    public ResponseStoryDto findStoryById(Long storyId) {
        Story findStory = storyRepository.findById(storyId)
                .orElseThrow(()->new StoryNotFoundException(StatusCode.STORY_NOT_FOUND));

        return ResponseStoryDto.from(findStory);
    }
}
