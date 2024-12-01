package com.junior.moduleapi.service.story;

import com.junior.ModuleApiApplication;
import com.junior.config.YamlPropertySourceFactory;
import com.junior.domain.like.Like;
import com.junior.domain.member.Member;
import com.junior.domain.member.MemberRole;
import com.junior.domain.member.MemberStatus;
import com.junior.domain.member.SignUpType;
import com.junior.domain.story.Story;
import com.junior.repository.member.MemberRepository;
import com.junior.repository.story.StoryRepository;
import com.junior.security.UserPrincipal;
import com.junior.service.story.StoryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest(classes = ModuleApiApplication.class,
        properties = "spring.config.location=classpath:/application-api-local.yml")
public class StoryServiceTest {

    @Autowired
    private StoryService storyService;

    @MockBean
    private StoryRepository storyRepository;

    @Test
    @DisplayName("test")
    public void test() {

    }
}
