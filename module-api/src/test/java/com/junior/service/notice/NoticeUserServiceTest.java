package com.junior.service.notice;

import com.junior.repository.notice.NoticeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NoticeUserServiceTest {

    @Mock
    private NoticeRepository noticeRepository;

    @InjectMocks
    private NoticeUserService noticeUserService;

    @Test
    @DisplayName("공지사항 조회 기능이 정상적으로 수행되어야 함")
    void findNotice() {

        //given
        Long cursorId = 1L;
        int size = 1;

        Pageable pageable = PageRequest.of(0, size);

        //when
        noticeUserService.findNotice(cursorId, size);

        //then
        verify(noticeRepository).findNotice(cursorId, pageable);
    }
}