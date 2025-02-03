package com.junior.moduleapi.service.story;

import com.junior.ModuleApiApplication;
import com.junior.repository.story.StoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = ModuleApiApplication.class,
        properties = "spring.config.location=classpath:/application-api-local.yml")
public class StoryServiceTest {


    @MockBean
    private StoryRepository storyRepository;

    @Test
    @DisplayName("test")
    public void test() {

    }
}
