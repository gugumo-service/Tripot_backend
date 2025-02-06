package com.junior.controller.qna;

import com.junior.controller.api.QnaAdminApi;
import com.junior.dto.qna.CreateQnaDto;
import com.junior.dto.qna.QnaAdminDto;
import com.junior.dto.qna.QnaDetailDto;
import com.junior.dto.qna.UpdateQnaDto;
import com.junior.exception.StatusCode;
import com.junior.page.PageCustom;
import com.junior.response.CommonResponse;
import com.junior.security.UserPrincipal;
import com.junior.service.qna.QnaAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class QnaAdminController implements QnaAdminApi {

    private final QnaAdminService qnaAdminService;

    /**
     * Q&A 작성 기능
     * @param createQnaDto
     * @return Q&A 업로드 성공
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/admin/qna")
    public CommonResponse<Object> saveQna(@AuthenticationPrincipal UserPrincipal principal, @RequestBody CreateQnaDto createQnaDto) {

        qnaAdminService.saveQna(principal, createQnaDto);

        return CommonResponse.success(StatusCode.QNA_CREATE_SUCCESS, null);
    }

    /**
     * Q&A 조회 기능
     * @param pageable  (page: 조회하고자 하는 페이지)
     * @param q Q&A 검색어
     * @return Q&A 목록
     */
    @GetMapping("/api/v1/admin/qna")
    public CommonResponse<PageCustom<QnaAdminDto>> findQna(@PageableDefault(size = 15, page = 1) Pageable pageable,
                                                           @RequestParam(required = false, value = "q", defaultValue = "") String q) {


        return CommonResponse.success(StatusCode.QNA_FIND_SUCCESS, qnaAdminService.findQna(q, pageable));
    }

    /**
     * Q&A 내용 조회 기능
     * @param qnaId Q&A id
     * @return 공지사항 내용
     */
    @GetMapping("/api/v1/admin/qna/{qna_id}")
    public CommonResponse<QnaDetailDto> findQnaDetail(@PathVariable("qna_id") Long qnaId) {

        return CommonResponse.success(StatusCode.QNA_FIND_DETAIL_SUCCESS, qnaAdminService.findQnaDetail(qnaId));
    }

    /**
     * Q&A 수정 기능
     * @param qnaId
     * @param updateQnaDto
     * @return Q&A 수정 성공
     */
    @PatchMapping("/api/v1/admin/qna/{qna_id}")
    public CommonResponse<Object> updateQna(@PathVariable(name = "qna_id") Long qnaId, @RequestBody UpdateQnaDto updateQnaDto) {

        qnaAdminService.updateQna(qnaId, updateQnaDto);

        return CommonResponse.success(StatusCode.QNA_UPDATE_SUCCESS, null);
    }

    /**
     * Q&A 삭제 성공
     * @param qnaId
     * @return Q&A 삭제 성공
     */
    @DeleteMapping("/api/v1/admin/qna/{qna_id}")
    public CommonResponse<Object> deleteQna(@PathVariable(name = "qna_id") Long qnaId) {

        qnaAdminService.deleteQna(qnaId);

        return CommonResponse.success(StatusCode.QNA_DELETE_SUCCESS, null);
    }


}

