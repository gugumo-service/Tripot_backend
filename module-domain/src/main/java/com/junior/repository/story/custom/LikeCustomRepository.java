package com.junior.repository.story.custom;

import com.junior.domain.like.Like;
import com.junior.domain.member.Member;
import com.junior.domain.story.Story;

public interface LikeCustomRepository {
    public Like findLikeByMemberAndStory(Member member, Story story);
    void deleteLikeByStoryAndMember(Member member , Story story);

    Boolean isLikeStory(Member findMember, Story findStory);
}
