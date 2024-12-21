package com.junior.repository.story.custom;

import com.junior.domain.like.Like;
import com.junior.domain.like.QLike;
import com.junior.domain.member.Member;
import com.junior.domain.story.Story;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LikeCustomRepositoryImpl implements LikeCustomRepository {

    private final JPAQueryFactory query;

    public Like findLikeByMemberAndStory(Member member, Story story) {
        QLike like = QLike.like;

        return query.select(like)
                .from(like)
                .where(like.member.eq(member),
                        like.story.eq(story))
                .fetchOne();
    }
    @Override
    public void deleteLikeByStoryAndMember(Member member, Story story) {

        QLike like = QLike.like;

        query.delete(like)
                .where(like.member.eq(member),
                        like.story.eq(story))
                .execute();
    }
}
