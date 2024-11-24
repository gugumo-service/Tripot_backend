package com.junior.repository.notice;

import com.junior.dto.notice.NoticeAdminDto;
import com.junior.dto.notice.NoticeUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface NoticeRepositoryCustom {
    public Page<NoticeAdminDto> findNotice(String q, Pageable pageable);

    public Slice<NoticeUserDto> findNotice(Long cursorId, Pageable pageable);

}
