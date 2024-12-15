package com.junior.repository.comment;

import com.junior.dto.comment.ResponseChildCommentDto;
import com.junior.dto.comment.ResponseParentCommentDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface CommentCustomRepository {

    public Slice<ResponseParentCommentDto> findParentCommentByStoryId(Long storyId, Pageable pageable, Long cursorId);
    public Slice<ResponseChildCommentDto> findChildCommentByParentCommendId(Long parentCommentId, Pageable pageable, Long cursorId);
}
