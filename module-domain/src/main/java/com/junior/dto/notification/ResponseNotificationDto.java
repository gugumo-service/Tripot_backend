package com.junior.dto.notification;

import com.junior.domain.notification.NotificationType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ResponseNotificationDto(
        Long id,
        Long storyId,
        String content,
        String profileImgPath,
        Long memberId,
        Boolean isRead,
        LocalDateTime createdAt,
        NotificationType notificationType
) {
}
