package com.junior.controller.story;

import com.junior.dto.story.ResponseStoryListDto;
import com.junior.exception.StatusCode;
import com.junior.response.CommonResponse;
import com.junior.service.story.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stories")
public class PublicStoryController {
    private final StoryService storyService;
    @GetMapping("/all")
    public CommonResponse<Object> getStories(
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam("size") int size,
            @RequestParam(value = "city", required = false) String city) {

        Slice<ResponseStoryListDto> allStories = storyService.findAllStories(cursorId, size, city);

        return CommonResponse.success(StatusCode.STORY_READ_SUCCESS, allStories);
    }
}
