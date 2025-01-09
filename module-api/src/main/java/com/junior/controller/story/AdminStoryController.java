package com.junior.controller.story;

import com.junior.dto.story.AdminStoryDetailDto;
import com.junior.dto.story.AdminStoryDto;
import com.junior.dto.story.ResponseStoryListDto;
import com.junior.exception.StatusCode;
import com.junior.page.PageCustom;
import com.junior.response.CommonResponse;
import com.junior.service.story.AdminStoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AdminStoryController {

    private final AdminStoryService adminStoryService;

    @GetMapping("/api/v1/admin/stories")
    public CommonResponse<PageCustom<AdminStoryDto>> findStory(@PageableDefault(size = 15, page = 1) Pageable pageable, @RequestParam(required = false, name = "q", defaultValue = "") String q) {
        return CommonResponse.success(StatusCode.STORY_READ_SUCCESS, adminStoryService.findStory(pageable, q));
    }

    @GetMapping("/api/v1/admin/stories/{story_id}")
    public CommonResponse<AdminStoryDetailDto> findStoryDetail(@PathVariable(name = "story_id") Long storyId) {
        return CommonResponse.success(StatusCode.STORY_READ_SUCCESS, adminStoryService.findStoryDetail(storyId));
    }

}
