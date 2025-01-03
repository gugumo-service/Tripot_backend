package com.junior.domain.report;

import com.junior.domain.base.BaseEntity;
import com.junior.domain.member.Member;
import com.junior.domain.story.Comment;
import com.junior.domain.story.Story;
import com.junior.exception.ReportException;
import com.junior.exception.StatusCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private ReportType reportType;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(length = 10)
    private ReportStatus reportStatus = ReportStatus.UNCONFIRMED;

    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private ReportReason reportReason;

    private LocalDateTime confirmTime;


    /**
     * reportType=STORY일 경우, 그 외의 경우 NULL
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id")
    private Story story;

    /**
     * reportType=COMMENT일 경우, 그 외의 경우 NULL
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;


    public void confirmReport() {
        this.reportStatus = ReportStatus.CONFIRMED;
        this.confirmTime = LocalDateTime.now();
    }

    public void deleteReportTarget() {
        if (reportType.equals(ReportType.STORY)) {
            this.story.deleteStory();
        } else if (reportType.equals(ReportType.COMMENT)) {
            this.comment.deleteComment(true);
        } else {
            throw new ReportException(StatusCode.REPORT_NOT_VALID);
        }

        this.reportStatus = ReportStatus.DELETED;
        this.confirmTime = LocalDateTime.now();
    }
}
