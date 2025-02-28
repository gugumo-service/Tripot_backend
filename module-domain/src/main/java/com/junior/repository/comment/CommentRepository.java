package com.junior.repository.comment;

import com.junior.domain.story.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentCustomRepository {

    public Page<Comment> findAllByOrderByIdDesc(Pageable pageable);

    public Long countByStoryIdAndIsDeletedFalse(Long storyId);
}
