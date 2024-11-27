package com.junior.repository.comment;

import com.junior.domain.story.Comment;

import java.util.List;

public interface CommentCustomRepository {

    public List<Comment> findCommentByStoryId(Long storyId);
}
