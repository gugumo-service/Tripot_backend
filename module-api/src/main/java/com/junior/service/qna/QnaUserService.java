package com.junior.service.qna;

import com.junior.dto.qna.QnaUserDto;
import com.junior.repository.qna.QnaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class QnaUserService {

    private final QnaRepository qnaRepository;

    public Slice<QnaUserDto> findQna(Long cursorId, int size) {

        Pageable pageable = PageRequest.of(0, size);

        log.info("[{}] Q&A 유저 조회", Thread.currentThread().getStackTrace()[1].getMethodName());
        return qnaRepository.findQna(cursorId, pageable);

    }


}
