package com.junior.controller;

import com.junior.dto.story.CreateStoryDto;
import com.junior.dto.story.ResponseStoryDto;
import com.junior.exception.StatusCode;
import com.junior.response.CommonResponse;
import com.junior.service.story.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/story")
public class StoryController {

    private final StoryService storyService;

    @PostMapping("/new")
    public CommonResponse save(@RequestBody CreateStoryDto createStoryDto) {

        storyService.createStory(createStoryDto);

        return CommonResponse.builder()
                .customCode(StatusCode.STORY_CREATE_SUCCESS.getCustomCode())
                .customMessage(StatusCode.STORY_CREATE_SUCCESS.getCustomMessage())
                .data(null)
                .build();
    }

    @GetMapping("/list")
    public CommonResponse<Object> getStories(
            @RequestParam Long cursorId,
            @RequestParam int size) {

//        Page<ResponseStoryDto> stories = storyService.getStories(sort, category, page, size);
        Slice<ResponseStoryDto> allStories = storyService.findAllStories(cursorId, size);
        return CommonResponse.builder()
                .customCode("200")
                .customMessage("성공")
                .data(allStories)
                .build();
    }
}
