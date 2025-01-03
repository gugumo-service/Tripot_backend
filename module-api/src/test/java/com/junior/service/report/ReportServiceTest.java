package com.junior.service.report;

import com.junior.domain.member.Member;
import com.junior.domain.member.MemberRole;
import com.junior.domain.member.MemberStatus;
import com.junior.domain.member.SignUpType;
import com.junior.domain.report.Report;
import com.junior.domain.report.ReportReason;
import com.junior.domain.report.ReportStatus;
import com.junior.domain.report.ReportType;
import com.junior.domain.story.Comment;
import com.junior.domain.story.Story;
import com.junior.dto.notice.NoticeAdminDto;
import com.junior.dto.report.CreateReportDto;
import com.junior.dto.report.ReportDto;
import com.junior.dto.report.ReportQueryDto;
import com.junior.dto.report.StoryReportDto;
import com.junior.exception.ReportException;
import com.junior.page.PageCustom;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
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
                .reportReason("스팸홍보")
                .build();

        Member testWriter = createActiveTestMember();
        Member testReporter = createActiveTestMember2();
        Story testStory = createStory(testWriter, "title", "city");

        UserPrincipal principal = new UserPrincipal(testReporter);

        given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(testReporter));
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
                .reportReason("스팸홍보")
                .build();

        Member testWriter = createActiveTestMember();
        Member testReporter = createActiveTestMember2();
        Story testStory = createStory(testWriter, "title", "city");
        Comment testComment = createComment(testWriter, testStory);


        UserPrincipal principal = new UserPrincipal(testReporter);

        given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(testReporter));
        given(commentRepository.findById(anyLong())).willReturn(Optional.ofNullable(testComment));


        //when
        reportService.save(createReportDto, principal);

        //then

        verify(reportRepository).save(any(Report.class));
    }

    @Test
    @DisplayName("본인 글은 신고가 불가능해야 함")
    void save_report_report_equal_author() {

        //given
        CreateReportDto createReportDto = CreateReportDto.builder()
                .reportContentId(1L)
                .reportType("comment")
                .reportReason("스팸홍보")
                .build();

        Member testWriter = createActiveTestMember();
        Member testWriter2 = createActiveTestMember();

        Story testStory = createStory(testWriter, "title", "city");
        Comment testComment = createComment(testWriter, testStory);


        UserPrincipal principal = new UserPrincipal(testWriter);

        given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(testWriter));
        given(commentRepository.findById(anyLong())).willReturn(Optional.ofNullable(testComment));


        //when, then
        assertThatThrownBy(() -> reportService.save(createReportDto, principal))
                .isInstanceOf(ReportException.class)
                .hasMessageContaining("본인 글은 신고할 수 없음");
    }


    @Test
    @DisplayName("중복신고가 불가능해야함")
    void save_report_report_duplicate() {

        //given
        CreateReportDto createReportDto = CreateReportDto.builder()
                .reportContentId(1L)
                .reportType("comment")
                .reportReason("스팸홍보")
                .build();

        Member testWriter = createActiveTestMember();
        Member testReporter = createActiveTestMember2();
        Story testStory = createStory(testWriter, "title", "city");
        Comment testComment = createComment(testWriter, testStory);


        UserPrincipal principal = new UserPrincipal(testReporter);

        given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(testReporter));
        given(commentRepository.findById(anyLong())).willReturn(Optional.ofNullable(testComment));
        given(reportRepository.existsByMemberAndComment(any(Member.class), any(Comment.class))).willReturn(true);


        //when, then
        assertThatThrownBy(() -> reportService.save(createReportDto, principal))
                .isInstanceOf(ReportException.class)
                .hasMessageContaining("중복신고할 수 없음");
    }


    @Test
    @DisplayName("유효하지 않은 신고 유형에 대한 예외 처리가 진행되어야 함")
    void save_report_type_not_valid() {

        //given
        CreateReportDto createReportDto = CreateReportDto.builder()
                .reportContentId(1L)
                .reportType("notvalid")
                .reportReason("스팸홍보")
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

    @Test
    @DisplayName("신고 조회 기능이 정상 동작해야 함")
    public void find_report_success() throws Exception {
        //given

        String reportStatus = "CONFIRMED";

        //전체 신고내역 생성
        List<ReportQueryDto> queryDtoList = new ArrayList<>();
        List<ReportDto> dtoList = new ArrayList<>();


        queryDtoList.add(ReportQueryDto.builder()
                .id(1L)
                .reportType(ReportType.STORY)
                .reportReason(ReportReason.SPAMMARKET)
                .reporterUsername("username")
                .reportStatus(ReportStatus.CONFIRMED)
                .build());

        dtoList.add(StoryReportDto.builder()
                .id(1L)
                .reportType(ReportType.STORY)
                .reportReason(ReportReason.SPAMMARKET.getName())
                .reporterUsername("username")
                .reportStatus(ReportStatus.CONFIRMED)
                .build());


        Pageable requestPageable = PageRequest.of(1, 15);
        Pageable pageableAfterFind = PageRequest.of(0, 15);

        PageImpl<ReportQueryDto> pageList = new PageImpl<>(queryDtoList, pageableAfterFind, 0);

        given(reportRepository.findReport(any(ReportStatus.class), any(Pageable.class))).willReturn(pageList);

        //when
        PageCustom<ReportDto> report = reportService.findReport(reportStatus, requestPageable);

        //then
        assertThat(report.getContent().get(0).getId()).isEqualTo(1L);
        assertThat(report.getContent().get(0).getReportType()).isEqualTo(ReportType.STORY);

        assertThat(report.getPageable().getNumber()).isEqualTo(1);
    }

    @Test
    @DisplayName("신고 상태가 적절하지 않을 경우 예외 처리를 해야 함")
    public void find_report_type_not_valid() throws Exception {
        //given
        String reportStatus = "ALL2";

        PageRequest pageRequest = PageRequest.of(0, 15);

        //when  //then
        assertThatThrownBy(() -> reportService.findReport(reportStatus, pageRequest))
                .isInstanceOf(ReportException.class)
                .hasMessageContaining("유효한 신고 타입이 아님");

    }

    @Test
    @DisplayName("신고 상태가 확인 상태로 변경되어야 함")
    void confirm_report_success() {

        //given
        Member testActiveMember = createActiveTestMember();
        Story testStory = createStory(testActiveMember, "title", "city");

        Report testReport = createReport(testActiveMember, ReportType.STORY, testStory);


        given(reportRepository.findById(anyLong())).willReturn(Optional.ofNullable(testReport));


        //when
        reportService.confirmReport(1L);

        //then
        Report report = reportRepository.findById(1L).orElseThrow(() -> new RuntimeException());

        assertThat(report.getReportStatus()).isEqualTo(ReportStatus.CONFIRMED);


    }

    @Test
    @DisplayName("확인할 신고를 찾지 못할 시 예외 처리를 해야 함")
    void confirm_report_not_found() {

        //given
        Member testActiveMember = createActiveTestMember();
        Story testStory = createStory(testActiveMember, "title", "city");



        //when, then
        assertThatThrownBy(() -> reportService.confirmReport(1L))
                .isInstanceOf(ReportException.class)
                .hasMessageContaining("해당 신고내역을 찾을 수 없음");



    }

    @Test
    @DisplayName("신고당한 스토리에 대한 삭제 처리가 정상적으로 이루어져야 함")
    void delete_report_story_success() {

        //given
        Member testActiveMember = createActiveTestMember();
        Story testStory = createStory(testActiveMember, "title", "city");

        Report testReport = createReport(testActiveMember, ReportType.STORY, testStory);

        given(reportRepository.findById(anyLong())).willReturn(Optional.ofNullable(testReport));

        //when
        reportService.deleteReportTarget(1L);

        //then
        Report resultReport = reportRepository.findById(1L).orElseThrow(RuntimeException::new);

        assertThat(resultReport.getReportStatus()).isEqualTo(ReportStatus.DELETED);
        assertThat(resultReport.getStory().getIsDeleted()).isTrue();

    }

    @Test
    @DisplayName("신고당한 댓글에 대한 삭제 처리가 정상적으로 이루어져야 함")
    void delete_report_comment_success() {

        //given
        Member testActiveMember = createActiveTestMember();
        Story testStory = createStory(testActiveMember, "title", "city");
        Comment testComment = createComment(testActiveMember, testStory);

        Report testReport = createReport(testActiveMember, ReportType.COMMENT, testComment);

        given(reportRepository.findById(anyLong())).willReturn(Optional.ofNullable(testReport));

        //when
        reportService.deleteReportTarget(1L);

        //then
        Report resultReport = reportRepository.findById(1L).orElseThrow(RuntimeException::new);

        assertThat(resultReport.getReportStatus()).isEqualTo(ReportStatus.DELETED);
        assertThat(resultReport.getComment().getIsDeleted()).isTrue();

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

    Member createActiveTestMember2() {
        return Member.builder()
                .id(4L)
                .nickname("테스트사용자닉네임2")
                .username("테스트사용자유저네임2")
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

    Report createReport(Member member, ReportType reportType, Story story) {
       return Report.builder()
                .member(member)
                .reportType(reportType)
                .reportReason(ReportReason.SPAMMARKET)
                .story(story)
                .build();
    }

    Report createReport(Member member, ReportType reportType, Comment comment) {
       return Report.builder()
                .member(member)
                .reportType(reportType)
                .reportReason(ReportReason.SPAMMARKET)
                .comment(comment)
                .build();
    }


}