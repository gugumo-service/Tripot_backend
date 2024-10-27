package com.junior.controller;

import com.junior.dto.story.CreateStoryDto;
import com.junior.dto.story.ResponseStoryDto;
import com.junior.exception.StatusCode;
import com.junior.response.CommonResponse;
import com.junior.security.UserPrincipal;
import com.junior.service.story.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/story")
public class StoryController {

    private final StoryService storyService;

    @PostMapping("/new")
    public CommonResponse<Object> save(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CreateStoryDto createStoryDto) {

        storyService.createStory(userPrincipal, createStoryDto);

        return CommonResponse.success(StatusCode.STORY_CREATE_SUCCESS, null);
    }

    @GetMapping("/list")
    public CommonResponse<Object> getStories(
            @RequestParam(required = false) Long cursorId,
            @RequestParam int size) {

        Slice<ResponseStoryDto> allStories = storyService.findAllStories(cursorId, size);

        return CommonResponse.success(StatusCode.STORY_READ_SUCCESS, allStories);
    }

    @GetMapping("/listByCity")
    public CommonResponse<Object> getStoriesByMemberSortedByCity(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(required = false) Long cursorId,
            @RequestParam int size,
            @RequestParam String city
    ) {
        Slice<ResponseStoryDto> storiesByCity = storyService.findStoriesByMemberAndCity(userPrincipal, cursorId, size, city);

        return CommonResponse.success(StatusCode.STORY_READ_SUCCESS, storiesByCity);
    }
}
