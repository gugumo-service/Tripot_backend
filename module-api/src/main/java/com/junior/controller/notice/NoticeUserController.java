package com.junior.controller.notice;

import com.junior.controller.api.NoticeUserApi;
import com.junior.dto.notice.NoticeUserDto;
import com.junior.exception.StatusCode;
import com.junior.response.CommonResponse;
import com.junior.service.notice.NoticeUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NoticeUserController implements NoticeUserApi {

    private final NoticeUserService noticeUserService;

    @GetMapping("/api/v1/notices")
    public CommonResponse<Slice<NoticeUserDto>> findNotice(
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam("size") int size
    ) {

        return CommonResponse.success(StatusCode.NOTICE_FIND_SUCCESS, noticeUserService.findNotice(cursorId, size));
    }
}
