package com.junior.service.story;

import com.junior.domain.repository.story.StoryRepository;
import com.junior.domain.story.Story;
import com.junior.domain.story.dto.CreateStoryDto;
import com.junior.domain.story.dto.ResponseStoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoryService {

    private final StoryRepository storyRepository;

    @Transactional
    public void createStory(CreateStoryDto createStoryDto) {
        Story story = Story.createStory()
                .title(createStoryDto.getTitle())
                .content(createStoryDto.getContent())
                .city(createStoryDto.getCity())
                .thumbnailImg(createStoryDto.getThumbnailImg())
                .latitude(createStoryDto.getLatitude())
                .longitude(createStoryDto.getLongitude())
                .isHidden(createStoryDto.isHidden())
                .build();

        storyRepository.save(story);
    }

    public Page<ResponseStoryDto> getStories(String sort, String category, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return storyRepository.findAllStories(sort, category, pageable);
    }

    public Slice<ResponseStoryDto> findAllStories(Long cursorId, int size) {
        Pageable pageable = PageRequest.of(0, size);

        return storyRepository.findAllStories(cursorId, pageable);
    }
}
