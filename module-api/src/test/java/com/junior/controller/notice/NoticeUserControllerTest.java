package com.junior.controller.notice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junior.config.SecurityConfig;
import com.junior.dto.notice.NoticeUserDto;
import com.junior.security.JwtUtil;
import com.junior.security.WithMockCustomUser;
import com.junior.security.exceptionhandler.CustomAuthenticationEntryPoint;
import com.junior.service.notice.NoticeUserService;
import com.junior.service.security.UserDetailsServiceImpl;
import com.junior.util.RedisUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(NoticeUserController.class)
@MockBean(JpaMetamodelMappingContext.class)     //JPA 관련 빈들을 mock으로 등록
@Import(SecurityConfig.class)
class NoticeUserControllerTest {

    @MockBean
    private RedisUtil redisUtil;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NoticeUserService noticeUserService;

    @InjectMocks
    private NoticeUserController noticeUserController;


    @Test
    @DisplayName("사용자 공지 조회 응답이 반환되어야 함")
    @WithMockCustomUser
    void findNotice() throws Exception {

        //given

        Long cursorId = 1L;
        Integer size = 5;

        PageRequest pageable = PageRequest.of(0, size);

        List<NoticeUserDto> result = new ArrayList<>();

        result.add(new NoticeUserDto(1L, "title", "content", LocalDateTime.MIN));

        given(noticeUserService.findNotice(cursorId, size)).willReturn(new SliceImpl<>(result, pageable, false));

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/v1/notices")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("cursorId", cursorId.toString())
                        .param("size", size.toString())
        );


        //then
        actions
                .andDo(print())
                .andExpect(jsonPath("$.customCode").value("NOTICE-SUCCESS-004"))
                .andExpect(jsonPath("$.customMessage").value("공지사항 조회 성공"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.data.pageable.pageSize").value(5))
                .andExpect(jsonPath("$.data.content[0].title").value("title"));


    }
}