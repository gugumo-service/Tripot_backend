package com.junior.service.notification;

import com.junior.domain.member.Member;
import com.junior.domain.notification.Notification;
import com.junior.domain.notification.NotificationType;
import com.junior.dto.notification.CreateNotificationDto;
import com.junior.dto.notification.ResponseNotificationDto;
import com.junior.exception.NotificationNotFoundException;
import com.junior.exception.StatusCode;
import com.junior.repository.notification.NotificationRepository;
import com.junior.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public void saveNotification(Member member, String profilePath, String content, Long storyId, NotificationType notificationType) {

        CreateNotificationDto createNotificationDto = CreateNotificationDto.builder()
                .content(content)
                .profileImgPath(profilePath)
                .memberId(member.getId())
                .storyId(storyId)
                .notificationType(notificationType)
                .build();

        Notification notification = Notification.from(createNotificationDto);

        notificationRepository.save(notification);
    }

    public Slice<ResponseNotificationDto> getNotification(UserPrincipal userPrincipal, Long cursorId, int size) {

        Member findMember = userPrincipal.getMember();

        Pageable pageable = PageRequest.of(0, size);

        return notificationRepository.findAllNotificationByMemberAndIsReadFalse(findMember.getId(), pageable, cursorId);
    }

    @Transactional
    public void readNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(()->new NotificationNotFoundException(StatusCode.NOTIFICATION_NOT_FOUND));
        notification.readNotification();
    }

    @Transactional
    public void readAllNotification(UserPrincipal userPrincipal) {

        Member findMember = userPrincipal.getMember();

        List<Notification> notifications = notificationRepository.findByMemberIdAndIsReadFalse(findMember.getId());
        notifications.forEach(Notification::readNotification);
    }
}
