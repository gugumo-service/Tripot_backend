package com.junior.repository.notice;

import com.junior.dto.notice.NoticeAdminDto;
import com.junior.dto.admin.notice.QNoticeDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.junior.domain.admin.QNotice.notice;

@RequiredArgsConstructor
@Slf4j
public class NoticeRepositoryImpl implements NoticeRepositoryCustom{

    private final JPAQueryFactory queryFactory;


    /**
     * 관리자 페이지에서의 공지사항 조회
     * @param q
     * @param pageable
     * @return 페이지네이션 기반 공지사항
     */
    @Override
    public Page<NoticeAdminDto> findNotice(String q, Pageable pageable) {

        log.info("[{}] 관리자 공지사항 조회 쿼리 실행", Thread.currentThread().getStackTrace()[1].getMethodName());
        List<NoticeAdminDto> searchResult = queryFactory.select(
                        new QNoticeDto(
                                notice.id,
                                notice.title
                        )
                )
                .from(notice)
                .where(
                        queryContains(q), notice.isDeleted.isFalse()
                )
                .orderBy(notice.createdDate.desc())
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
