package com.junior.repository.report;

import com.junior.domain.report.ReportType;
import com.junior.dto.report.QReportQueryDto;
import com.junior.dto.report.ReportDto;
import com.junior.dto.report.ReportQueryDto;
import com.junior.exception.ReportException;
import com.junior.exception.StatusCode;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.junior.domain.member.QMember.member;
import static com.junior.domain.report.QReport.report;
import static com.junior.domain.story.QComment.comment;
import static com.junior.domain.story.QStory.story;

@Slf4j
@RequiredArgsConstructor
public class ReportRepositoryImpl implements ReportRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    //이 로직은 신고 상세조회로?
    //모든 report를 한 번에 조회?
    @Override
    public Page<ReportQueryDto> findReport(ReportType reportType, Pageable pageable) {

        List<ReportQueryDto> result;
        JPAQuery<Long> count;

        result = queryFactory.select(
                        new QReportQueryDto(
                                report.id,
                                report.member.username,
                                report.reportType,
                                report.createdDate,
                                report.reportStatus,
                                report.reportReason,
                                report.confirmTime,
                                report.story.title,
                                report.comment.content
                        )
                )
                .from(report)
                .leftJoin(report.member, member)
                .leftJoin(report.story, story)
                .leftJoin(report.comment, comment)
                .where(reportTypeEq(reportType))
                .orderBy(report.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        count = queryFactory.select(report.count())
                .from(report)
                .leftJoin(report.member, member)
                .leftJoin(report.story, story)
                .leftJoin(report.comment, comment);




        return PageableExecutionUtils.getPage(result, pageable, count::fetchOne);

    }

    private BooleanExpression reportTypeEq(ReportType reportType) {
        return reportType != null ? report.reportType.eq(reportType) : null;
    }
}
