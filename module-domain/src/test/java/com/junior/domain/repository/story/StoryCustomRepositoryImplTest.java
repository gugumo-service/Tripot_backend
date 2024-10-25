package com.junior.domain.repository.story;

import com.junior.config.QueryDslConfig;
import com.junior.domain.story.Story;
import com.junior.dto.story.ResponseStoryDto;
import com.junior.repository.story.StoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@DataJpaTest
@Import(QueryDslConfig.class)
class StoryCustomRepositoryImplTest {
    @Autowired
    StoryRepository storyRepository;

    public Story createStory() {
        return Story.createStory()
                .title("title")
                .content("content")
                .longitude(1.0)
                .latitude(1.0)
                .city("DAEJEON")
                .isHidden(true)
                .thumbnailImg("thumbURL")
                .build();
    }

    @Test
    @DisplayName("Story를 저장할 수 있다.")
    public void saveStoryTest() {
        Story story = createStory();

        Assertions.assertThatCode(() -> storyRepository.save(story)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("title로 story를 찾을 수 있다.")
    public void findStoryByTitleTest() {
        Story story = createStory();

        storyRepository.save(story);

        List<ResponseStoryDto> stories = storyRepository.findStoryByTitleDevTest("title");

        Assertions.assertThat(stories.get(0).getTitle()).isEqualTo("title");
    }
}