package com.junior.controller;

import com.junior.domain.story.Story;
import com.junior.dto.story.*;
import com.junior.exception.StatusCode;
import com.junior.response.CommonResponse;
import com.junior.security.UserPrincipal;
import com.junior.service.story.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{storyId}")
    public CommonResponse<Object> findStoryById(
            @PathVariable("storyId") Long storyId
    ) {
        ResponseStoryDto findStory = storyService.findStoryById(storyId);

        if(findStory != null) {
            return CommonResponse.success(StatusCode.STORY_READ_SUCCESS, findStory);
        }
        else {
            return CommonResponse.fail(StatusCode.STORY_NOT_FOUND);
        }
    }

    @PatchMapping("/{storyId}")
    public CommonResponse<Object> edit(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("storyId") Long storyId,
            @RequestBody CreateStoryDto createStoryDto) {

        storyService.editStory(userPrincipal, storyId, createStoryDto);

        return CommonResponse.success(StatusCode.STORY_EDIT_SUCCESS, null);
    }

    @GetMapping("/all")
    public CommonResponse<Object> getStories(
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam("size") int size) {

        Slice<ResponseStoryDto> allStories = storyService.findAllStories(cursorId, size);

        return CommonResponse.success(StatusCode.STORY_READ_SUCCESS, allStories);
    }

    @GetMapping("/listByCity")
    public CommonResponse<Object> getStoriesByMemberAndCity(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam("size") int size,
            @RequestParam("city") String city
    ) {
        Slice<ResponseStoryDto> storiesByCity = storyService.findStoriesByMemberAndCity(userPrincipal, cursorId, size, city);

        return CommonResponse.success(StatusCode.STORY_READ_SUCCESS, storiesByCity);
    }

    @PostMapping("/listByFiltering")
    public CommonResponse<Object> getStoriesByMemberAndCityAndSearch(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam("size") int size,
            @RequestBody SearchAndCityDto searchAndCityDto
    ) {
        Slice<ResponseStoryDto> storiesByFiltering = storyService.findStoriesByMemberAndCityAndSearch(userPrincipal, cursorId, size, searchAndCityDto.city(), searchAndCityDto.search());

        return CommonResponse.success(StatusCode.STORY_READ_SUCCESS, storiesByFiltering);
    }

    @PostMapping("/listByMapWithPaging")
    public CommonResponse<Object> getStoriesByMemberAndMapWithPaging(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody GeoRect geoRect,
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam(name = "size") int size
    ) {
        Slice<ResponseStoryDto> storiesByMap = storyService.findStoriesByMemberAndMapWithPaging(userPrincipal, cursorId, size, geoRect.geoPointLt(), geoRect.geoPointRb());

        return CommonResponse.success(StatusCode.STORY_READ_SUCCESS, storiesByMap);
    }

    @PostMapping("/listByMap")
    public CommonResponse<Object> getStoriesByMemberAndMap(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody GeoRect geoRect
    ) {
        List<ResponseStoryDto> stories = storyService.findStoriesByMemberAndMap(userPrincipal, geoRect.geoPointLt(), geoRect.geoPointRb());

        return CommonResponse.success(StatusCode.STORY_READ_SUCCESS, stories);
    }

    @GetMapping("/countByCity")
    public CommonResponse<Object> getStoryCntByCity(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        List<ResponseStoryCntByCityDto> storyCntByCity = storyService.getStoryCntByCity(userPrincipal);

        return CommonResponse.success(StatusCode.STORY_READ_SUCCESS, storyCntByCity);
    }
}
