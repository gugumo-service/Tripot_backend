package com.junior.controller.story;

import com.junior.dto.story.*;
import com.junior.exception.StatusCode;
import com.junior.response.CommonResponse;
import com.junior.security.UserPrincipal;
import com.junior.service.story.MemberStoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stories")
public class MemberStoryController {

    private final MemberStoryService memberStoryService;

    //NOTE: 스토리 생성  notion 명: 스토리 작성
    @PostMapping("/new")
    public CommonResponse<Object> save(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CreateStoryDto createStoryDto) {

        memberStoryService.createStory(userPrincipal, createStoryDto);

        return CommonResponse.success(StatusCode.STORY_CREATE_SUCCESS, null);
    }

    //NOTE: 스토리 단일 조회   notion 명: 스토리 조회(단일 스토리)
    @GetMapping("/{storyId}")
    public CommonResponse<Object> findStoryById(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("storyId") Long storyId
    ) {

        ResponseStoryDto findStory = memberStoryService.findOneStory(userPrincipal, storyId);

        return CommonResponse.success(StatusCode.STORY_READ_SUCCESS, findStory);
    }

    @DeleteMapping("/{storyId}")
    public CommonResponse<Object> deleteStory(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("storyId") Long storyId
    ) {
        memberStoryService.deleteStory(userPrincipal, storyId);

        return CommonResponse.success(StatusCode.STORY_DELETE_SUCCESS, null);
    }

    //NOTE: 회원별 검색어 기반 스토리 조회   notion 명: 스토리 조회(검색어 + 도시)
    @GetMapping("/search")
    public CommonResponse<Object> getStoriesByMemberAndCityAndSearch(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam("size") int size,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "city", required = false) String city
    ) {

        Slice<ResponseStoryListDto> storiesByFilter = memberStoryService.findStoriesByFilter(userPrincipal, cursorId, size, city, search);

        return CommonResponse.success(StatusCode.STORY_READ_SUCCESS, storiesByFilter);
    }

    //NOTE: 회원별 지도 기반 스토리 조회(전체, 지도에 표시할 스토리들)  notion 명: 스토리 조회(좌표 기준 조회)
    //@PostMapping("/listByMap") // /list/map
    @PostMapping("/map")
    public CommonResponse<Object> getStoriesByMemberAndMap(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody GeoRect geoRect
    ) {

        List<ResponseStoryListDto> storiesByMap = memberStoryService.findStoriesByMap(userPrincipal, geoRect.geoPointLt(), geoRect.geoPointRb());

        return CommonResponse.success(StatusCode.STORY_READ_SUCCESS, storiesByMap);
    }

    //NOTE: 스토리 수정  notion 명: 스토리 수정
    @PatchMapping("/{storyId}")
    public CommonResponse<Object> edit(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("storyId") Long storyId,
            @RequestBody CreateStoryDto createStoryDto) {

        memberStoryService.editStory(userPrincipal, storyId, createStoryDto);

        return CommonResponse.success(StatusCode.STORY_EDIT_SUCCESS, null);
    }

    //NOTE: 회원 지역 별 스토리 수   notion 명: 도시 및 스토리 수 조회
    @GetMapping("/count")
    public CommonResponse<Object> getStoryCntByCity(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        List<ResponseStoryCntByCityDto> storyCntByCity = memberStoryService.getStoryCntByCity(userPrincipal);

        return CommonResponse.success(StatusCode.STORY_READ_SUCCESS, storyCntByCity);
    }

    //NOTE: 스토리 좋아요 기능  notion 명: 좋아요 기능
    @GetMapping("/{storyId}/like")
    public CommonResponse<Object> clickLike(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("storyId") Long storyId
    ) {

        memberStoryService.clickLike(userPrincipal, storyId);

        return CommonResponse.success(StatusCode.LIKE_CHANGE_SUCCESS, null);
    }

    //NOTE: 좋아요 스토리 리스트  notion 명: 좋아요 누른 스토리 조회
    @GetMapping("/like-list")
    public CommonResponse<Object> getLikeStories(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam("size") int size
    ) {
        Slice<ResponseStoryListDto> stories = memberStoryService.getLikeStories(userPrincipal, cursorId, size);

        return CommonResponse.success(StatusCode.STORY_READ_SUCCESS, stories);
    }
}
