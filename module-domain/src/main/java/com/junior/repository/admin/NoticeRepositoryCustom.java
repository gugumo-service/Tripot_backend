package com.junior.repository.admin;

import com.junior.dto.admin.notice.NoticeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NoticeRepositoryCustom {
    public Page<NoticeDto> findNotice(String q, Pageable pageable);
}
