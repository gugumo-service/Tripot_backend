package com.junior.dto.notification;

import com.junior.domain.notification.NotificationType;
import lombok.Builder;

@Builder
public record CreateNotificationDto(
        String content,
        String profileImgPath,
        Long memberId,
        Long storyId,
        NotificationType notificationType
) {

}
