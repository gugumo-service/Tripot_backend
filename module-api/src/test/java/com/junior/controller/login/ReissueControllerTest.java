package com.junior.controller.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junior.config.SecurityConfig;
import com.junior.security.WithMockCustomUser;
import com.junior.dto.jwt.RefreshTokenDto;
import com.junior.security.JwtUtil;
import com.junior.security.exceptionhandler.CustomAuthenticationEntryPoint;
import com.junior.service.member.ReissueService;
import com.junior.service.security.UserDetailsServiceImpl;
import com.junior.util.RedisUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReissueController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(SecurityConfig.class)
class ReissueControllerTest {

    @MockBean
    private RedisUtil redisUtil;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private ReissueService reissueService;

    @Test
    @DisplayName("reissue 기능에 대한 응답을 리턴해야 함")
    @WithMockCustomUser
    void reissue() throws Exception {

        //given

        RefreshTokenDto refreshTokenDto = new RefreshTokenDto("Bearer sample_token");

        String content = objectMapper.writeValueAsString(refreshTokenDto);

        //when
        ResultActions actions = mockMvc.perform(
                post("/api/v1/reissue")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customCode").value("JWT-SUCCESS-001"))
                .andExpect(jsonPath("$.customMessage").value("JWT 재발급 완료"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data").value(nullValue()));

    }
}