package com.junior.integration;

import com.junior.config.SecurityConfig;
import com.junior.domain.member.Member;
import com.junior.domain.member.MemberRole;
import com.junior.domain.member.MemberStatus;
import com.junior.domain.member.SignUpType;
import com.junior.domain.story.Comment;
import com.junior.domain.story.Story;
import com.junior.dto.story.CreateStoryDto;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.profiles.active=local")
@Transactional
@Import(SecurityConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class IntegrationControllerTest {

    Member createPreactiveTestMember() {
        return Member.builder()
                .id(1L)
                .nickname("테스트비활성화닉네임")
                .username("테스트비활성화유저네임")
                .role(MemberRole.USER)
                .signUpType(SignUpType.KAKAO)
                .profileImage("s3.com/testProfile")
                .recommendLocation("서울")
                .build();
    }

    Member createActiveTestMember() {
        return Member.builder()
                .id(2L)
                .nickname("테스트사용자닉네임")
                .username("테스트사용자유저네임")
                .role(MemberRole.USER)
                .signUpType(SignUpType.KAKAO)
                .profileImage("s3.com/testProfile")
                .recommendLocation("서울")
                .status(MemberStatus.ACTIVE)
                .build();
    }

    MockMultipartFile createMockMultipartFile() {
        MockMultipartFile profileImg = new MockMultipartFile(
                "profileimg",
                "profiles.png",
                MediaType.IMAGE_PNG_VALUE,
                "thumbnail".getBytes()
        );

        return profileImg;

    }

    Story createStory(Member member) {
        List<String> imgUrls = new ArrayList<>();
        imgUrls.add("imgUrl1");
        imgUrls.add("imgUrl2");
        imgUrls.add("imgUrl3");

        return Story.builder()
                .id(1L)
                .title("testStoryTitle")
                .member(member)
                .content("testStoryContent")
                .longitude(1.0)
                .latitude(1.0)
                .city("city")
                .isHidden(false)
                .thumbnailImg("thumbURL")
                .imgUrls(imgUrls)
                .build();
    }


    Comment createComment(Member member, Story story) {

        return Comment.builder()
                .id(1L)
                .member(member)
                .content("content")
                .story(story)
                .build();
    }
}
