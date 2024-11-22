package com.junior.moduleapi.service.story;

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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@TestPropertySource(properties = "spring.profiles.active=local")
public class StoryServiceTest {

    @Autowired
    private StoryService storyService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private StoryRepository storyRepository;

    public Member createMember(String nickname) {
        return Member.builder()
                .nickname(nickname)
                .username("username")
                .password("password")
                .profileImage("profilePath")
                .role(MemberRole.USER)
                .status(MemberStatus.ACTIVE)
                .isAgreeTermsUse(true)
                .isAgreeMarketing(true)
                .isAgreeCollectingUsingPersonalInformation(true)
                .recommendLocation("DEAJEON")
                .signUpType(SignUpType.KAKAO)
                .build();
    }

    public Story createStory(Member member, String title, String city) {
        return Story.createStory()
                .title(title)
                .member(member)
                .content("content")
                .longitude(1.0)
                .latitude(1.0)
                .city(city)
                .isHidden(false)
                .thumbnailImg("thumbURL")
                .build();
    }

    @Test
    @Transactional
    public void clickLikeTest() {
        Member member = createMember("heeseong");
        memberRepository.save(member);

        Story story = createStory(member, "title", "city");
        storyRepository.save(story);

        UserPrincipal userPrincipal = new UserPrincipal(member);

        storyService.clickLike(userPrincipal, 1L);

        List<Like> likeMembers = story.getLikeMembers();

        Assertions.assertThat(likeMembers.size()).isEqualTo(1);
        Assertions.assertThat(likeMembers.get(0).getMember()).isEqualTo(member);

        storyService.clickLike(userPrincipal, 1L);
        Assertions.assertThat(likeMembers.size()).isEqualTo(0);
    }
}
