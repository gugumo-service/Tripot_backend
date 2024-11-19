package com.junior.controller;

import com.junior.dto.admin.notice.CreateNoticeDto;
import com.junior.dto.admin.notice.UpdateNoticeDto;
import com.junior.exception.StatusCode;
import com.junior.response.CommonResponse;
import com.junior.service.admin.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;


    @PostMapping("/api/v1/admin/notice")
    public CommonResponse<Object> saveNotice(@RequestBody CreateNoticeDto createNoticeDto) {

        noticeService.saveNotice(createNoticeDto);

        return CommonResponse.success(StatusCode.NOTICE_CREATE_SUCCESS, null);
    }

    @PatchMapping("/api/v1/admin/notice/{notice_id}")
    public CommonResponse<Object> updateNotice(@PathVariable(name = "notice_id") Long noticeId, @RequestBody UpdateNoticeDto updateNoticeDto) {

        noticeService.updateNotice(noticeId, updateNoticeDto);

        return CommonResponse.success(StatusCode.NOTICE_UPDATE_SUCCESS, null);
    }

    @DeleteMapping("/api/v1/admin/notice/{notice_id}")
    public CommonResponse<Object> deleteNotice(@PathVariable(name = "notice_id") Long noticeId) {

        noticeService.deleteNotice(noticeId);

        return CommonResponse.success(StatusCode.NOTICE_DELETE_SUCCESS, null);
    }


}
