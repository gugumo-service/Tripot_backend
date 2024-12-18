package com.junior.service.report;

import com.junior.domain.member.Member;
import com.junior.domain.member.MemberRole;
import com.junior.domain.member.MemberStatus;
import com.junior.domain.member.SignUpType;
import com.junior.domain.report.Report;
import com.junior.domain.story.Comment;
import com.junior.domain.story.Story;
import com.junior.dto.report.CreateReportDto;
import com.junior.exception.ReportException;
import com.junior.repository.comment.CommentRepository;
import com.junior.repository.member.MemberRepository;
import com.junior.repository.report.ReportRepository;
import com.junior.repository.story.StoryRepository;
import com.junior.security.UserPrincipal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {


    @Mock
    private StoryRepository storyRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private ReportService reportService;

    @Test
    @DisplayName("스토리 신고 기능이 정상 작동해야 함")
    void save_report_story() {

        //given
        CreateReportDto createReportDto = CreateReportDto.builder()
                .reportContentId(1L)
                .reportType("story")
                .reportReason("샘플")
                .build();

        Member testActiveMember = createActiveTestMember();
        Story testStory = createStory(testActiveMember, "title", "city");

        UserPrincipal principal = new UserPrincipal(testActiveMember);

        given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(testActiveMember));
        given(storyRepository.findById(anyLong())).willReturn(Optional.ofNullable(testStory));

        //when
        reportService.save(createReportDto, principal);

        //then

        verify(reportRepository).save(any(Report.class));
    }

    @Test
    @DisplayName("댓글 신고 기능이 정상 작동해야 함")
    void save_report_comment() {

        //given
        CreateReportDto createReportDto = CreateReportDto.builder()
                .reportContentId(1L)
                .reportType("comment")
                .reportReason("샘플")
                .build();

        Member testActiveMember = createActiveTestMember();
        Story testStory = createStory(testActiveMember, "title", "city");
        Comment testComment = createComment(testActiveMember, testStory);

        UserPrincipal principal = new UserPrincipal(testActiveMember);

        given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(testActiveMember));
        given(commentRepository.findById(anyLong())).willReturn(Optional.ofNullable(testComment));


        //when
        reportService.save(createReportDto, principal);

        //then

        verify(reportRepository).save(any(Report.class));
    }


    @Test
    @DisplayName("유효하지 않은 신고 유형에 대한 예외 처리가 진행되어야 함")
    void save_report_type_not_valid() {

        //given
        CreateReportDto createReportDto = CreateReportDto.builder()
                .reportContentId(1L)
                .reportType("notvalid")
                .reportReason("샘플")
                .build();

        Member testActiveMember = createActiveTestMember();


        UserPrincipal principal = new UserPrincipal(testActiveMember);

        given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(testActiveMember));



        //when, then
        assertThatThrownBy(() -> reportService.save(createReportDto, principal))
                .isInstanceOf(ReportException.class)
                .hasMessageContaining("유효한 신고 유형이 아님");
    }


    @Test
    @DisplayName("유효하지 않은 신고 사유에 대한 예외 처리가 진행되어야 함")
    void save_report_reason_not_valid() {

        //given
        CreateReportDto createReportDto = CreateReportDto.builder()
                .reportContentId(1L)
                .reportType("notvalid")
                .reportReason("샘플이아님")
                .build();

        Member testActiveMember = createActiveTestMember();


        UserPrincipal principal = new UserPrincipal(testActiveMember);

        given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(testActiveMember));



        //when, then
        assertThatThrownBy(() -> reportService.save(createReportDto, principal))
                .isInstanceOf(ReportException.class)
                .hasMessageContaining("유효한 신고 유형이 아님");
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

    Story createStory(Member member, String title, String city) {
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

    Comment createComment(Member member, Story story) {

        return Comment.builder()
                .member(member)
                .content("content")
                .story(story)
                .build();
    }


}