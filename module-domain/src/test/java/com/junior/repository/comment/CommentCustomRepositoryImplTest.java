package com.junior.repository.comment;

import com.junior.TestConfig;
import com.junior.domain.member.Member;
import com.junior.domain.member.MemberRole;
import com.junior.domain.member.MemberStatus;
import com.junior.domain.member.SignUpType;
import com.junior.domain.story.Comment;
import com.junior.domain.story.Story;
import com.junior.dto.comment.ResponseChildCommentDto;
import com.junior.dto.comment.ResponseMyCommentDto;
import com.junior.dto.comment.ResponseParentCommentDto;
import com.junior.dto.story.CreateStoryDto;
import com.junior.repository.member.MemberRepository;
import com.junior.repository.story.StoryRepository;
import org.assertj.core.api.Assert;
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

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@Import(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CommentCustomRepositoryImplTest {

    @Autowired
    StoryRepository storyRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CommentRepository commentRepository;

    public Story createStory(Member member, String title, String city) {

        List<String> imgUrls = new ArrayList<>();
        imgUrls.add("imgUrl1");
        imgUrls.add("imgUrl2");
        imgUrls.add("imgUrl3");

        CreateStoryDto createStoryDto = CreateStoryDto.builder()
                .title(title)
                .content("content")
                .longitude(1.0)
                .latitude(1.0)
                .city(city)
                .isHidden(false)
                .thumbnailImg("thumbURL")
                .imgUrls(imgUrls)
                .build();

        return Story.from(member, createStoryDto);
    }

    @Test
    @DisplayName("comment를 저장할 수 있다.")
    public void saveTest() {
        Member testMember = Member.builder().nickname("테스트닉")
                .username("KAKAO 3748293466")
                .role(MemberRole.USER)
                .status(MemberStatus.ACTIVE)
                .signUpType(SignUpType.KAKAO)
                .recommendLocation("서울")
                .build();
        memberRepository.save(testMember);

        Story story = createStory(testMember, "title", "city");
        storyRepository.save(story);

        Comment parentComment1 = Comment.builder()
                .parent(null)
                .content("content")
                .member(testMember)
                .story(story)
                .isDeleted(false)
                .build();

        Assertions.assertThatCode(()->commentRepository.save(parentComment1)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("parentComment를 slice로 조회할 수 있다.")
    public void findParentCommentByStoryIdTest() {
        Member testMember = Member.builder().nickname("테스트닉")
                .username("KAKAO 3748293466")
                .role(MemberRole.USER)
                .status(MemberStatus.ACTIVE)
                .signUpType(SignUpType.KAKAO)
                .recommendLocation("서울")
                .build();
        memberRepository.save(testMember);

        Story story = createStory(testMember, "title", "city");
        storyRepository.save(story);

        Comment parentComment1 = Comment.builder()
                .parent(null)
                .content("content1")
                .member(testMember)
                .story(story)
                .isDeleted(false)
                .build();
        Comment parentComment2 = Comment.builder()
                .parent(null)
                .content("content2")
                .member(testMember)
                .story(story)
                .isDeleted(false)
                .build();
        Comment parentComment3 = Comment.builder()
                .parent(null)
                .content("content3")
                .member(testMember)
                .story(story)
                .isDeleted(false)
                .build();

        Comment childComment1 = Comment.builder()
                .parent(parentComment3)
                .content("child1")
                .member(testMember)
                .story(story)
                .isDeleted(false)
                .build();
        childComment1.updateParent(parentComment3);

        commentRepository.save(parentComment1);
        commentRepository.save(parentComment2);
        commentRepository.save(parentComment3);
        commentRepository.save(childComment1);

        Pageable pageable = PageRequest.of(0, 5);

        Slice<ResponseParentCommentDto> parentCommentByStoryId = commentRepository.findParentCommentByStoryId(story.getId(), pageable, null);
        List<ResponseParentCommentDto> content = parentCommentByStoryId.getContent();

        Assertions.assertThat(content.size()).isEqualTo(3);
        Assertions.assertThat(content.get(2).childCount()).isEqualTo(1L);
    }

    @Test
    @DisplayName("childComment를 slice로 조회할 수 있다.")
    public void findChildCommentByParentCommendIdTest() {
        Member testMember = Member.builder().nickname("테스트닉")
                .username("KAKAO 3748293466")
                .role(MemberRole.USER)
                .status(MemberStatus.ACTIVE)
                .signUpType(SignUpType.KAKAO)
                .recommendLocation("서울")
                .build();
        memberRepository.save(testMember);

        Story story = createStory(testMember, "title", "city");
        storyRepository.save(story);

        Comment parentComment3 = Comment.builder()
                .parent(null)
                .content("content3")
                .member(testMember)
                .story(story)
                .isDeleted(false)
                .build();

        Comment childComment1 = Comment.builder()
                .parent(parentComment3)
                .content("child1")
                .member(testMember)
                .story(story)
                .isDeleted(false)
                .build();

        Comment childComment2 = Comment.builder()
                .parent(parentComment3)
                .content("child2")
                .member(testMember)
                .story(story)
                .isDeleted(false)
                .build();

        Comment childComment3 = Comment.builder()
                .parent(parentComment3)
                .content("child3")
                .member(testMember)
                .story(story)
                .isDeleted(false)
                .build();

        childComment1.updateParent(parentComment3);
        childComment2.updateParent(parentComment3);
        childComment3.updateParent(parentComment3);

        commentRepository.save(parentComment3);
        commentRepository.save(childComment1);
        commentRepository.save(childComment2);
        commentRepository.save(childComment3);

        Pageable pageable = PageRequest.of(0, 5);

        Slice<ResponseChildCommentDto> parentCommentByStoryId = commentRepository.findChildCommentByParentCommendId(parentComment3.getId(), pageable, null);
        List<ResponseChildCommentDto> content = parentCommentByStoryId.getContent();

        Assertions.assertThat(content.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("내가 쓴 댓글을 조회할 수 있다.")
    public void findCommentsByMemberTest() {
        Member testMember = Member.builder().nickname("테스트닉")
                .username("KAKAO 3748293466")
                .role(MemberRole.USER)
                .status(MemberStatus.ACTIVE)
                .signUpType(SignUpType.KAKAO)
                .recommendLocation("서울")
                .build();
        memberRepository.save(testMember);

        Story story = createStory(testMember, "title", "city");
        storyRepository.save(story);

        Comment parentComment3 = Comment.builder()
                .parent(null)
                .content("content3")
                .member(testMember)
                .story(story)
                .isDeleted(false)
                .build();

        Comment childComment1 = Comment.builder()
                .parent(parentComment3)
                .content("child1")
                .member(testMember)
                .story(story)
                .isDeleted(false)
                .build();

        Comment childComment2 = Comment.builder()
                .parent(parentComment3)
                .content("child2")
                .member(testMember)
                .story(story)
                .isDeleted(false)
                .build();

        Comment childComment3 = Comment.builder()
                .parent(parentComment3)
                .content("child3")
                .member(testMember)
                .story(story)
                .isDeleted(false)
                .build();

        childComment1.updateParent(parentComment3);
        childComment2.updateParent(parentComment3);
        childComment3.updateParent(parentComment3);

        commentRepository.save(parentComment3);
        commentRepository.save(childComment1);
        commentRepository.save(childComment2);
        commentRepository.save(childComment3);

        Pageable pageable = PageRequest.of(0, 5);

        Slice<ResponseMyCommentDto> commentsByMember = commentRepository.findCommentsByMember(testMember, pageable, null);
        List<ResponseMyCommentDto> content = commentsByMember.getContent();
        Assertions.assertThat(content.size()).isEqualTo(4);
    }
}
