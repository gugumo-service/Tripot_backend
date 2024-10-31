package com.junior.controller;

import com.junior.dto.member.CheckActiveMemberDto;
import com.junior.dto.oauth2.OAuth2Provider;
import com.junior.exception.StatusCode;
import com.junior.response.CommonResponse;
import com.junior.service.OAuth2Service;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;

    @PostMapping("/api/v1/login/oauth2/{provider}")
    public CommonResponse<CheckActiveMemberDto> oauth2Login(HttpServletResponse response, @RequestParam("code") String code, @PathVariable("provider") String provider) {


        return CommonResponse.success(StatusCode.OAUTH2_LOGIN_SUCCESS, oAuth2Service.oauth2Login(response, code, OAuth2Provider.valueOf(provider.toUpperCase())));

    }
}
