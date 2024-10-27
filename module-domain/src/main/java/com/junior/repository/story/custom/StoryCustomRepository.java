package com.junior.repository.story.custom;

import com.junior.domain.member.Member;
import com.junior.dto.story.ResponseStoryDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface StoryCustomRepository {
    public Slice<ResponseStoryDto> findAllStories(Long cursorId, Pageable pageable);

    public Slice<ResponseStoryDto> findStoriesByMemberAndCity(Long cursorId, Pageable pageable, String city, Member member);
}
