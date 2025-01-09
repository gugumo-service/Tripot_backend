package com.junior.controller;

import com.junior.dto.comment.CommentAdminDto;
import com.junior.exception.StatusCode;
import com.junior.page.PageCustom;
import com.junior.response.CommonResponse;
import com.junior.service.comment.CommentAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentAdminController {

    private final CommentAdminService commentAdminService;

    @GetMapping("/api/v1/admin/comments")
    public CommonResponse<PageCustom<CommentAdminDto>> findComment(@PageableDefault(size = 15, page = 1) Pageable pageable) {

        return CommonResponse.success(StatusCode.COMMENT_READ_SUCCESS, commentAdminService.findComment(pageable));
    }

    @DeleteMapping("/api/v1/admin/comments/{comment_id}")
    public CommonResponse<PageCustom<CommentAdminDto>> findComment(@PathVariable(name = "comment_id") Long commentId) {
        commentAdminService.deleteComment(commentId);

        return CommonResponse.success(StatusCode.COMMENT_DELETE_SUCCESS, null);
    }



}
