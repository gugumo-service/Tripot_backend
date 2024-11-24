package com.junior.repository.notice;

import com.junior.dto.notice.NoticeAdminDto;
import com.junior.dto.notice.NoticeUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {
    public Page<NoticeAdminDto> findNotice(String q, Pageable pageable);

}
