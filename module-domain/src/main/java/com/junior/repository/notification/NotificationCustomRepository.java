package com.junior.repository.notification;

import com.junior.dto.notification.ResponseNotificationDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface NotificationCustomRepository {
    public Slice<ResponseNotificationDto> findAllNotificationByMemberAndIsReadFalse(Long memberId, Pageable pageable, Long cursorId);
}
