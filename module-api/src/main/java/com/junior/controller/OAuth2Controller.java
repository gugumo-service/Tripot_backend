package com.junior.controller;

import com.junior.controller.api.OAuth2Api;
import com.junior.dto.jwt.RefreshTokenDto;
import com.junior.dto.member.CheckActiveMemberDto;
import com.junior.dto.oauth2.OAuth2Provider;
import com.junior.exception.StatusCode;
import com.junior.response.CommonResponse;
import com.junior.service.member.OAuth2Service;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "OAuth2")
public class OAuth2Controller implements OAuth2Api {

    private final OAuth2Service oAuth2Service;

    @PostMapping("/api/v1/login/oauth2/{provider}")
    public CommonResponse<CheckActiveMemberDto> oauth2Login(HttpServletResponse response, @RequestParam("code") String code, @PathVariable("provider") String provider) {


        return CommonResponse.success(StatusCode.OAUTH2_LOGIN_SUCCESS, oAuth2Service.oauth2Login(response, code, OAuth2Provider.valueOf(provider.toUpperCase())));

    }

    @PostMapping("/api/v1/logout")
    public CommonResponse<Boolean> logout(@RequestBody RefreshTokenDto refreshTokenDto) {

        oAuth2Service.logout(refreshTokenDto);

        return CommonResponse.success(StatusCode.LOGOUT, null);

    }
}
