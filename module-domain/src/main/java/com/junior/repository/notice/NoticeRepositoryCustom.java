package com.junior.repository.notice;

import com.junior.dto.admin.notice.NoticeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {
    public Page<NoticeDto> findNotice(String q, Pageable pageable);
}
