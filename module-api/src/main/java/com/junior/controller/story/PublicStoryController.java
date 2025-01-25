package com.junior.controller.story;

import com.junior.dto.story.ResponseStoryListDto;
import com.junior.exception.StatusCode;
import com.junior.response.CommonResponse;
import com.junior.security.UserPrincipal;
import com.junior.service.story.PublicStoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/public/stories")
public class PublicStoryController {

    private final PublicStoryService publicStoryService;

    @GetMapping("/search")
    public CommonResponse<Object> getStories(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam("size") int size,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "search", required = false) String search) {

        Slice<ResponseStoryListDto> allStories = publicStoryService.findStoriesByFilter(userPrincipal, cursorId, size, city, search);

        return CommonResponse.success(StatusCode.STORY_READ_SUCCESS, allStories);
    }

    @GetMapping("/recommended/random")
    public CommonResponse<Object> recommendStories(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        String recommendedRandomCity = publicStoryService.getRecommendedRandomCity();

        return CommonResponse.success(StatusCode.RECOMMENDED_CITY_SUCCESS, recommendedRandomCity);
    }

    @GetMapping("/recommended/recent-popular-story")
    public CommonResponse<Object> recentPopularStories(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam("size") int size
    ) {

        Slice<ResponseStoryListDto> recentPopularStories = publicStoryService.getRecentPopularStories(userPrincipal, cursorId, size);

        return CommonResponse.success(StatusCode.RECOMMENDED_STORIES_SUCCESS, recentPopularStories);
    }

    @GetMapping("/recommended/recent-popular-city")
    public CommonResponse<Object> recentMostCityStories(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        String recommendedRecentPopular = publicStoryService.getRecommendedRecentPopularCity();

        return CommonResponse.success(StatusCode.RECOMMENDED_CITY_SUCCESS, recommendedRecentPopular);
    }
}
