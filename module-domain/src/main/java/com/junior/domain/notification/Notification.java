package com.junior.domain.notification;

import com.junior.domain.base.BaseEntity;
import com.junior.dto.notification.CreateNotificationDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    // 알림 받는 멤버 정보
    private Long memberId;

    // 알림 발생시킨 멤버 정보
    private String profileImgPath;

    // 알링 발생시킨 스토리 정보
    private Long storyId;

    private String content;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    // 읽음 유무
    @Builder.Default
    private Boolean isRead = false;

    public static Notification from(CreateNotificationDto createNotificationDto) {
        return Notification.builder()
                .content(createNotificationDto.content())
                .profileImgPath(createNotificationDto.profileImgPath())
                .memberId(createNotificationDto.memberId())
                .storyId(createNotificationDto.storyId())
                .notificationType(createNotificationDto.notificationType())
                .build();
    }

    public void readNotification() {
        this.isRead = true;
    }
}