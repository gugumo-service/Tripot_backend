package com.junior.integration.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junior.controller.member.MemberController;
import com.junior.domain.member.Member;
import com.junior.dto.jwt.LoginCreateJwtDto;
import com.junior.dto.jwt.RefreshTokenDto;
import com.junior.dto.oauth2.OAuth2LoginDto;
import com.junior.exception.StatusCode;
import com.junior.integration.BaseIntegrationTest;
import com.junior.repository.member.MemberRepository;
import com.junior.security.JwtUtil;
import com.junior.security.WithMockCustomUser;
import com.junior.util.RedisUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OAuth2IntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MemberController memberController;

    @Autowired
    private MemberRepository memberRepository;

    @MockBean
    private RedisUtil redisUtil;

    @MockBean
    private JwtUtil jwtUtil;


    @BeforeEach
    void init() {
        Member preactiveTestMember = createPreactiveTestMember();
        Member activeTestMember = createActiveTestMember("KAKAO 1234");

        memberRepository.save(preactiveTestMember);
        memberRepository.save(activeTestMember);

    }

    @Test
    @DisplayName("소셜 로그인 - 결과가 정상적으로 리턴되어야 함")
    public void oauth2LoginV2() throws Exception {
        //given
        String sampleAccess = "sample_access_token";
        String sampleRefresh = "sample_refresh_token";
        String kakaoProvider = "kakao";

        OAuth2LoginDto oAuth2LoginDto = OAuth2LoginDto.builder()
                .id(1234L)
                .nickname("nickname")
                .build();

        given(jwtUtil.createJwt(any(LoginCreateJwtDto.class), eq("access"))).willReturn(sampleAccess);
        given(jwtUtil.createJwt(any(LoginCreateJwtDto.class), eq("refresh"))).willReturn(sampleRefresh);

        String content = objectMapper.writeValueAsString(oAuth2LoginDto);

        //when
        ResultActions actions = mockMvc.perform(
                post("/api/v2/login/oauth2/{provider}", kakaoProvider)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customCode").value(StatusCode.OAUTH2_LOGIN_SUCCESS.getCustomCode()))
                .andExpect(jsonPath("$.customMessage").value(StatusCode.OAUTH2_LOGIN_SUCCESS.getCustomMessage()))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.nickname").value("테스트사용자닉네임"))
                .andExpect(jsonPath("$.data.isActivate").value(true));

        verify(redisUtil).setDataExpire(anyString(), anyString(), anyLong());

    }

    @Test
    @DisplayName("로그아웃 - 결과가 정상적으로 리턴되어야 함")
    @WithMockCustomUser
    public void logout() throws Exception {
        //given
        String sampleRefresh = "Bearer sample_refresh_token";

        RefreshTokenDto refreshTokenDto = new RefreshTokenDto(sampleRefresh);
        String content = objectMapper.writeValueAsString(refreshTokenDto);

        given(jwtUtil.getCategory(anyString())).willReturn("refresh");

        //when
        ResultActions actions = mockMvc.perform(
                post("/api/v1/logout")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        //then
        actions
                .andDo(print())
                .andExpect(jsonPath("$.customCode").value(StatusCode.LOGOUT.getCustomCode()))
                .andExpect(jsonPath("$.customMessage").value(StatusCode.LOGOUT.getCustomMessage()))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data").value(nullValue()));

        verify(redisUtil).deleteData("sample_refresh_token");

    }
}
