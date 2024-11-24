package com.junior.service.notice;

import com.junior.dto.notice.NoticeUserDto;
import com.junior.repository.notice.NoticeRepository;
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
public class NoticeUserService {

    private final NoticeRepository noticeRepository;

    public Slice<NoticeUserDto> findNotice(Long cursorId, int size) {

        Pageable pageable = PageRequest.of(0, size);

        log.info("[{}] 공지사항 유저 조회", Thread.currentThread().getStackTrace()[1].getMethodName());
        return noticeRepository.findNotice(cursorId, pageable);

    }


}
