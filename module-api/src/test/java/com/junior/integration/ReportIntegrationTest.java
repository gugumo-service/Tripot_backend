package com.junior.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junior.controller.report.ReportController;
import com.junior.domain.member.Member;
import com.junior.domain.report.Report;
import com.junior.domain.report.ReportReason;
import com.junior.domain.report.ReportStatus;
import com.junior.domain.report.ReportType;
import com.junior.domain.story.Comment;
import com.junior.domain.story.Story;
import com.junior.dto.report.CreateReportDto;
import com.junior.repository.comment.CommentRepository;
import com.junior.repository.member.MemberRepository;
import com.junior.repository.report.ReportRepository;
import com.junior.repository.story.StoryRepository;
import com.junior.security.WithMockCustomAdmin;
import com.junior.security.WithMockCustomUser;
import com.junior.security.WithMockCustomUser2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ReportIntegrationTest extends IntegrationControllerTest {

    @Autowired
    private ReportController reportController;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;



    @BeforeEach
    void init() throws InterruptedException {
        Member preactiveTestMember = createPreactiveTestMember();
        Member activeTestMember = createActiveTestMember();
        Member testAdmin = createAdmin();
        Member activeTestMember2 = createActiveTestMember2();

        memberRepository.save(preactiveTestMember);
        memberRepository.save(activeTestMember);
        memberRepository.save(testAdmin);
        memberRepository.save(activeTestMember2);

        Story testStory = createStory(activeTestMember);
        storyRepository.save(testStory);

        Comment testComment = createComment(activeTestMember, testStory);
        commentRepository.save(testComment);

        for (int i = 1; i <= 100; i++) {
            Report testReport;

            if (i % 2 == 1) {

                testReport = Report.builder()
                        .reportType(ReportType.STORY)
                        .story(testStory)
                        .reportReason(ReportReason.SPAMMARKET)
                        .member(activeTestMember)
                        .build();


            } else {
                testReport = Report.builder()
                        .reportType(ReportType.COMMENT)
                        .comment(testComment)
                        .reportReason(ReportReason.SPAMMARKET)
                        .reportStatus(ReportStatus.CONFIRMED)
                        .member(activeTestMember)
                        .build();


            }

            Thread.sleep(5);

            reportRepository.save(testReport);
        }


    }

    @Test
    @DisplayName("스토리에 대한 신고 기능이 정상적으로 이루어져야 함")
    @WithMockCustomUser2
    public void report_story() throws Exception {
        //given
        CreateReportDto createReportDto = new CreateReportDto(1L, "STORY", "스팸홍보");
        String content = objectMapper.writeValueAsString(createReportDto);

        //when
        ResultActions actions = mockMvc.perform(
                post("/api/v1/reports")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );


        //then
        actions
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customCode").value("REPORT-SUCCESS-001"))
                .andExpect(jsonPath("$.customMessage").value("신고 성공"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data").value(nullValue()));

        //신고 내역이 정상적으로 저장되어야 함
        Report report = reportRepository.findById(1L).orElseThrow(RuntimeException::new);

        assertThat(report.getMember().getUsername()).isEqualTo("테스트사용자유저네임2");
        assertThat(report.getReportType()).isEqualTo(ReportType.STORY);
        assertThat(report.getReportReason()).isEqualTo(ReportReason.SPAMMARKET);
        assertThat(report.getStory().getTitle()).isEqualTo("testStoryTitle");
        assertThat(report.getComment()).isNull();
        assertThat(report.getReportStatus()).isEqualTo(ReportStatus.UNCONFIRMED);


    }

    @Test
    @DisplayName("본인 글은 신고할 수 없어야 함")
    @WithMockCustomUser
    public void report_story_equal_author() throws Exception {
        //given
        CreateReportDto createReportDto = new CreateReportDto(1L, "STORY", "스팸홍보");
        String content = objectMapper.writeValueAsString(createReportDto);

        //when
        ResultActions actions = mockMvc.perform(
                post("/api/v1/reports")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );


        //then
        actions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.customCode").value("REPORT-ERR-004"))
                .andExpect(jsonPath("$.customMessage").value("본인 글은 신고할 수 없음"))
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.data").value(nullValue()));



    }

    @Test
    @DisplayName("신고 조회 기능이 정상적으로 작동되어야 함")
    @WithMockCustomAdmin
    void findReport() throws Exception {

        //given
        String reportType = "CONFIRMED";

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/v1/admin/reports")
                        .queryParam("report_type",reportType)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customCode").value("REPORT-SUCCESS-004"))
                .andExpect(jsonPath("$.customMessage").value("신고 조회 성공"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.pageable.number").value(1))
                .andExpect(jsonPath("$.data.content[0].reportReason").value("스팸홍보"))
                .andExpect(jsonPath("$.data.content[0].reportStatus").value("CONFIRMED"));



    }

    @Test
    @DisplayName("신고 확인 기능이 정상적으로 적동되어야 함")
    @WithMockCustomAdmin
    void confirmReport() throws Exception {

        //given
        Long reportId = 1L;

        Member testMember = memberRepository.findById(2L).get();
        Story testStory = storyRepository.findById(1L).get();

        Report report = Report.builder()
                .member(testMember)
                .reportType(ReportType.STORY)
                .reportReason(ReportReason.SPAMMARKET)
                .story(testStory)
                .build();

        reportRepository.save(report);

        //when
        ResultActions actions = mockMvc.perform(
                patch("/api/v1/admin/reports/{report_id}/confirm", reportId)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customCode").value("REPORT-SUCCESS-002"))
                .andExpect(jsonPath("$.customMessage").value("신고 처리(미삭제) 성공"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data").value(nullValue()));

        Report resultReport = reportRepository.findById(1L).orElseThrow(RuntimeException::new);

        assertThat(resultReport.getReportStatus()).isEqualTo(ReportStatus.CONFIRMED);
    }

    @Test
    @DisplayName("신고 대상 스토리 삭제 기능이 정상적으로 적동되어야 함")
    @WithMockCustomAdmin
    void deleteReportTarget_story() throws Exception {

        //given
        Long reportId = 1L;

        Member testMember = memberRepository.findById(2L).get();
        Story testStory = storyRepository.findById(1L).get();

        Report report = Report.builder()
                .member(testMember)
                .reportType(ReportType.STORY)
                .reportReason(ReportReason.SPAMMARKET)
                .story(testStory)
                .build();

        reportRepository.save(report);

        //when
        ResultActions actions = mockMvc.perform(
                patch("/api/v1/admin/reports/{report_id}/delete", reportId)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customCode").value("REPORT-SUCCESS-003"))
                .andExpect(jsonPath("$.customMessage").value("신고 처리(삭제) 성공"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data").value(nullValue()));

        Report resultReport = reportRepository.findById(1L).orElseThrow(RuntimeException::new);
        Story resultStory = storyRepository.findById(1L).orElseThrow(RuntimeException::new);

        assertThat(resultReport.getReportStatus()).isEqualTo(ReportStatus.DELETED);
        assertThat(resultStory.getIsDeleted()).isTrue();

    }

    @Test
    @DisplayName("신고 대상 댓글 삭제 기능이 정상적으로 적동되어야 함")
    @WithMockCustomAdmin
    void deleteReportTarget_comment() throws Exception {

        //given
        Long reportId = 1L;

        Member testMember = memberRepository.findById(2L).get();
        Comment testComment = commentRepository.findById(1L).get();


        Report report = Report.builder()
                .member(testMember)
                .reportType(ReportType.COMMENT)
                .reportReason(ReportReason.SPAMMARKET)
                .comment(testComment)
                .build();

        reportRepository.save(report);

        //when
        ResultActions actions = mockMvc.perform(
                patch("/api/v1/admin/reports/{report_id}/delete", reportId)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customCode").value("REPORT-SUCCESS-003"))
                .andExpect(jsonPath("$.customMessage").value("신고 처리(삭제) 성공"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data").value(nullValue()));

        Report resultReport = reportRepository.findById(1L).orElseThrow(RuntimeException::new);
        Comment resultComment = commentRepository.findById(1L).orElseThrow(RuntimeException::new);

        assertThat(resultReport.getReportStatus()).isEqualTo(ReportStatus.DELETED);
        assertThat(resultComment.getIsDeleted()).isTrue();

    }


}
