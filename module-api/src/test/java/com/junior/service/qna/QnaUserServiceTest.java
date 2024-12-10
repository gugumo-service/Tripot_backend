package com.junior.service.qna;

import com.junior.repository.qna.QnaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class QnaUserServiceTest {

    @Mock
    private QnaRepository qnaRepository;

    @InjectMocks
    private QnaUserService qnaUserService;

    @Test
    @DisplayName("Q&A 조회 기능이 정상적으로 수행되어야 함")
    void findNotice() {

        //given
        Long cursorId = 1L;
        int size = 1;

        Pageable pageable = PageRequest.of(0, size);

        //when
        qnaUserService.findQna(cursorId, size);

        //then
        verify(qnaRepository).findQna(cursorId, pageable);
    }
}