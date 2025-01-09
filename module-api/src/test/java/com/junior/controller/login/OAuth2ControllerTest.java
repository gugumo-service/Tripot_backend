package com.junior.controller.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junior.config.SecurityConfig;
import com.junior.security.WithMockCustomUser;
import com.junior.dto.jwt.RefreshTokenDto;
import com.junior.dto.member.CheckActiveMemberDto;
import com.junior.dto.oauth2.OAuth2Provider;
import com.junior.security.JwtUtil;
import com.junior.security.exceptionhandler.CustomAuthenticationEntryPoint;
import com.junior.service.member.OAuth2Service;
import com.junior.service.security.UserDetailsServiceImpl;
import com.junior.util.RedisUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OAuth2Controller.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(SecurityConfig.class)
class OAuth2ControllerTest {

    @MockBean
    private RedisUtil redisUtil;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @MockBean
    OAuth2Service oAuth2Service;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("소셜 로그인 응답이 정상적으로 반환되어야 함")
    @WithMockCustomUser
    void oauth2Login() throws Exception {

        //given
        MockHttpServletResponse response = new MockHttpServletResponse();
        String code = "code";
        String kakaoProvider = "kakao";

        CheckActiveMemberDto checkActiveMemberDto = new CheckActiveMemberDto("nickname", false);

        //argumentmatcher는 메서드의 모든 파라미터에 쓰거나, 안 써야 함
        given(oAuth2Service.oauth2Login(any(HttpServletResponse.class), anyString(), any(OAuth2Provider.class))).willReturn(checkActiveMemberDto);

        //when
        ResultActions actions = mockMvc.perform(
                post("/api/v1/login/oauth2/{provider}", kakaoProvider)
                        .queryParam("code", code)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customCode").value("MEMBER-SUCCESS-004"))
                .andExpect(jsonPath("$.customMessage").value("소셜 로그인 성공"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.nickname").value("nickname"))
                .andExpect(jsonPath("$.data.isActivate").value(false));


    }

    @Test
    @DisplayName("로그아웃 응답을 성공적으로 반환해야 함")
    @WithMockCustomUser
    void logout() throws Exception {

        //given
        String refreshtoken = "refreshtoken";
        RefreshTokenDto refreshTokenDto = new RefreshTokenDto(refreshtoken);
        String content = objectMapper.writeValueAsString(refreshTokenDto);

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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customCode").value("JWT-SUCCESS-002"))
                .andExpect(jsonPath("$.customMessage").value("로그아웃 완료"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }
}