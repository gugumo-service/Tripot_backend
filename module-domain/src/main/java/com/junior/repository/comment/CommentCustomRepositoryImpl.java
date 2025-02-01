package com.junior.repository.comment;

import com.junior.domain.member.Member;
import com.junior.dto.comment.ResponseChildCommentDto;
import com.junior.dto.comment.ResponseMyCommentDto;
import com.junior.dto.comment.ResponseParentCommentDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.junior.domain.story.QComment.comment;
import static com.junior.domain.story.QStory.story;

@RequiredArgsConstructor
public class CommentCustomRepositoryImpl implements CommentCustomRepository {

    private final JPAQueryFactory query;

    private<T> boolean isHaveNextStoryList(List<T> comments, Pageable pageable) {

        boolean hasNext;

        if(comments.size() == pageable.getPageSize() + 1) {
            comments.remove(pageable.getPageSize());
            hasNext = true;
        }
        else {
            hasNext = false;
        }

        return hasNext;
    }

    private BooleanExpression eqCursorId(Long cursorId) {
        if(cursorId != null) {
            return comment.id.lt(cursorId);
        }
        return null;
    }

    @Override
    public Slice<ResponseParentCommentDto> findParentCommentByStoryId(Member member, Long storyId, Pageable pageable, Long cursorId) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        booleanBuilder.and(comment.story.id.eq(storyId));
        booleanBuilder.and(comment.parent.isNull());
        booleanBuilder.and(comment.isDeleted.eq(false));
        booleanBuilder.and(eqCursorId(cursorId));

        List<ResponseParentCommentDto> comments = query.select(
                        Projections.constructor(
                                ResponseParentCommentDto.class,
                                comment.id,
                                comment.content,
                                comment.member.id,
                                comment.member.nickname,
                                comment.member.profileImage,
                                comment.child.size().longValue(),
                                comment.createdDate,
                                comment.member.eq(member)
                        )
                )
                .from(comment)
                .where(booleanBuilder)
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = isHaveNextStoryList(comments, pageable);

        return new SliceImpl<>(comments, pageable, hasNext);
    }

    @Override
    public Slice<ResponseChildCommentDto> findChildCommentByParentCommendId(Member member, Long parentCommentId, Pageable pageable, Long cursorId) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(comment.parent.id.eq(parentCommentId));
        booleanBuilder.and(comment.isDeleted.eq(false));
        booleanBuilder.and(eqCursorId(cursorId));

        List<ResponseChildCommentDto> comments = query.select(
                        Projections.constructor(
                                ResponseChildCommentDto.class,
                                comment.id,
                                comment.content,
                                comment.member.id,
                                comment.member.nickname,
                                comment.member.profileImage,
                                comment.createdDate,
                                comment.member.eq(member)
                        )
                )
                .from(comment)
                .where(booleanBuilder)
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = isHaveNextStoryList(comments, pageable);

        return new SliceImpl<>(comments, pageable, hasNext);
    }

    @Override
    public Slice<ResponseMyCommentDto> findCommentsByMember(Member findMember, Pageable pageable, Long cursorId) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(comment.member.eq(findMember));
        booleanBuilder.and(comment.isDeleted.eq(false));
        booleanBuilder.and(eqCursorId(cursorId));

        List<ResponseMyCommentDto> comments = query.select(
                        Projections.constructor(
                                ResponseMyCommentDto.class,
                                comment.id,
                                comment.story.id,
                                comment.content,
                                comment.createdDate,
                                story.title
                        )
                )
                .from(comment)
                .join(comment.story, story)
                .where(booleanBuilder)
                .orderBy(comment.createdDate.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = isHaveNextStoryList(comments, pageable);

        return new SliceImpl<>(comments, pageable, hasNext);
    }
}
