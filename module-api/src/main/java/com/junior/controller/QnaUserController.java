package com.junior.controller;

import com.junior.controller.api.QnaUserApi;
import com.junior.dto.qna.QnaUserDto;
import com.junior.exception.StatusCode;
import com.junior.response.CommonResponse;
import com.junior.service.qna.QnaUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QnaUserController implements QnaUserApi {

    private final QnaUserService qnaUserService;

    @GetMapping("/api/v1/qna")
    public CommonResponse<Slice<QnaUserDto>> findQna(
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam("size") int size
    ) {

        return CommonResponse.success(StatusCode.QNA_FIND_SUCCESS, qnaUserService.findQna(cursorId, size));
    }

}
