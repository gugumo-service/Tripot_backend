package com.junior.controller;

import com.junior.dto.comment.CreateCommentDto;
import com.junior.dto.comment.ResponseChildCommentDto;
import com.junior.dto.comment.ResponseParentCommentDto;
import com.junior.exception.StatusCode;
import com.junior.response.CommonResponse;
import com.junior.security.UserPrincipal;
import com.junior.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/save")
    public CommonResponse<Object> save(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CreateCommentDto createCommentDto
            ) {
        commentService.saveComment(userPrincipal, createCommentDto);

        return CommonResponse.success(StatusCode.COMMENT_CREATE_SUCCESS, null);
    }

    @PatchMapping("/{commentId}")
    public CommonResponse<Object> edit(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("commentId") Long commentId,
            @RequestBody String content
    ) {
        commentService.editComment(userPrincipal, commentId, content);

        return CommonResponse.success(StatusCode.COMMENT_EDIT_SUCCESS, null);
    }

    @DeleteMapping("/{commentId}")
    public CommonResponse<Object> delete(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("commentId") Long commentId
    ) {
        commentService.deleteComment(userPrincipal, commentId);

        return CommonResponse.success(StatusCode.COMMENT_DELETE_SUCCESS, null);
    }

    @GetMapping("/{storyId}/parent")
    public CommonResponse<Object> findParentComment(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam("size") int size,
            @PathVariable("storyId") Long storyId
    ) {
        Slice<ResponseParentCommentDto> parentCommentDto = commentService.findParentCommentByStoryId(storyId, cursorId, size);

        return CommonResponse.success(StatusCode.COMMENT_READ_SUCCESS, parentCommentDto);
    }

    @GetMapping("/{parentCommentId}/child")
    public CommonResponse<Object> findChildComment(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam("size") int size,
            @PathVariable("parentCommentId") Long parentCommentId
    ) {
        Slice<ResponseChildCommentDto> childCommentDto = commentService.findChildCommentByParentCommentId(parentCommentId, cursorId, size);

        return CommonResponse.success(StatusCode.COMMENT_READ_SUCCESS, childCommentDto);
    }
}
