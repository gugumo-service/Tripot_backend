package com.junior.repository.admin;

import com.junior.dto.admin.notice.NoticeDto;
import com.junior.dto.admin.notice.QNoticeDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.junior.domain.admin.QNotice.notice;

@RequiredArgsConstructor
@Slf4j
public class NoticeRepositoryImpl implements NoticeRepositoryCustom{

    private final JPAQueryFactory queryFactory;


    @Override
    public Page<NoticeDto> findNotice(String q, Pageable pageable) {

        log.info("[{}] 공지사항 조회 쿼리 실행", Thread.currentThread().getStackTrace()[1].getMethodName());
        List<NoticeDto> searchResult = queryFactory.select(
                        new QNoticeDto(
                                notice.id,
                                notice.title
                        )
                )
                .from(notice)
                .where(
                        queryContains(q)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        log.info("[{}] 공지사항 카운트 쿼리 실행", Thread.currentThread().getStackTrace()[1].getMethodName());
        JPAQuery<Long> count = queryFactory.select(notice.count())
                .from(notice)
                .where(queryContains(q));

        return PageableExecutionUtils.getPage(searchResult, pageable, count::fetchOne);

    }

    private static BooleanExpression queryContains(String q) {
        return notice.title.contains(q).or(notice.content.contains(q));
    }
}
