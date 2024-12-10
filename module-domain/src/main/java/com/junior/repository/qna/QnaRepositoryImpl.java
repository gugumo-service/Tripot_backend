package com.junior.repository.qna;

import com.junior.dto.notice.NoticeAdminDto;
import com.junior.dto.notice.NoticeUserDto;
import com.junior.dto.notice.QNoticeAdminDto;
import com.junior.dto.notice.QNoticeUserDto;
import com.junior.dto.qna.QQnaAdminDto;
import com.junior.dto.qna.QQnaUserDto;
import com.junior.dto.qna.QnaAdminDto;
import com.junior.dto.qna.QnaUserDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.junior.domain.admin.QNotice.notice;
import static com.junior.domain.admin.QQna.qna;

@RequiredArgsConstructor
@Slf4j
public class QnaRepositoryImpl implements QnaRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    /**
     * 관리자 페이지에서의 Q&A 조회
     * @param q
     * @param pageable
     * @return 페이지네이션 기반 Q&A
     */
    @Override
    public Page<QnaAdminDto> findQna(String q, Pageable pageable) {

        log.info("[{}] 관리자 Q&A 조회 쿼리 실행", Thread.currentThread().getStackTrace()[1].getMethodName());
        List<QnaAdminDto> searchResult = queryFactory.select(
                        new QQnaAdminDto(
                                qna.id,
                                qna.question
                        )
                )
                .from(qna)
                .where(
                        queryContains(q), qna.isDeleted.isFalse()
                )
                .orderBy(qna.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        log.info("[{}] Q&A 카운트 쿼리 실행", Thread.currentThread().getStackTrace()[1].getMethodName());
        JPAQuery<Long> count = queryFactory.select(qna.count())
                .from(qna)
                .where(queryContains(q), qna.isDeleted.isFalse());

        return PageableExecutionUtils.getPage(searchResult, pageable, count::fetchOne);

    }


    /**
     * 사용자 어플에서의 Q&A 조회
     * Spring Data JPA에서 Slice 기반 페이지네이션을 지원하나 no-offset 방식이 아님
     * 따라서 성능 최적화를 위해 직접 쿼리를 작성하여 구현
     * @param cursorId
     * @param pageable
     * @return 무한스크롤 기반 Q&A
     */
    @Override
    public Slice<QnaUserDto> findQna(Long cursorId, Pageable pageable) {
        log.info("[{}] 사용자 Q&A 조회 쿼리 실행", Thread.currentThread().getStackTrace()[1].getMethodName());

        List<QnaUserDto> resultList = queryFactory.select(
                        new QQnaUserDto(
                                qna.id, qna.question, qna.answer, qna.createdDate
                        )
                )
                .from(qna)
                .where(idLt(cursorId), qna.isDeleted.isFalse())
                .limit(pageable.getPageSize() + 1)
                .orderBy(qna.createdDate.desc())
                .fetch();

        boolean hasNext;

        if (resultList.size() > pageable.getPageSize()) {
            resultList.remove(resultList.size() - 1);
            hasNext = true;
        } else {
            hasNext = false;
        }

        return new SliceImpl<>(resultList, pageable, hasNext);


    }

    private static BooleanExpression idLt(Long cursorId) {

        return cursorId != null ? qna.id.lt(cursorId) : null;
    }

    private static BooleanExpression queryContains(String q) {
        return qna.question.contains(q).or(qna.answer.contains(q));
    }
}
