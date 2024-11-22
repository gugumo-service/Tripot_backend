package com.junior.controller.story;

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

//    //NOTE: 스토리 생성  notion 명: 스토리 작성
//    @PostMapping("/new")
//    public CommonResponse<Object> save(
//            @AuthenticationPrincipal UserPrincipal userPrincipal,
//            @RequestBody CreateStoryDto createStoryDto) {
//
//        storyService.createStory(userPrincipal, createStoryDto);
//
//        return CommonResponse.success(StatusCode.STORY_CREATE_SUCCESS, null);
//    }

//    //NOTE: 스토리 단일 조회   notion 명: 스토리 조회(단일 스토리)
//    @GetMapping("/{storyId}")
//    public CommonResponse<Object> findStoryById(
//            @AuthenticationPrincipal UserPrincipal userPrincipal,
//            @PathVariable("storyId") Long storyId
//    ) {
//        ResponseStoryDto findStory = storyService.findStoryById(userPrincipal, storyId);
//
//        return CommonResponse.success(StatusCode.STORY_READ_SUCCESS, findStory);
//    }


//    //NOTE: 스토리 수정  notion 명: 스토리 수정
//    @PatchMapping("/{storyId}")
//    public CommonResponse<Object> edit(
//            @AuthenticationPrincipal UserPrincipal userPrincipal,
//            @PathVariable("storyId") Long storyId,
//            @RequestBody CreateStoryDto createStoryDto) {
//
//        storyService.editStory(userPrincipal, storyId, createStoryDto);
//
//        return CommonResponse.success(StatusCode.STORY_EDIT_SUCCESS, null);
//    }

    //NOTE: 전체 스토리 조회(option: 도시)  notion 명: 스토리 조회(둘러보기)
    //NOTE: /all/cursorId=1&size=3&city=daejeon
//    @GetMapping("/all")
//    public CommonResponse<Object> getStories(
//            @RequestParam(name = "cursorId", required = false) Long cursorId,
//            @RequestParam("size") int size,
//            @RequestParam(value = "city", required = false) String city) {
//
////        Slice<ResponseStoryDto> allStories = storyService.findAllStories_old(cursorId, size);
//        Slice<ResponseStoryDto> allStories = storyService.findAllStories(cursorId, size, city);
//
//        return CommonResponse.success(StatusCode.STORY_READ_SUCCESS, allStories);
//    }

//    //NOTE: 회원별 지도 기반 스토리 조회    notion 명: 스토리 조회(도시 별 조회) - 사용 x
////    @GetMapping("/listByCity")  // /list?city=~~&size=3&cursorId=1
//    @GetMapping("/list/map")  // /list?city=~~&size=3&cursorId=1
//    public CommonResponse<Object> getStoriesByMemberAndCity(
//            @AuthenticationPrincipal UserPrincipal userPrincipal,
//            @RequestParam(name = "cursorId", required = false) Long cursorId,
//            @RequestParam("size") int size,
//            @RequestParam("city") String city
//    ) {
//        Slice<ResponseStoryDto> storiesByCity = storyService.findStoriesByMemberAndCity(userPrincipal, cursorId, size, city);
//
//        return CommonResponse.success(StatusCode.STORY_READ_SUCCESS, storiesByCity);
//    }

//    //NOTE: 회원별 지도 기반 스토리 조회(페이징) notion 명: 스토리 조회(좌표 기준 조회, 페이징)
//    //NOTE: 사용 안하나?
////    @PostMapping("/listByMapWithPaging") // /list/map/size=3&cursorId=1
//    @PostMapping("/list/map") // /list/map/size=3&cursorId=1
//    public CommonResponse<Object> getStoriesByMemberAndMapWithPaging(
//            @AuthenticationPrincipal UserPrincipal userPrincipal,
//            @RequestBody GeoRect geoRect,
//            @RequestParam(name = "cursorId", required = false) Long cursorId,
//            @RequestParam(name = "size") int size
//    ) {
//        Slice<ResponseStoryDto> storiesByMap = storyService.findStoriesByMemberAndMapWithPaging(userPrincipal, cursorId, size, geoRect.geoPointLt(), geoRect.geoPointRb());
//
//        return CommonResponse.success(StatusCode.STORY_READ_SUCCESS, storiesByMap);
//    }

//    //NOTE: 회원별 검색어 기반 스토리 조회   notion 명: 스토리 조회(검색어 + 도시)
////    @PostMapping("/listByFiltering") // /listByFiltering?city=~~&search=~~&size=3&cursorId=1  getStoriesByMemberAndCity랑 합치기 가능할듯?
//    @PostMapping("/list") // /list?city=~~&search=~~&size=3&cursorId=1  getStoriesByMemberAndCity랑 합치기 가능할듯?
//    public CommonResponse<Object> getStoriesByMemberAndCityAndSearch(
//            @AuthenticationPrincipal UserPrincipal userPrincipal,
//            @RequestParam(name = "cursorId", required = false) Long cursorId,
//            @RequestParam("size") int size,
//            @RequestBody SearchAndCityDto searchAndCityDto
//    ) {
//        Slice<ResponseStoryDto> storiesByFiltering = storyService.findStoriesByMemberAndCityAndSearch(userPrincipal, cursorId, size, searchAndCityDto.city(), searchAndCityDto.search());
//
//        return CommonResponse.success(StatusCode.STORY_READ_SUCCESS, storiesByFiltering);
//    }

//    //NOTE: 회원별 지도 기반 스토리 조회(전체, 지도에 표시할 스토리들)  notion 명: 스토리 조회(좌표 기준 조회)
////    @PostMapping("/listByMap") // /list/map
//    @PostMapping("/list/map") // /list/map
//    public CommonResponse<Object> getStoriesByMemberAndMap(
//            @AuthenticationPrincipal UserPrincipal userPrincipal,
//            @RequestBody GeoRect geoRect
//    ) {
//        List<ResponseStoryDto> stories = storyService.findStoriesByMemberAndMap(userPrincipal, geoRect.geoPointLt(), geoRect.geoPointRb());
//
//        return CommonResponse.success(StatusCode.STORY_READ_SUCCESS, stories);
//    }

//    //NOTE: 회원 지역 별 스토리 수   notion 명: 도시 및 스토리 수 조회
//    @GetMapping("/countByCity")
//    public CommonResponse<Object> getStoryCntByCity(
//            @AuthenticationPrincipal UserPrincipal userPrincipal
//    ) {
//        List<ResponseStoryCntByCityDto> storyCntByCity = storyService.getStoryCntByCity(userPrincipal);
//
//        return CommonResponse.success(StatusCode.STORY_READ_SUCCESS, storyCntByCity);
//    }

//    //NOTE: 스토리 좋아요 기능  notion 명: 좋아요 기능
//    @GetMapping("/like/{storyId}")
//    public CommonResponse<Object> clickLike(
//            @AuthenticationPrincipal UserPrincipal userPrincipal,
//            @PathVariable("storyId") Long storyId
//    ) {
//
//        storyService.clickLike(userPrincipal, storyId);
//
//        return CommonResponse.success(StatusCode.LIKE_CHANGE_SUCCESS, null);
//    }
}
