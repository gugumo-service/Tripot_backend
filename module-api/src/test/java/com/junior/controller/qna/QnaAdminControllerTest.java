package com.junior.controller.qna;

import com.junior.controller.BaseControllerTest;
import com.junior.dto.qna.CreateQnaDto;
import com.junior.dto.qna.QnaAdminDto;
import com.junior.dto.qna.QnaDetailDto;
import com.junior.dto.qna.UpdateQnaDto;
import com.junior.page.PageCustom;
import com.junior.security.WithMockCustomAdmin;
import com.junior.service.qna.QnaAdminService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(QnaAdminController.class)
class QnaAdminControllerTest extends BaseControllerTest {

    @MockBean
    private QnaAdminService qnaAdminService;

    @Test
    @DisplayName("Q&A 저장 - 응답이 반환되어야 함")
    @WithMockCustomAdmin
    void saveQna() throws Exception {

        //given
        CreateQnaDto createQnaDto = new CreateQnaDto("question", "answer");
        String content = objectMapper.writeValueAsString(createQnaDto);

        //when
        ResultActions actions = mockMvc.perform(
                post("/api/v1/admin/qna")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );


        //then
        actions
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customCode").value("Q&A-SUCCESS-001"))
                .andExpect(jsonPath("$.customMessage").value("Q&A 업로드 성공"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data").value(nullValue()));


    }

    @Test
    @DisplayName("Q&A 조회 - 응답이 반환되어야 함")
    @WithMockCustomAdmin
    void findQna() throws Exception {

        //given
        Pageable resultPageable = PageRequest.of(0, 15);
        String q = "";

        List<QnaAdminDto> result = new ArrayList<>();

        result.add(new QnaAdminDto(1L, "question"));


        given(qnaAdminService.findQna(anyString(), any(Pageable.class)))
                .willReturn(new PageCustom<>(result, resultPageable, result.size()));

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/v1/admin/qna")
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customCode").value("Q&A-SUCCESS-004"))
                .andExpect(jsonPath("$.customMessage").value("Q&A 조회 성공"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.pageable.number").value(1))
                .andExpect(jsonPath("$.data.content[0].question").value("question"));


    }

    @Test
    @DisplayName("Q&A 세부내용 조회 - 응답이 반환되어야 함")
    @WithMockCustomAdmin
    void findQnaDetail() throws Exception {

        //given

        QnaDetailDto qnaDetailDto = QnaDetailDto.builder()
                .id(1L)
                .question("question")
                .answer("answer")
                .build();

        given(qnaAdminService.findQnaDetail(anyLong())).willReturn(qnaDetailDto);

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/v1/admin/qna/{qna_id}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customCode").value("Q&A-SUCCESS-005"))
                .andExpect(jsonPath("$.customMessage").value("Q&A 세부정보 조회 성공"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.question").value("question"))
                .andExpect(jsonPath("$.data.answer").value("answer"));


    }


    @Test
    @DisplayName("Q&A 수정 - 응답이 반환되어야 함")
    @WithMockCustomAdmin
    void updateQna() throws Exception {

        //given
        Long qnaId = 1L;
        UpdateQnaDto updateQnaDto = new UpdateQnaDto("new question", "new answer");
        String content = objectMapper.writeValueAsString(updateQnaDto);

        //when
        ResultActions actions = mockMvc.perform(
                patch("/api/v1/admin/qna/{qna_id}", qnaId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customCode").value("Q&A-SUCCESS-003"))
                .andExpect(jsonPath("$.customMessage").value("Q&A 수정 성공"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    @Test
    @DisplayName("Q&A 삭제 - 응답이 반환되어야 함")
    @WithMockCustomAdmin
    void deleteQna() throws Exception {

        //given
        Long qnaId = 1L;

        //when
        ResultActions actions = mockMvc.perform(
                delete("/api/v1/admin/qna/{qna_id}", qnaId)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customCode").value("Q&A-SUCCESS-002"))
                .andExpect(jsonPath("$.customMessage").value("Q&A 삭제 성공"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }
}