package com.junior.controller.api;

import com.junior.dto.jwt.RefreshTokenDto;
import com.junior.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface ReissueApi {

    @Operation(summary = "JWT Reissue", description = "토큰을 재발급합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "댓글 등록 완료",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                        "customCode": "JWT-SUCCESS-001",
                                                        "customMessage": "JWT 재발급 완료",
                                                        "status": true,
                                                        "data": null
                                                    }
                                                    """
                                    ))),
                    @ApiResponse(responseCode = "401", description = "인증 실패",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class),
                                    examples = {
                                            @ExampleObject(name = "Refresh token이 아님",
                                                    value = """
                                                    {
                                                        "customCode": "JWT-ERR-003",
                                                        "customMessage": "Refresh token이 아님",
                                                        "status": false,
                                                        "data": null
                                                    }
                                                    """
                                            ),
                                            @ExampleObject(name = "만료된 Refresh token",
                                                    value = """
                                                    {
                                                        "customCode": "JWT-ERR-004",
                                                        "customMessage": "만료된 Refresh token",
                                                        "status": false,
                                                        "data": null
                                                    }
                                                    """
                                            )
                                    })),
                    @ApiResponse(responseCode = "400", description = "존재하지 않는 토큰",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                        "customCode": "JWT-ERR-002",
                                                        "customMessage": "존재하지 않는 토큰",
                                                        "status": false,
                                                        "data": null
                                                    }
                                                    """
                                    )))
            })
    public CommonResponse<String> reissue(@RequestBody RefreshTokenDto refreshTokenDto, HttpServletResponse response);
}
