package com.junior.repository.comment;

import com.junior.domain.member.Member;
import com.junior.dto.comment.ResponseChildCommentDto;
import com.junior.dto.comment.ResponseMyCommentDto;
import com.junior.dto.comment.ResponseParentCommentDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface CommentCustomRepository {

    public Slice<ResponseParentCommentDto> findParentCommentByStoryId(Member member, Long storyId, Pageable pageable, Long cursorId);
    public Slice<ResponseChildCommentDto> findChildCommentByParentCommendId(Member member, Long parentCommentId, Pageable pageable, Long cursorId);

    Slice<ResponseMyCommentDto> findCommentsByMember(Member findMember, Pageable pageable, Long cursorId);
}
