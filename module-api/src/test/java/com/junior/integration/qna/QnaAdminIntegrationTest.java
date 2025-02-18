package com.junior.integration.qna;

import com.junior.domain.admin.Qna;
import com.junior.domain.member.Member;
import com.junior.dto.qna.CreateQnaDto;
import com.junior.dto.qna.UpdateQnaDto;
import com.junior.exception.StatusCode;
import com.junior.integration.BaseIntegrationTest;
import com.junior.repository.member.MemberRepository;
import com.junior.repository.qna.QnaRepository;
import com.junior.security.WithMockCustomAdmin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class QnaAdminIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private QnaRepository qnaRepository;

    @BeforeEach
    void init() throws InterruptedException {
        Member preactiveTestMember = createPreactiveTestMember();
        Member activeTestMember = createActiveTestMember();
        Member testAdmin = createAdmin();
        Member activeTestMember2 = createActiveTestMember2();

        memberRepository.save(preactiveTestMember);
        memberRepository.save(activeTestMember);
        memberRepository.save(testAdmin);
        memberRepository.save(activeTestMember2);

        for (int i = 1; i <= 100; i++) {
            Qna qna = Qna.builder()
                    .question("질문 " + i)
                    .answer("답변 " + i)
                    .member(testAdmin)
                    .build();

            qnaRepository.save(qna);

            Thread.sleep(3);

        }

    }


    @Test
    @DisplayName("Q&A 저장 - 새 Q&A이 정상적으로 저장되어야 함")
    @WithMockCustomAdmin
    void saveQna() throws Exception {

        //given
        CreateQnaDto createQnaDto = new CreateQnaDto("question 101", "answer 101");
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
                .andExpect(jsonPath("$.customCode").value(StatusCode.QNA_CREATE_SUCCESS.getCustomCode()))
                .andExpect(jsonPath("$.customMessage").value(StatusCode.QNA_CREATE_SUCCESS.getCustomMessage()))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data").value(nullValue()));

        Qna newQna = qnaRepository.findById(101L)
                .orElseThrow(RuntimeException::new);

        assertThat(newQna.getQuestion()).isEqualTo("question 101");
        assertThat(newQna.getAnswer()).isEqualTo("answer 101");


    }

    @Test
    @DisplayName("Q&A 조회 - 기능이 정상적으로 동작해야 함")
    @WithMockCustomAdmin
    void findQna() throws Exception {

        //given


        //when
        ResultActions actions = mockMvc.perform(
                get("/api/v1/admin/qna")
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customCode").value(StatusCode.QNA_FIND_SUCCESS.getCustomCode()))
                .andExpect(jsonPath("$.customMessage").value(StatusCode.QNA_FIND_SUCCESS.getCustomMessage()))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.pageable.number").value(1))
                .andExpect(jsonPath("$.data.content[0].question").value("질문 100"));


    }

    @Test
    @DisplayName("Q&A 조회 - 검색 기능이 정상적으로 동작해야 함")
    @WithMockCustomAdmin
    void findNoticeWithKeyword() throws Exception {

        //given


        //when
        ResultActions actions = mockMvc.perform(
                get("/api/v1/admin/qna")
                        .queryParam("q", "31")
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customCode").value(StatusCode.QNA_FIND_SUCCESS.getCustomCode()))
                .andExpect(jsonPath("$.customMessage").value(StatusCode.QNA_FIND_SUCCESS.getCustomMessage()))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.pageable.number").value(1))
                .andExpect(jsonPath("$.data.content[0].question").value("질문 31"));


    }

    @Test
    @DisplayName("Q&A 세부내용 조회 - Q&A 새부내용을 정상적으로 응답해야 함")
    @WithMockCustomAdmin
    void findNoticeDetail() throws Exception {

        //given

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/v1/admin/qna/{qna_id}", 45L)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customCode").value(StatusCode.QNA_FIND_DETAIL_SUCCESS.getCustomCode()))
                .andExpect(jsonPath("$.customMessage").value(StatusCode.QNA_FIND_DETAIL_SUCCESS.getCustomMessage()))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.question").value("질문 45"))
                .andExpect(jsonPath("$.data.answer").value("답변 45"));


    }

    @Test
    @DisplayName("Q&A 수정 - 기능이 정상적으로 동작해야 함")
    @WithMockCustomAdmin
    void updateNotice() throws Exception {

        //given
        Long qnaId = 20L;
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
                .andExpect(jsonPath("$.customCode").value(StatusCode.QNA_UPDATE_SUCCESS.getCustomCode()))
                .andExpect(jsonPath("$.customMessage").value(StatusCode.QNA_UPDATE_SUCCESS.getCustomMessage()))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data").value(nullValue()));

        Qna updatedQna = qnaRepository.findById(qnaId)
                .orElseThrow(RuntimeException::new);

        assertThat(updatedQna.getQuestion()).isEqualTo("new question");
        assertThat(updatedQna.getAnswer()).isEqualTo("new answer");

    }

    @Test
    @DisplayName("Q&A 삭제 - Q&A의 삭제 flag가 true로 수정되어야 함")
    @WithMockCustomAdmin
    void deleteNotice() throws Exception {

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
                .andExpect(jsonPath("$.customCode").value(StatusCode.QNA_DELETE_SUCCESS.getCustomCode()))
                .andExpect(jsonPath("$.customMessage").value(StatusCode.QNA_DELETE_SUCCESS.getCustomMessage()))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data").value(nullValue()));

        Qna deletedQna = qnaRepository.findById(qnaId)
                .orElseThrow(RuntimeException::new);

        assertThat(deletedQna.getIsDeleted()).isTrue();
    }

}
