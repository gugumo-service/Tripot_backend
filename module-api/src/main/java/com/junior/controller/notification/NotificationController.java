package com.junior.controller.notification;

import com.junior.dto.notification.ResponseNotificationDto;
import com.junior.exception.StatusCode;
import com.junior.response.CommonResponse;
import com.junior.security.UserPrincipal;
import com.junior.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public CommonResponse<Object> getNotificationByMemberId(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam("size") int size
    ) {

        Slice<ResponseNotificationDto> notifications = notificationService.getNotification(userPrincipal, cursorId, size);

        return CommonResponse.success(StatusCode.NOTIFICATION_READ_SUCCESS, notifications);
    }

    @GetMapping("/read/{notificationId}")
    public CommonResponse<Object> readNotification(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable(name = "notificationId") Long notificationId
    ) {
        notificationService.readNotification(notificationId);

        return CommonResponse.success(StatusCode.NOTIFICATION_READ_SUCCESS, null);
    }

    @DeleteMapping("/{notificationId}")
    public CommonResponse<Object> deleteNotification(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable(name = "notificationId") Long notificationId
    ) {
        notificationService.deleteNotification(userPrincipal, notificationId);

        return CommonResponse.success(StatusCode.NOTIFICATION_DELETE_SUCCESS, null);
    }

    @GetMapping("/read/all")
    public CommonResponse<Object> readAllNotification(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        notificationService.readAllNotification(userPrincipal);

        return CommonResponse.success(StatusCode.NOTIFICATION_READ_SUCCESS, null);
    }
}
