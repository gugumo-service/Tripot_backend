package com.junior.service.admin;

import com.junior.domain.admin.Notice;
import com.junior.dto.admin.notice.CreateNoticeDto;
import com.junior.dto.admin.notice.UpdateNoticeDto;
import com.junior.exception.NoticeException;
import com.junior.repository.admin.NoticeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NoticeServiceTest {

    @Mock
    private NoticeRepository noticeRepository;

    @InjectMocks
    private NoticeService noticeService;

    @Test
    @DisplayName("공지사항 저장 로직이 정상적으로 실행되어야 함")
    void saveNotice_success() {

        //given
        CreateNoticeDto createNoticeDto = new CreateNoticeDto("title", "content");

        //when
        noticeService.saveNotice(createNoticeDto);

        //then
        verify(noticeRepository).save(any(Notice.class));


    }

    @Test
    @DisplayName("공지사항 수정 로직이 정상적으로 실행되어야 함")
    void updateNotice_success() {

        //given
        Long updateNoticeId = 1L;
        UpdateNoticeDto updateNoticeDto = new UpdateNoticeDto("new title", "new content");

        Notice notice = createNotice();

        given(noticeRepository.findById(updateNoticeId)).willReturn(Optional.ofNullable(notice));

        //when
        noticeService.updateNotice(updateNoticeId, updateNoticeDto);

        //then
        Notice updatedNotice = noticeRepository.findById(1L).get();

        Assertions.assertThat(notice.getTitle()).isEqualTo("new title");
        Assertions.assertThat(notice.getContent()).isEqualTo("new content");


    }

    @Test
    @DisplayName("공지 수정 - 공지사항을 찾지 못했을 때 관련 예외처리를 해야 함")
    void updateNotice_notice_not_found() {

        //given
        Long updateNoticeId = 1L;
        UpdateNoticeDto updateNoticeDto = new UpdateNoticeDto("new title", "new content");


        //when, then
        Assertions.assertThatThrownBy(() -> noticeService.updateNotice(updateNoticeId, updateNoticeDto))
                .isInstanceOf(NoticeException.class)
                .hasMessageContaining("해당 공지사항을 찾을 수 없음");




    }

    @Test
    @DisplayName("공지사항 삭제 로직이 정상적으로 실행되어야 함")
    void deleteNotice_success() {

        //given
        Notice notice = createNotice();
        Long deleteNoticeId = 1L;

        given(noticeRepository.findById(deleteNoticeId)).willReturn(Optional.ofNullable(notice));

        //when
        noticeService.deleteNotice(deleteNoticeId);

        //then
        Assertions.assertThat(notice.getIsDeleted()).isTrue();
    }

    @Test
    @DisplayName("공지 삭제 - 공지사항을 찾지 못했을 때 관련 예외처리를 해야 함")
    void deleteNotice_notice_not_found() {

        //given
        Long deleteNoticeId = 1L;


        //when, then
        Assertions.assertThatThrownBy(() -> noticeService.deleteNotice(deleteNoticeId))
                .isInstanceOf(NoticeException.class)
                .hasMessageContaining("해당 공지사항을 찾을 수 없음");




    }

    private static Notice createNotice() {
        Notice notice = Notice.builder()
                .title("title")
                .content("content")
                .build();
        return notice;
    }
}