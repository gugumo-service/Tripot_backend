package com.junior.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junior.config.SecurityConfig;
import com.junior.controller.report.ReportController;
import com.junior.dto.member.ActivateMemberDto;
import com.junior.dto.report.CreateReportDto;
import com.junior.security.JwtUtil;
import com.junior.security.WithMockCustomUser;
import com.junior.security.exceptionhandler.CustomAuthenticationEntryPoint;
import com.junior.service.member.MemberService;
import com.junior.service.report.ReportService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportController.class)
@MockBean(JpaMetamodelMappingContext.class)     //JPA 관련 빈들을 mock으로 등록
@Import(SecurityConfig.class)
public class ReportControllerTest {

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

    @MockBean
    ReportService reportService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("신고 응답이 반환되어야 함")
    @WithMockCustomUser
    void saveReport() throws Exception {

        //given
        CreateReportDto createReportDto = new CreateReportDto(1L, "STORY");

        String content = objectMapper.writeValueAsString(createReportDto);

        //when
        ResultActions actions = mockMvc.perform(
                post("/api/v1/reports")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );


        //then
        actions
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customCode").value("REPORT-SUCCESS-001"))
                .andExpect(jsonPath("$.customMessage").value("신고 성공"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }
}
