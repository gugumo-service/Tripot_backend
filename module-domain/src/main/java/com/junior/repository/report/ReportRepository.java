package com.junior.repository.report;

import com.junior.domain.member.Member;
import com.junior.domain.report.Report;
import com.junior.domain.story.Comment;
import com.junior.domain.story.Story;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long>, ReportRepositoryCustom {


    boolean existsByMemberAndStory(Member member, Story story);
    boolean existsByMemberAndComment(Member member, Comment comment);
}
