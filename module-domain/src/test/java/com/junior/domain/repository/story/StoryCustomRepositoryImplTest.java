package com.junior.domain.repository.story;

import com.junior.domain.TestConfig;
import com.junior.domain.member.Member;
import com.junior.domain.member.MemberRole;
import com.junior.domain.member.MemberStatus;
import com.junior.domain.member.SignUpType;
import com.junior.domain.story.Story;
import com.junior.dto.story.ResponseStoryDto;
import com.junior.repository.member.MemberRepository;
import com.junior.repository.story.StoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

@DataJpaTest
@Import(TestConfig.class)
class StoryCustomRepositoryImplTest {
    @Autowired
    StoryRepository storyRepository;
    @Autowired
    MemberRepository memberRepository;

    public Story createStory(Member member, String title, String city) {
        return Story.createStory()
                .title(title)
                .member(member)
                .content("content")
                .longitude(1.0)
                .latitude(1.0)
                .city(city)
                .isHidden(true)
                .thumbnailImg("thumbURL")
                .build();
    }

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

    @Test
    @DisplayName("Story를 저장할 수 있다.")
    public void saveStoryTest() {

        Member member = createMember("nickname");
        Story story = createStory(member, "title1", "DAEJEON");

        memberRepository.save(member);
        Assertions.assertThatCode(() -> storyRepository.save(story)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Story를 원하는 수만큼 조회할 수 있다.")
    public void findAllStoriesTest() {
        Member member = createMember("nickname");
        memberRepository.save(member);

        Story story1 = createStory(member, "title1","DAEJEON");
        Story story2 = createStory(member, "title2","DAEJEON");
        Story story3 = createStory(member, "title3","DAEJEON");
        Story story4 = createStory(member, "title4","DAEJEON");
        Story story5 = createStory(member, "title5","DAEJEON");

        storyRepository.save(story1);
        storyRepository.save(story2);
        storyRepository.save(story3);
        storyRepository.save(story4);
        storyRepository.save(story5);

        Pageable pageable1 = PageRequest.of(0, 3);

        Slice<ResponseStoryDto> allStories1 = storyRepository.findAllStories(null, pageable1);
        List<ResponseStoryDto> contents1 = allStories1.getContent();

        Assertions.assertThat(contents1.size()).isEqualTo(3);
        Assertions.assertThat(contents1.get(0).title()).isEqualTo("title5");
        Assertions.assertThat(contents1.get(1).title()).isEqualTo("title4");
        Assertions.assertThat(contents1.get(2).title()).isEqualTo("title3");

        Long lastId = contents1.get(2).id();

        Slice<ResponseStoryDto> stories2 = storyRepository.findAllStories(lastId, pageable1);
        List<ResponseStoryDto> contents2 = stories2.getContent();

        Assertions.assertThat(contents2.size()).isEqualTo(2);
        Assertions.assertThat(contents2.get(0).title()).isEqualTo("title2");
        Assertions.assertThat(contents2.get(1).title()).isEqualTo("title1");
    }

    @Test
    @DisplayName("회원과 도시를 필터링할 수 있다.")
    public void findStoriesByMemberAndCityTest() {
        Member member1 = createMember("nickname1");
        Member member2 = createMember("nickname2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        Story story1 = createStory(member1, "member1 title1","DAEJEON");
        Story story2 = createStory(member1, "member1 title2","DAEJEON");
        Story story3 = createStory(member1, "member1 title3","SEOUL");

        storyRepository.save(story1);
        storyRepository.save(story2);
        storyRepository.save(story3);

        Story story4 = createStory(member2, "member2 title4","SEOUL");
        Story story5 = createStory(member2, "member2 title5","SEOUL");
        Story story6 = createStory(member2, "member2 title6","DAEJEON");

        storyRepository.save(story4);
        storyRepository.save(story5);
        storyRepository.save(story6);


        Pageable pageable = PageRequest.of(0, 3);

        Slice<ResponseStoryDto> stories1 = storyRepository.findStoriesByMemberAndCity(null, pageable, "DAEJEON", member1);
        List<ResponseStoryDto> contents1 = stories1.getContent();

        Assertions.assertThat(contents1.size()).isEqualTo(2);
        Assertions.assertThat(contents1.get(0).title()).isEqualTo("member1 title2");
        Assertions.assertThat(contents1.get(1).title()).isEqualTo("member1 title1");

        Slice<ResponseStoryDto> stories2 = storyRepository.findStoriesByMemberAndCity(null, pageable, "SEOUL", member1);
        List<ResponseStoryDto> contents2 = stories2.getContent();

        Assertions.assertThat(contents2.size()).isEqualTo(1);
        Assertions.assertThat(contents2.get(0).title()).isEqualTo("member1 title3");
    }
}