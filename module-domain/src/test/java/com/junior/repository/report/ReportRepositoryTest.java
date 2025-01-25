package com.junior.repository.report;

import com.junior.domain.member.Member;
import com.junior.domain.report.Report;
import com.junior.domain.report.ReportReason;
import com.junior.domain.report.ReportStatus;
import com.junior.domain.report.ReportType;
import com.junior.domain.story.Comment;
import com.junior.domain.story.Story;
import com.junior.dto.report.ReportQueryDto;
import com.junior.repository.BaseRepositoryTest;
import com.junior.repository.comment.CommentRepository;
import com.junior.repository.member.MemberRepository;
import com.junior.repository.story.StoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class ReportRepositoryTest extends BaseRepositoryTest {


    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ReportRepository reportRepository;


    @BeforeEach
    void init() throws InterruptedException {

        Member testMember = createActiveTestMember();

        memberRepository.save(testMember);

        Story testStory = createStory(testMember);
        storyRepository.save(testStory);

        Comment testComment = createComment(testMember, testStory);
        commentRepository.save(testComment);


        for (int i = 1; i <= 100; i++) {
            Report testReport;

            if (i % 2 == 1) {

                testReport = Report.builder()
                        .reportType(ReportType.STORY)
                        .story(testStory)
                        .reportReason(ReportReason.SPAMMARKET)
                        .member(testMember)
                        .build();


            } else {
                testReport = Report.builder()
                        .reportType(ReportType.COMMENT)
                        .comment(testComment)
                        .reportReason(ReportReason.SPAMMARKET)
                        .reportStatus(ReportStatus.CONFIRMED)
                        .member(testMember)
                        .build();


            }

            Thread.sleep(5);

            reportRepository.save(testReport);


        }


    }

    @Test
    @DisplayName("신고내역을 정상적으로 페이징하여 가져올 수 있어야 함")
    public void findReport_success() throws Exception {
        //given
        ReportStatus reportStatus = null;

        PageRequest pageRequest = PageRequest.of(0, 15);

        //when
        Page<ReportQueryDto> page = reportRepository.findReport(reportStatus, pageRequest);
        List<ReportQueryDto> content = page.getContent();


        //then
        //100개의 페이지를 페이지 크기 15로 잘랐을 때 첫 페이지의 크기는 15이다.
        assertThat(content.size()).isEqualTo(15);

        //페이지는 createDate의 내림차순, 따라서 가장 마지막에 넣은 공지사항 100이 첫 요소여야 한다.
        assertThat(content.get(0).getId()).isEqualTo(100L);

        //요소의 총 개수 100개를 정상적으로 조회할 수 있어야 함
        assertThat(page.getTotalElements()).isEqualTo(100);

    }


    @Test
    @DisplayName("신고내역의 마지막 페이징이 정상적으로 되어야 함")
    public void findReport_success_last() throws Exception {
        //given
        ReportStatus reportStatus = null;

        PageRequest pageRequest = PageRequest.of(6, 15);

        //when
        Page<ReportQueryDto> page = reportRepository.findReport(reportStatus, pageRequest);
        List<ReportQueryDto> content = page.getContent();


        //then
        //100개의 페이지를 페이지 크기 15로 잘랐을 때 마지막 페이지의 크기는 (100%15) 10이다.
        assertThat(content.size()).isEqualTo(10);

        //가장 마지막 페이지의 첫 요소는 10번 신고내역이어야 함
        assertThat(content.get(0).getId()).isEqualTo(10);

        //요소의 총 개수 100개를 정상적으로 조회할 수 있어야 함
        assertThat(page.getTotalElements()).isEqualTo(100);

    }

    @Test
    @DisplayName("신고내역의 status 조건 조회가 정상적으로 되어야 함")
    public void findReport_success_status() throws Exception {
        //given
        ReportStatus reportStatus = ReportStatus.CONFIRMED;

        PageRequest pageRequest = PageRequest.of(0, 15);

        //when
        Page<ReportQueryDto> page = reportRepository.findReport(reportStatus, pageRequest);
        List<ReportQueryDto> content = page.getContent();


        //then
        //50개의 페이지를 페이지 크기 15로 잘랐을 때 첫 페이지의 크기는 15이다.
        assertThat(content.size()).isEqualTo(15);

        //조회된 모든 신고내역이 처리 완료 상태여야함
        for (int i = 0; i < 15; i++) {
            assertThat(content.get(i).getReportStatus()).isEqualTo(ReportStatus.CONFIRMED);
        }


        //요소의 총 개수 100개를 정상적으로 조회할 수 있어야 함
        assertThat(page.getTotalElements()).isEqualTo(100);


    }


}