package com.junior.service.story;

import com.junior.domain.member.Member;
import com.junior.domain.story.Story;
import com.junior.dto.story.AdminStoryDetailDto;
import com.junior.dto.story.ResponseStoryDto;
import com.junior.dto.story.ResponseStoryListDto;
import com.junior.exception.StatusCode;
import com.junior.exception.StoryNotFoundException;
import com.junior.page.PageCustom;
import com.junior.repository.story.StoryRepository;
import com.junior.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminStoryService {

    private final StoryRepository storyRepository;

    public PageCustom<ResponseStoryListDto> findStory(Pageable pageable, String keyword) {

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize());

        Page<ResponseStoryListDto> result = storyRepository.findAllStories(pageRequest, keyword);

        log.info("[{}] 관리자 스토리 조회 page: {}", Thread.currentThread().getStackTrace()[1].getMethodName(), pageable.getPageNumber());

        return new PageCustom<>(result.getContent(), result.getPageable(), result.getTotalElements());

    }


    public AdminStoryDetailDto findStoryDetail(Long storyId) {

        Story findStory = storyRepository.findById(storyId)
                .orElseThrow(()->new StoryNotFoundException(StatusCode.STORY_NOT_FOUND));

        log.info("[{}] 관리자 스토리 상세 조회", Thread.currentThread().getStackTrace()[1].getMethodName());

        return AdminStoryDetailDto.from(findStory);
    }



}
