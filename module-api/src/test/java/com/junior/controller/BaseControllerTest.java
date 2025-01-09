package com.junior.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junior.config.SecurityConfig;
import com.junior.security.JwtUtil;
import com.junior.security.exceptionhandler.CustomAuthenticationEntryPoint;
import com.junior.service.notice.NoticeUserService;
import com.junior.service.security.UserDetailsServiceImpl;
import com.junior.util.RedisUtil;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 반드시 해당 컨트톨러 유닛 테스트에 다음의 애노테이션을 붙일 것
 * @WebMvcTest(SampleController.class)
 */
@MockBean(JpaMetamodelMappingContext.class)     //JPA 관련 빈들을 mock으로 등록
@Import(SecurityConfig.class)
public class BaseControllerTest {

    @MockBean
    protected RedisUtil redisUtil;

    @MockBean
    protected JwtUtil jwtUtil;

    @MockBean
    protected UserDetailsServiceImpl userDetailsService;

    @MockBean
    protected CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

}
