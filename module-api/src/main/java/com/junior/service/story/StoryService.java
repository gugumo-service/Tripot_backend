package com.junior.service.story;

import com.junior.domain.member.Member;
import com.junior.dto.story.GeoPointDto;
import com.junior.repository.story.StoryRepository;
import com.junior.domain.story.Story;
import com.junior.dto.story.CreateStoryDto;
import com.junior.dto.story.ResponseStoryDto;
import com.junior.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoryService {

    private final StoryRepository storyRepository;

    @Transactional
    public void createStory(UserPrincipal userPrincipal, CreateStoryDto createStoryDto) {

        Member member = userPrincipal.getMember();

        Story story = Story.createStory()
                .member(member)
                .title(createStoryDto.title())
                .content(createStoryDto.content())
                .city(createStoryDto.city())
                .thumbnailImg(createStoryDto.thumbnailImg())
                .latitude(createStoryDto.latitude())
                .longitude(createStoryDto.longitude())
                .isHidden(createStoryDto.isHidden())
                .build();

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

    public Slice<ResponseStoryDto> findStoriesByMemberAndMap(UserPrincipal userPrincipal, Long cursorId, int size, GeoPointDto geoPointLt, GeoPointDto geoPointRb) {

        Member findMember = userPrincipal.getMember();

        Pageable pageable = PageRequest.of(0, size);

        return storyRepository.findStoriesByMemberAndMap(cursorId, pageable, geoPointLt, geoPointRb, findMember);
    }
}
