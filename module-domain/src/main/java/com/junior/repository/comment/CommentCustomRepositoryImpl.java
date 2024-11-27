package com.junior.repository.comment;

import com.junior.domain.story.Comment;
import com.junior.domain.story.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CommentCustomRepositoryImpl implements CommentCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public List<Comment> findCommentByStoryId(Long storyId) {
        QComment qComment = new QComment(QComment.comment);

        return query.select(qComment)
                .leftJoin(qComment.parent)
                .fetchJoin()
                .where(qComment.story.id.eq(storyId))
                .orderBy(qComment.parent.id.asc().nullsLast())
                .fetch();

    }
}
