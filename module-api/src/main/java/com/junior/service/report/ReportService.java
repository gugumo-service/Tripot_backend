package com.junior.service.report;

import com.junior.domain.member.Member;
import com.junior.domain.report.Report;
import com.junior.domain.report.ReportReason;
import com.junior.domain.report.ReportType;
import com.junior.domain.story.Comment;
import com.junior.domain.story.Story;
import com.junior.dto.report.*;
import com.junior.exception.*;
import com.junior.page.PageCustom;
import com.junior.repository.comment.CommentRepository;
import com.junior.repository.member.MemberRepository;
import com.junior.repository.report.ReportRepository;
import com.junior.repository.story.StoryRepository;
import com.junior.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


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

            if (story.getMember().equals(member)) {
                throw new ReportException(StatusCode.REPORT_EQUALS_AUTHOR);
            }

            report = Report.builder()
                    .member(member)
                    .reportType(reportType)
                    .reportReason(reportReason)
                    .story(story)
                    .build();
        } else if (reportType.equals(ReportType.COMMENT)) {
            Comment comment = commentRepository.findById(createReportDto.reportContentId())
                    .orElseThrow(() -> new CommentNotFoundException(StatusCode.COMMENT_NOT_FOUND));

            if (comment.getMember().equals(member)) {
                throw new ReportException(StatusCode.REPORT_EQUALS_AUTHOR);
            }

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

    public <T extends ReportDto> PageCustom<T> findReport(String reportType, Pageable pageable) {

        ReportType eReportType;

        if (reportType.equals("ALL")) {
            eReportType = null;
        } else {
            try {
                eReportType = ReportType.valueOf(reportType);
            } catch (IllegalArgumentException e) {
                throw new ReportException(StatusCode.REPORT_TYPE_NOT_VALID);
            }
        }

        Page<ReportQueryDto> report = reportRepository.findReport(eReportType, pageable);

        List<T> result = report.stream()
                .map(r -> convertReport(r))
                .map(r -> (T) r)
                .collect(Collectors.toList());

        return new PageCustom<>(result, pageable, report.getTotalElements());
    }

    public void confirmReport(Long id) {

        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ReportException(StatusCode.REPORT_NOT_FOUND));

        report.confirmReport();
    }

    public void deleteReportTarget(Long id) {

        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ReportException(StatusCode.REPORT_NOT_FOUND));

        report.deleteReportTarget();

    }

    private <T extends ReportDto> T convertReport(ReportQueryDto r) {

        ReportDto result;

        if (r.getReportType().equals(ReportType.STORY)) {
            result = StoryReportDto.builder()
                    .id(r.getId())
                    .reporterUsername(r.getReporterUsername())
                    .reportType(r.getReportType())
                    .reportedTime(r.getReportedTime())
                    .reportStatus(r.getReportStatus())
                    .reportReason(r.getReportReason().getName())
                    .title(r.getTitle())
                    .build();
        } else if (r.getReportType().equals(ReportType.COMMENT)) {
            result = CommentReportDto.builder()
                    .id(r.getId())
                    .reporterUsername(r.getReporterUsername())
                    .reportType(r.getReportType())
                    .reportedTime(r.getReportedTime())
                    .reportStatus(r.getReportStatus())
                    .reportReason(r.getReportReason().getName())
                    .content(r.getContent())
                    .build();
        } else {
            throw new ReportException(StatusCode.REPORT_NOT_VALID);
        }

        return (T) result;
    }


}
