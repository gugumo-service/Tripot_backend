package com.junior.controller.login;

import com.junior.controller.api.ReissueApi;
import com.junior.dto.jwt.RefreshTokenDto;
import com.junior.response.CommonResponse;
import com.junior.service.member.ReissueService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.junior.exception.StatusCode.REISSUE_SUCCESS;

@RestController
@RequiredArgsConstructor
public class ReissueController implements ReissueApi {

    private final ReissueService reissueService;

    /**
     * JWT 재발급 기능
     * @param refreshTokenDto: 재발급을 위한 refresh 토큰이 담긴 body
     * @param response
     * @return header: access, refresh token
     *         body: JWT 재발급 완료
     */
    @PostMapping("/api/v1/reissue")
    public CommonResponse<String> reissue(@RequestBody RefreshTokenDto refreshTokenDto, HttpServletResponse response) {

        reissueService.reissue(refreshTokenDto, response);

        return CommonResponse.success(REISSUE_SUCCESS, null);

    }

}
