package com.junior.controller.api;

import com.junior.dto.admin.notice.CreateNoticeDto;
import com.junior.dto.admin.notice.UpdateNoticeDto;
import com.junior.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface NoticeApi {

    @Operation(summary = "회원 활성화", description = "회원의 추가정보를 입력받습니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "공지사항 업로드 성공",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                        "customCode": "NOTICE-SUCCESS-001",
                                                        "customMessage": "공지사항 업로드 성공",
                                                        "status": true,
                                                        "data": null
                                                    }
                                                    """
                                    )))
            })
    public CommonResponse<Object> saveNotice(@RequestBody CreateNoticeDto createNoticeDto);


    @Operation(summary = "공지사항 수정", description = "공지사항을 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "공지사항 수정 성공",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                        "customCode": "NOTICE-SUCCESS-003",
                                                        "customMessage": "공지사항 수정 성공",
                                                        "status": true,
                                                        "data": null
                                                    }
                                                    """
                                    ))),
                    @ApiResponse(responseCode = "400", description = "해당 공지사항을 찾을 수 없음",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "customCode": "NOTICE-ERR-001",
                                                                "customMessage": "해당 공지사항을 찾을 수 없음",
                                                                "status": false,
                                                                "data": null
                                                            }
                                                            """
                                            )
                                    }))
            })
    public CommonResponse<Object> updateNotice(@PathVariable(name = "notice_id") Long noticeId, @RequestBody UpdateNoticeDto updateNoticeDto);

    @Operation(summary = "공지사항 삭제", description = "공지사항을 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "공지사항 삭제 성공",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                        "customCode": "NOTICE-SUCCESS-002",
                                                        "customMessage": "공지사항 삭제 성공",
                                                        "status": true,
                                                        "data": null
                                                    }
                                                    """
                                    ))),
                    @ApiResponse(responseCode = "400", description = "해당 공지사항을 찾을 수 없음",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "customCode": "NOTICE-ERR-001",
                                                                "customMessage": "해당 공지사항을 찾을 수 없음",
                                                                "status": false,
                                                                "data": null
                                                            }
                                                            """
                                            )
                                    }))
            })
    public CommonResponse<Object> deleteNotice(@PathVariable(name = "notice_id") Long noticeId);
}
