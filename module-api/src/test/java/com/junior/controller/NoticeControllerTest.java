package com.junior.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junior.controller.security.WithMockCustomAdmin;
import com.junior.controller.security.WithMockCustomUser;
import com.junior.dto.admin.notice.CreateNoticeDto;
import com.junior.dto.admin.notice.NoticeDetailDto;
import com.junior.dto.admin.notice.NoticeDto;
import com.junior.dto.admin.notice.UpdateNoticeDto;
import com.junior.page.PageCustom;
import com.junior.service.admin.NoticeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(NoticeController.class)
@MockBean(JpaMetamodelMappingContext.class)     //JPA 관련 빈들을 mock으로 등록
class NoticeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NoticeService noticeService;

    @InjectMocks
    private NoticeController noticeController;

    @Test
    @DisplayName("공지 저장 응답이 반환되어야 함")
    @WithMockCustomAdmin
    void saveNotice() throws Exception {

        //given
        CreateNoticeDto createNoticeDto = new CreateNoticeDto("title", "content");
        String content = objectMapper.writeValueAsString(createNoticeDto);

        //when
        ResultActions actions = mockMvc.perform(
                post("/api/v1/admin/notices")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .with(csrf())
        );


        //then
        actions
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customCode").value("NOTICE-SUCCESS-001"))
                .andExpect(jsonPath("$.customMessage").value("공지사항 업로드 성공"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data").value(nullValue()));


    }

    @Test
    @DisplayName("공지 조회 응답이 반환되어야 함")
    @WithMockCustomAdmin
    void findNotice() throws Exception {

        //given
        Pageable resultPageable = PageRequest.of(0, 15);
        String q = "";

        List<NoticeDto> result = new ArrayList<>();

        result.add(new NoticeDto(1L, "title"));


        given(noticeService.findNotice(anyString(), any(Pageable.class))).willReturn(new PageCustom<>(result, resultPageable, result.size()));

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/v1/admin/notices")
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customCode").value("NOTICE-SUCCESS-004"))
                .andExpect(jsonPath("$.customMessage").value("공지사항 조회 성공"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.pageable.number").value(1))
                .andExpect(jsonPath("$.data.content[0].title").value("title"));




    }

    @Test
    @DisplayName("공지 세부내용 조회 응답이 반환되어야 함")
    @WithMockCustomAdmin
    void findNoticeDetail() throws Exception {

        //given

        NoticeDetailDto noticeDetailDto = NoticeDetailDto.builder()
                .title("title")
                .content("content")
                .authorNick("nickname")
                .build();

        given(noticeService.findNoticeDetail(anyLong())).willReturn(noticeDetailDto);

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/v1/admin/notices/{notice_id}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customCode").value("NOTICE-SUCCESS-005"))
                .andExpect(jsonPath("$.customMessage").value("공지사항 세부정보 조회 성공"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.title").value("title"))
                .andExpect(jsonPath("$.data.content").value("content"))
                .andExpect(jsonPath("$.data.authorNick").value("nickname"));





    }



    @Test
    @DisplayName("공지 수정 응답이 반환되어야 함")
    @WithMockCustomAdmin
    void updateNotice() throws Exception {

        //given
        Long noticeId = 1L;
        UpdateNoticeDto updateNoticeDto = new UpdateNoticeDto("new title", "new content");
        String content = objectMapper.writeValueAsString(updateNoticeDto);

        //when
        ResultActions actions = mockMvc.perform(
                patch("/api/v1/admin/notices/{notice_id}", noticeId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .with(csrf())
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customCode").value("NOTICE-SUCCESS-003"))
                .andExpect(jsonPath("$.customMessage").value("공지사항 수정 성공"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    @Test
    @DisplayName("공지사항 삭제 응답이 반환되어야 함")
    @WithMockCustomAdmin
    void deleteNotice() throws Exception {

        //given
        Long noticeId = 1L;

        //when
        ResultActions actions = mockMvc.perform(
                delete("/api/v1/admin/notices/{notice_id}", noticeId)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customCode").value("NOTICE-SUCCESS-002"))
                .andExpect(jsonPath("$.customMessage").value("공지사항 삭제 성공"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }
}