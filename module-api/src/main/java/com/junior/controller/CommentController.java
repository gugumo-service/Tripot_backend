package com.junior.controller;

import com.junior.dto.comment.*;
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
            @RequestBody UpdateCommentDto content
    ) {
        commentService.editComment(userPrincipal, commentId, content.content());

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
        Slice<ResponseParentCommentDto> parentCommentDto = commentService.findParentCommentByStoryId(userPrincipal, storyId, cursorId, size);

        return CommonResponse.success(StatusCode.COMMENT_READ_SUCCESS, parentCommentDto);
    }

    @GetMapping("/{parentCommentId}/child")
    public CommonResponse<Object> findChildComment(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam("size") int size,
            @PathVariable("parentCommentId") Long parentCommentId
    ) {
        Slice<ResponseChildCommentDto> childCommentDto = commentService.findChildCommentByParentCommentId(userPrincipal, parentCommentId, cursorId, size);

        return CommonResponse.success(StatusCode.COMMENT_READ_SUCCESS, childCommentDto);
    }

    @GetMapping("/my")
    public CommonResponse<Object> findMyComment(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam("size") int size
    ) {

        Slice<ResponseMyCommentDto> commentsDto = commentService.findMyCommentByMember(userPrincipal, cursorId, size);

        return CommonResponse.success(StatusCode.COMMENT_READ_SUCCESS, commentsDto);
    }

    @GetMapping("/cnt/{storyId}")
    public CommonResponse<Object> findCommentCntByStoryId(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable(name = "storyId") Long storyId
    ) {
        Long commentCntByStoryId = commentService.findCommentCntByStoryId(userPrincipal, storyId);

        return CommonResponse.success(StatusCode.COMMENT_READ_SUCCESS, commentCntByStoryId);
    }
}
