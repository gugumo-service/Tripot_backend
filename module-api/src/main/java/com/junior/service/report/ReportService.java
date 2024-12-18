package com.junior.service.report;

import com.junior.domain.member.Member;
import com.junior.domain.report.Report;
import com.junior.domain.report.ReportReason;
import com.junior.domain.report.ReportType;
import com.junior.domain.story.Comment;
import com.junior.domain.story.Story;
import com.junior.dto.report.CreateReportDto;
import com.junior.exception.*;
import com.junior.repository.comment.CommentRepository;
import com.junior.repository.member.MemberRepository;
import com.junior.repository.report.ReportRepository;
import com.junior.repository.story.StoryRepository;
import com.junior.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final StoryRepository storyRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;

    @Transactional
    public void save(CreateReportDto createReportDto, UserPrincipal principal) {

        Member member = memberRepository.findById(principal.getMember().getId()).orElseThrow(
                () -> new NotValidMemberException(StatusCode.INVALID_MEMBER)
        );

        ReportType reportType;

        try{
            reportType = ReportType.valueOf(createReportDto.reportType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ReportException(StatusCode.REPORT_NOT_VALID);
        }

        ReportReason reportReason;


        try{
            reportReason = ReportReason.nameOf(createReportDto.reportReason());
        } catch (IllegalArgumentException e) {
            throw new ReportException(StatusCode.REPORT_NOT_VALID);
        }

        Report report;

        if (reportType.equals(ReportType.STORY)) {

            Story story = storyRepository.findById(createReportDto.reportContentId())
                    .orElseThrow(()->new StoryNotFoundException(StatusCode.STORY_NOT_FOUND));

            report = Report.builder()
                    .member(member)
                    .reportType(reportType)
                    .reportReason(reportReason)
                    .story(story)
                    .build();
        } else if (reportType.equals(ReportType.COMMENT)) {
            Comment comment = commentRepository.findById(createReportDto.reportContentId())
                    .orElseThrow(() -> new CommentNotFoundException(StatusCode.COMMENT_NOT_FOUND));

            report = Report.builder()
                    .member(member)
                    .reportType(reportType)
                    .reportReason(reportReason)
                    .comment(comment)
                    .build();
        } else {
            throw new ReportException(StatusCode.REPORT_NOT_VALID);
        }

        reportRepository.save(report);
    }

    //TODO: findReport 구현(페이징)

    public void confirmReport(Long id) {

        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ReportException(StatusCode.REPORT_NOT_FOUND));

        report.confirmReport();
    }


}
