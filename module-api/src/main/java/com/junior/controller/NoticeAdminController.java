package com.junior.controller;

import com.junior.controller.api.NoticeApi;
import com.junior.dto.admin.notice.CreateNoticeDto;
import com.junior.dto.admin.notice.NoticeDetailDto;
import com.junior.dto.admin.notice.NoticeDto;
import com.junior.dto.admin.notice.UpdateNoticeDto;
import com.junior.exception.StatusCode;
import com.junior.page.PageCustom;
import com.junior.response.CommonResponse;
import com.junior.service.admin.NoticeAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class NoticeAdminController implements NoticeApi {

    private final NoticeAdminService noticeAdminService;

    /**
     * 공지사항 작성 기능
     * @param createNoticeDto
     * @return 공지사항 업로드 성공
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/admin/notices")
    public CommonResponse<Object> saveNotice(@RequestBody CreateNoticeDto createNoticeDto) {

        noticeAdminService.saveNotice(createNoticeDto);

        return CommonResponse.success(StatusCode.NOTICE_CREATE_SUCCESS, null);
    }

    /**
     * 공지사항 조회 기능
     * @param pageable  (page: 조회하고자 하는 페이지)
     * @param q 공지사항 검색어
     * @return 공지사항 목록
     */
    @GetMapping("/api/v1/admin/notices")
    public CommonResponse<PageCustom<NoticeDto>> findNotice(@PageableDefault(size = 15, page = 1) Pageable pageable,
                                                            @RequestParam(required = false, value = "q", defaultValue = "") String q) {

        return CommonResponse.success(StatusCode.NOTICE_FIND_SUCCESS, noticeAdminService.findNotice(q, pageable));
    }

    /**
     * 공지사항 내용 조회 기능
     * @param noticeId 공지사항 id
     * @return 공지사항 내용
     */
    @GetMapping("/api/v1/admin/notices/{notice_id}")
    public CommonResponse<NoticeDetailDto> findNoticeDetail(@PathVariable("notice_id") Long noticeId) {

        return CommonResponse.success(StatusCode.NOTICE_FIND_DETAIL_SUCCESS, noticeAdminService.findNoticeDetail(noticeId));
    }

    /**
     * 공지사항 수정 기능
     * @param noticeId
     * @param updateNoticeDto
     * @return 공지사항 수정 성공
     */
    @PatchMapping("/api/v1/admin/notices/{notice_id}")
    public CommonResponse<Object> updateNotice(@PathVariable(name = "notice_id") Long noticeId, @RequestBody UpdateNoticeDto updateNoticeDto) {

        noticeAdminService.updateNotice(noticeId, updateNoticeDto);

        return CommonResponse.success(StatusCode.NOTICE_UPDATE_SUCCESS, null);
    }

    /**
     * 공지사항 삭제 성공
     * @param noticeId
     * @return 공지사항 삭제 성공
     */
    @DeleteMapping("/api/v1/admin/notices/{notice_id}")
    public CommonResponse<Object> deleteNotice(@PathVariable(name = "notice_id") Long noticeId) {

        noticeAdminService.deleteNotice(noticeId);

        return CommonResponse.success(StatusCode.NOTICE_DELETE_SUCCESS, null);
    }


}
