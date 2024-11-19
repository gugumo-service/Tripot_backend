package com.junior.service.admin;

import com.junior.domain.admin.Notice;
import com.junior.dto.admin.notice.CreateNoticeDto;
import com.junior.dto.admin.notice.UpdateNoticeDto;
import com.junior.exception.NoticeException;
import com.junior.exception.StatusCode;
import com.junior.repository.admin.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional
    public void saveNotice(CreateNoticeDto createNoticeDto) {

        Notice notice = Notice.builder()
                .title(createNoticeDto.title())
                .content(createNoticeDto.content())
                .build();

        noticeRepository.save(notice);

    }

    @Transactional
    public void updateNotice(Long updateNoticeId, UpdateNoticeDto updateNoticeDto) {

        Notice notice = noticeRepository.findById(updateNoticeId)
                .orElseThrow(() -> new NoticeException(StatusCode.NOTICE_NOT_FOUND));

        notice.update(updateNoticeDto);


    }



    @Transactional
    public void deleteNotice(Long noticeId) {

        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoticeException(StatusCode.NOTICE_NOT_FOUND));

        notice.softDelete();


    }
}
