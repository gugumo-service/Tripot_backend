package com.junior.integration;

import com.junior.config.SecurityConfig;
import com.junior.domain.member.Member;
import com.junior.domain.member.MemberRole;
import com.junior.domain.member.MemberStatus;
import com.junior.domain.member.SignUpType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.profiles.active=prod")
@Transactional
@Import(SecurityConfig.class)
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
}
