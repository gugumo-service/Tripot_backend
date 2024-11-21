package com.junior.service.admin;

import com.junior.domain.admin.Notice;
import com.junior.dto.admin.notice.CreateNoticeDto;
import com.junior.dto.admin.notice.NoticeDto;
import com.junior.dto.admin.notice.UpdateNoticeDto;
import com.junior.exception.NoticeException;
import com.junior.exception.StatusCode;
import com.junior.page.PageCustom;
import com.junior.repository.admin.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Slf4j
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

        log.info("[{}] 공지사항 저장 - id: {}", Thread.currentThread().getStackTrace()[1].getMethodName(), notice.getId());
        noticeRepository.save(notice);

    }


    public PageCustom<NoticeDto> findNotice(String q, Pageable pageable) {


        Page<NoticeDto> page = noticeRepository.findNotice(q, pageable);

        log.info("[{}] 공지사항 조회 결과 리턴 page: {}", Thread.currentThread().getStackTrace()[1].getMethodName(), pageable.getPageNumber() + 1);
        return new PageCustom<>(page.getContent(), page.getPageable(), page.getTotalElements());

    }


    @Transactional
    public void updateNotice(Long updateNoticeId, UpdateNoticeDto updateNoticeDto) {

        Notice notice = noticeRepository.findById(updateNoticeId)
                .orElseThrow(() -> new NoticeException(StatusCode.NOTICE_NOT_FOUND));

        log.info("[{}] 공지사항 수정 내용 - id: {}", Thread.currentThread().getStackTrace()[1].getMethodName(), updateNoticeId);
        notice.update(updateNoticeDto);


    }



    @Transactional
    public void deleteNotice(Long noticeId) {

        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoticeException(StatusCode.NOTICE_NOT_FOUND));

        log.info("[{}] 공지사항 삭제 - id: {}", Thread.currentThread().getStackTrace()[1].getMethodName(), noticeId);
        notice.softDelete();


    }
}
