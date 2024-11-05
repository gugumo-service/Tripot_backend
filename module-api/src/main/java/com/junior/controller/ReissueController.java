package com.junior.controller;

import com.junior.dto.jwt.ReissueDto;
import com.junior.exception.StatusCode;
import com.junior.response.CommonResponse;
import com.junior.service.ReissueService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.junior.exception.StatusCode.REISSUE_SUCCESS;

@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final ReissueService reissueService;

    /**
     * JWT 재발급 기능
     * @param reissueDto: 재발급을 위한 refresh 토큰이 담긴 body
     * @param response
     * @return header: access, refresh token
     *         body: JWT 재발급 완료
     */
    @PostMapping("/api/v1/reissue")
    public CommonResponse<String> reissue(@RequestBody ReissueDto reissueDto, HttpServletResponse response) {

        reissueService.reissue(reissueDto, response);

        return CommonResponse.success(REISSUE_SUCCESS, null);

    }

}
