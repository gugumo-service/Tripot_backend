package com.junior.service.story;

import com.junior.domain.story.Story;
import com.junior.dto.story.AdminStoryDetailDto;
import com.junior.dto.story.ResponseStoryListDto;
import com.junior.page.PageCustom;
import com.junior.repository.story.StoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class AdminStoryServiceTest {

    @Mock
    private StoryRepository storyRepository;

    @InjectMocks
    private AdminStoryService adminStoryService;

    @Test
    @DisplayName("관리자 전용 스토리 페이지 조회가 정상적으로 이루어져야 함")
    void findStory() {

        //given
        List<ResponseStoryListDto> storyDtos = new ArrayList<>();

        ResponseStoryListDto storyDto = ResponseStoryListDto.builder()
                .thumbnailImg("thumbnail")
                .storyId(1L)
                .title("title")
                .content("content")
                .longitude(-10.0)
                .latitude(10.0)
                .city("서울")
                .build();

        storyDtos.add(storyDto);


        PageRequest beforePageRequest = PageRequest.of(1, 15);
        PageRequest afterPageRequest = PageRequest.of(0, 15);

        given(storyRepository.findAllStories(any(Pageable.class), anyString())).willReturn(new PageImpl<>(storyDtos, afterPageRequest, storyDtos.size()));

        //when
        PageCustom<ResponseStoryListDto> result = adminStoryService.findStory(beforePageRequest, "");

        //then
        List<ResponseStoryListDto> content = result.getContent();

        assertThat(content.get(0).city()).isEqualTo("서울");

    }

    @Test
    @DisplayName("스토리 상세 조회 기능이 정상 동작해야 함")
    void findStoryDetail() {

        //given
        Story story = Story.builder()
                .id(1L)
                .title("title")
                .content("content")
                .thumbnailImg("thumbnail")
                .latitude(-10.0)
                .longitude(10.0)
                .city("서울")
                .likeCnt(3L)
                .imgUrls(new ArrayList<>())
                .build();

        given(storyRepository.findById(anyLong())).willReturn(Optional.ofNullable(story));

        //when
        AdminStoryDetailDto result = adminStoryService.findStoryDetail(1L);

        //then
        assertThat(result.city()).isEqualTo("서울");
        assertThat(result.likeCnt()).isEqualTo(3L);
    }
}