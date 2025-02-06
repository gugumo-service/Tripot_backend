package com.junior.service.qna;

import com.junior.domain.admin.Qna;
import com.junior.domain.member.Member;
import com.junior.dto.qna.CreateQnaDto;
import com.junior.dto.qna.QnaAdminDto;
import com.junior.dto.qna.QnaDetailDto;
import com.junior.dto.qna.UpdateQnaDto;
import com.junior.exception.NotValidMemberException;
import com.junior.exception.QnaException;
import com.junior.exception.StatusCode;
import com.junior.page.PageCustom;
import com.junior.repository.member.MemberRepository;
import com.junior.repository.qna.QnaRepository;
import com.junior.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QnaAdminService {

    private final MemberRepository memberRepository;
    private final QnaRepository qnaRepository;

    @Transactional
    public void saveQna(UserPrincipal principal, CreateQnaDto createQnaDto) {

        Member author = memberRepository.findById(principal.getMember().getId()).orElseThrow(
                () -> new NotValidMemberException(StatusCode.INVALID_MEMBER)
        );

        Qna qna = Qna.builder()
                .question(createQnaDto.question())
                .answer(createQnaDto.answer())
                .member(author)
                .build();

        log.info("[{}] Q&A 저장", Thread.currentThread().getStackTrace()[1].getMethodName());
        qnaRepository.save(qna);

    }


    public PageCustom<QnaAdminDto> findQna(String q, Pageable pageable) {

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize());

        Page<QnaAdminDto> page = qnaRepository.findQna(q, pageRequest);

        log.info("[{}] Q&A 조회 결과 리턴 page: {}", Thread.currentThread().getStackTrace()[1].getMethodName(), pageable.getPageNumber());
        return new PageCustom<>(page.getContent(), page.getPageable(), page.getTotalElements());

    }

    public QnaDetailDto findQnaDetail(Long qnaId) {

        log.info("[{}] Q&A 세부정보 조회", Thread.currentThread().getStackTrace()[1].getMethodName());
        Qna qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new QnaException(StatusCode.QNA_NOT_FOUND));

        if (qna.getIsDeleted()) {
            log.error("[{}] 삭제된 Q&A 조회 - id: {}", Thread.currentThread().getStackTrace()[1].getMethodName(), qna.getId());
            throw new QnaException(StatusCode.QNA_NOT_FOUND);
        }

        log.info("[{}] Q&A 작성자 조회", Thread.currentThread().getStackTrace()[1].getMethodName());
        Member author = memberRepository.findById(qna.getCreatedBy())
                .orElseThrow(() -> new NotValidMemberException(StatusCode.MEMBER_NOT_FOUND));

        QnaDetailDto qnaDetailDto = QnaDetailDto.builder()
                .id(qna.getId())
                .question(qna.getQuestion())
                .answer(qna.getAnswer())
                .build();

        return qnaDetailDto;

    }


    @Transactional
    public void updateQna(Long updateQnaId, UpdateQnaDto updateQnaDto) {

        Qna qna = qnaRepository.findById(updateQnaId)
                .orElseThrow(() -> new QnaException(StatusCode.QNA_NOT_FOUND));

        log.info("[{}] Q&A 수정 내용 - id: {}", Thread.currentThread().getStackTrace()[1].getMethodName(), updateQnaId);
        qna.update(updateQnaDto);


    }


    @Transactional
    public void deleteQna(Long qnaId) {

        Qna qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new QnaException(StatusCode.QNA_NOT_FOUND));

        log.info("[{}] Q&A 삭제 - id: {}", Thread.currentThread().getStackTrace()[1].getMethodName(), qnaId);
        qna.softDelete();


    }
}
