package com.junior.repository.qna;

import com.junior.dto.qna.QnaAdminDto;
import com.junior.dto.qna.QnaUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface QnaRepositoryCustom {
    public Page<QnaAdminDto> findQna(String q, Pageable pageable);

    public Slice<QnaUserDto> findQna(Long cursorId, Pageable pageable);

}
