package com.junior.domain.repository.story;

import com.junior.TestConfig;
import com.junior.domain.member.Member;
import com.junior.domain.member.MemberRole;
import com.junior.domain.member.MemberStatus;
import com.junior.domain.member.SignUpType;
import com.junior.domain.story.Story;
import com.junior.dto.story.CreateStoryDto;
import com.junior.dto.story.GeoPointDto;
import com.junior.dto.story.ResponseStoryCntByCityDto;
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
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

/*
    @DirtiesContext
    테스트가 끝날 때마다 context 를 초기화하는 어노테이션
    AFTER_EACH_TEST_METHOD : 테스트가 끝난 후 context 를 초기화
    BEFORE, AFTER 등 다양하게 있음
 */

@DataJpaTest
@Import(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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
                .isHidden(false)
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
    @DisplayName("Story를 저장할 때 아무런 예외가 발생하지 않는다.")
    public void saveStoryTest() {

        Member member = createMember("nickname");
        Story story = createStory(member, "title1", "DAEJEON");

        memberRepository.save(member);
        Assertions.assertThatCode(() -> storyRepository.save(story)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("story를 수정할 수 있다.")
    public void editStoryTest() {
        Member member = createMember("nickname");
        Story story = createStory(member, "title1", "DAEJEON");

        memberRepository.save(member);
        storyRepository.save(story);

        CreateStoryDto createStoryDto = CreateStoryDto.builder()
                .title("editStory")
                .content("editContent")
                .city("seoul")
                .thumbnailImg("editThumb")
                .latitude(1.0)
                .longitude(1.0)
                .isHidden(true)
                .build();

        Story findStory = storyRepository.findStoryByIdAndMember(1L, member);

        findStory.updateStory(createStoryDto);

        Story editStory = storyRepository.findStoryByIdAndMember(1L, member);

        Assertions.assertThat(story.getTitle()).isEqualTo("editStory");
        Assertions.assertThat(story.getContent()).isEqualTo("editContent");
        Assertions.assertThat(story.getCity()).isEqualTo("seoul");
    }

    @Test
    @DisplayName("Story를 원하는 수만큼 가져올 수 있으며, 최신 순으로 가져와야 한다.")
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
    @DisplayName("회원 별 특정 도시로 작성된 story를 최신 순으로 가져올 수 있다.")
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

    @Test
    @DisplayName("회원이 보고 있는 지도를 기준으로 그 회원이 작성한 story를 최신 순으로 pageable 하게 가져올 수 있다.")
    public void findStoriesByMemberAndMapWithPagingTest() {
        Member member = createMember("nickname1");
        memberRepository.save(member);

        GeoPointDto geoPointLt = GeoPointDto.builder()
                .latitude(0.0)
                .longitude(10.0)
                .build();

        GeoPointDto geoPointRb = GeoPointDto.builder()
                .latitude(10.0)
                .longitude(0.0)
                .build();

        Story inside1 = Story.createStory()
                .title("inside1")
                .member(member)
                .content("content")
                .longitude(5.0)
                .latitude(5.0)
                .city("city")
                .isHidden(true)
                .thumbnailImg("thumbURL")
                .build();

        Story inside2 = Story.createStory()
                .title("inside2")
                .member(member)
                .content("content")
                .longitude(5.0)
                .latitude(5.0)
                .city("city")
                .isHidden(true)
                .thumbnailImg("thumbURL")
                .build();

        Story outside1 = Story.createStory()
                .title("outside1")
                .member(member)
                .content("content")
                .longitude(-10.0)
                .latitude(-10.0)
                .city("city")
                .isHidden(true)
                .thumbnailImg("thumbURL")
                .build();

        Story outside2 = Story.createStory()
                .title("outside2")
                .member(member)
                .content("content")
                .longitude(20.0)
                .latitude(20.0)
                .city("city")
                .isHidden(true)
                .thumbnailImg("thumbURL")
                .build();

        storyRepository.save(inside1);
        storyRepository.save(inside2);
        storyRepository.save(outside1);
        storyRepository.save(outside2);

        Pageable pageable = PageRequest.of(0, 3);

        Slice<ResponseStoryDto> stories = storyRepository.findStoriesByMemberAndMapWithPaging(null, pageable, geoPointLt, geoPointRb, member);
        List<ResponseStoryDto> contents = stories.getContent();

        Assertions.assertThat(contents.size()).isEqualTo(2);
        Assertions.assertThat(contents.get(0).title()).isEqualTo("inside2");
        Assertions.assertThat(contents.get(1).title()).isEqualTo("inside1");

    }

    @Test
    @DisplayName("회원이 보고 있는 지도를 기준으로 그 회원이 작성한 story를 최신 순으로 가져올 수 있다.")
    public void findStoriesByMemberAndMap() {
        Member member = createMember("nickname1");
        memberRepository.save(member);

        GeoPointDto geoPointLt = GeoPointDto.builder()
                .latitude(0.0)
                .longitude(10.0)
                .build();

        GeoPointDto geoPointRb = GeoPointDto.builder()
                .latitude(10.0)
                .longitude(0.0)
                .build();

        Story inside1 = Story.createStory()
                .title("inside1")
                .member(member)
                .content("content")
                .longitude(5.0)
                .latitude(5.0)
                .city("city")
                .isHidden(true)
                .thumbnailImg("thumbURL")
                .build();

        Story inside2 = Story.createStory()
                .title("inside2")
                .member(member)
                .content("content")
                .longitude(5.0)
                .latitude(5.0)
                .city("city")
                .isHidden(true)
                .thumbnailImg("thumbURL")
                .build();

        Story outside1 = Story.createStory()
                .title("outside1")
                .member(member)
                .content("content")
                .longitude(-10.0)
                .latitude(-10.0)
                .city("city")
                .isHidden(true)
                .thumbnailImg("thumbURL")
                .build();

        Story outside2 = Story.createStory()
                .title("outside2")
                .member(member)
                .content("content")
                .longitude(20.0)
                .latitude(20.0)
                .city("city")
                .isHidden(true)
                .thumbnailImg("thumbURL")
                .build();

        storyRepository.save(inside1);
        storyRepository.save(inside2);
        storyRepository.save(outside1);
        storyRepository.save(outside2);

        List<ResponseStoryDto> stories = storyRepository.findStoryByMap(member, geoPointLt, geoPointRb);

        Assertions.assertThat(stories.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("회원이 지역과 검색어를 기반으로 스토리를 조회할 수 있다.")
    public void findStoriesByMemberAndCityAndSearch() {
        Member member = createMember("nickname1");
        memberRepository.save(member);

        Story hasSearchStory = Story.createStory()
                .title("filterTitle")
                .member(member)
                .content("filterContent")
                .longitude(-10.0)
                .latitude(-10.0)
                .city("city")
                .isHidden(true)
                .thumbnailImg("thumbURL")
                .build();

        Story hasNotSearchStory = Story.createStory()
                .title("title")
                .member(member)
                .content("content")
                .longitude(-10.0)
                .latitude(-10.0)
                .city("city")
                .isHidden(true)
                .thumbnailImg("thumbURL")
                .build();

        storyRepository.save(hasSearchStory);
        storyRepository.save(hasNotSearchStory);

        Pageable pageable = PageRequest.of(0, 3);

        Slice<ResponseStoryDto> stories = storyRepository.findStoriesByMemberAndCityAndSearch(null, pageable, member, null, "filter");
        List<ResponseStoryDto> contents = stories.getContent();

        Assertions.assertThat(contents.size()).isEqualTo(1);
        Assertions.assertThat(contents.get(0).title()).isEqualTo("filterTitle");
    }

    @Test
    @DisplayName("지역별 스토리 수를 사전 순으로 정렬해서 얻을 수 있다.")
    public void getStoryCntByCityTest() {
        Member member = createMember("nickname");
        memberRepository.save(member);

        Story story1 = Story.createStory()
                .title("filterTitle")
                .member(member)
                .content("filterContent")
                .longitude(-10.0)
                .latitude(-10.0)
                .city("서울")
                .isHidden(true)
                .thumbnailImg("thumbURL")
                .build();

        Story story2 = Story.createStory()
                .title("filterTitle")
                .member(member)
                .content("filterContent")
                .longitude(-10.0)
                .latitude(-10.0)
                .city("대전")
                .isHidden(true)
                .thumbnailImg("thumbURL")
                .build();

        Story story3 = Story.createStory()
                .title("filterTitle")
                .member(member)
                .content("filterContent")
                .longitude(-10.0)
                .latitude(-10.0)
                .city("대구")
                .isHidden(true)
                .thumbnailImg("thumbURL")
                .build();

        Story story4 = Story.createStory()
                .title("filterTitle")
                .member(member)
                .content("filterContent")
                .longitude(-10.0)
                .latitude(-10.0)
                .city("부산")
                .isHidden(true)
                .thumbnailImg("thumbURL")
                .build();

        Story story5 = Story.createStory()
                .title("filterTitle")
                .member(member)
                .content("filterContent")
                .longitude(-10.0)
                .latitude(-10.0)
                .city("서울")
                .isHidden(true)
                .thumbnailImg("thumbURL")
                .build();

        Story story6 = Story.createStory()
                .title("filterTitle")
                .member(member)
                .content("filterContent")
                .longitude(-10.0)
                .latitude(-10.0)
                .city("대전")
                .isHidden(true)
                .thumbnailImg("thumbURL")
                .build();

        storyRepository.save(story1);
        storyRepository.save(story2);
        storyRepository.save(story3);
        storyRepository.save(story4);
        storyRepository.save(story5);
        storyRepository.save(story6);

        List<ResponseStoryCntByCityDto> storyCntByCity = storyRepository.getStoryCntByCity(member);

        System.out.println("storyCntByCity = " + storyCntByCity);

        Assertions.assertThat(storyCntByCity.size()).isEqualTo(4);

        Assertions.assertThat(storyCntByCity.get(0).city()).isEqualTo("대구");
        Assertions.assertThat(storyCntByCity.get(0).cnt()).isEqualTo(1);

        Assertions.assertThat(storyCntByCity.get(1).city()).isEqualTo("대전");
        Assertions.assertThat(storyCntByCity.get(1).cnt()).isEqualTo(2);

        Assertions.assertThat(storyCntByCity.get(2).city()).isEqualTo("부산");
        Assertions.assertThat(storyCntByCity.get(2).cnt()).isEqualTo(1);

        Assertions.assertThat(storyCntByCity.get(3).city()).isEqualTo("서울");
        Assertions.assertThat(storyCntByCity.get(3).cnt()).isEqualTo(2);
    }
}