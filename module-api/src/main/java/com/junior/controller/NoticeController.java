package com.junior.controller;

import com.junior.controller.api.NoticeApi;
import com.junior.dto.admin.notice.CreateNoticeDto;
import com.junior.dto.admin.notice.UpdateNoticeDto;
import com.junior.exception.StatusCode;
import com.junior.response.CommonResponse;
import com.junior.service.admin.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class NoticeController implements NoticeApi {

    private final NoticeService noticeService;

    /**
     * 공지사항 작성 기능
     * @param createNoticeDto
     * @return 공지사항 업로드 성공
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/admin/notice")
    public CommonResponse<Object> saveNotice(@RequestBody CreateNoticeDto createNoticeDto) {

        noticeService.saveNotice(createNoticeDto);

        return CommonResponse.success(StatusCode.NOTICE_CREATE_SUCCESS, null);
    }

    /**
     * 공지사항 수정 기능
     * @param noticeId
     * @param updateNoticeDto
     * @return 공지사항 수정 성공
     */
    @PatchMapping("/api/v1/admin/notice/{notice_id}")
    public CommonResponse<Object> updateNotice(@PathVariable(name = "notice_id") Long noticeId, @RequestBody UpdateNoticeDto updateNoticeDto) {

        noticeService.updateNotice(noticeId, updateNoticeDto);

        return CommonResponse.success(StatusCode.NOTICE_UPDATE_SUCCESS, null);
    }

    /**
     * 공지사항 삭제 성공
     * @param noticeId
     * @return 공지사항 삭제 성공
     */
    @DeleteMapping("/api/v1/admin/notice/{notice_id}")
    public CommonResponse<Object> deleteNotice(@PathVariable(name = "notice_id") Long noticeId) {

        noticeService.deleteNotice(noticeId);

        return CommonResponse.success(StatusCode.NOTICE_DELETE_SUCCESS, null);
    }


}
