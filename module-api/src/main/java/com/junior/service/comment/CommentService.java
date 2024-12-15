package com.junior.service.comment;

import com.junior.domain.member.Member;
import com.junior.domain.story.Comment;
import com.junior.domain.story.Story;
import com.junior.dto.comment.CreateCommentDto;
import com.junior.dto.comment.ResponseParentCommentDto;
import com.junior.exception.CommentNotFoundException;
import com.junior.exception.StatusCode;
import com.junior.exception.StoryNotFoundException;
import com.junior.repository.comment.CommentRepository;
import com.junior.repository.story.StoryRepository;
import com.junior.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final StoryRepository storyRepository;

    @Transactional
    public void saveComment(UserPrincipal userPrincipal, CreateCommentDto createCommentDto) {

        Member findMember = userPrincipal.getMember();

        Story findStory = storyRepository.findById(createCommentDto.storyId())
                .orElseThrow(()->new StoryNotFoundException(StatusCode.STORY_NOT_FOUND));

        Comment comment = Comment.builder()
                .member(findMember)
                .story(findStory)
                .content(createCommentDto.content())
                .build();

        // 부모 댓글 추가
        if(createCommentDto.parentCommentId() != null) {
            Comment parentComment = commentRepository.findById(createCommentDto.parentCommentId())
                    .orElseThrow(() -> new CommentNotFoundException(StatusCode.COMMENT_NOT_FOUND));

            comment.updateParent(parentComment);
        }

        commentRepository.save(comment);
    }

    public Slice<ResponseParentCommentDto> findParentCommentByStoryId(Long storyId, Long cursorId, int size) {

        Pageable pageable = PageRequest.of(0, size);

        return commentRepository.findParentCommentByStoryId(storyId, pageable, cursorId);
    }

    public List<Comment> findChildCommentByCommentStoryId(Long commentId) {

        return null;
    }
}
