package com.junior.domain.repository.story.custom;

import com.junior.domain.story.dto.ResponseStoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface StoryCustomRepository {
    public List<ResponseStoryDto> findStoryByTitleDevTest(String title);
    public Page<ResponseStoryDto> findAllStories(String sort, String category, Pageable pageable);

    public Slice<ResponseStoryDto> findAllStories(Long cursorId, Pageable pageable);
}
