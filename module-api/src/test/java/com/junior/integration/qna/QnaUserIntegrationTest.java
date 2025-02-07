package com.junior.integration.qna;

import com.junior.domain.admin.Notice;
import com.junior.domain.admin.Qna;
import com.junior.domain.member.Member;
import com.junior.exception.StatusCode;
import com.junior.integration.BaseIntegrationTest;
import com.junior.repository.member.MemberRepository;
import com.junior.repository.qna.QnaRepository;
import com.junior.security.WithMockCustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class QnaUserIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private QnaRepository qnaRepository;

    @BeforeEach
    void init() {
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

        }

    }

    @Test
    @DisplayName("사용자 Q&A 조회 - Q&A 리스트가 정상적으로 반환되어야 함")
    @WithMockCustomUser
    void findQna() throws Exception {

        //given

        Integer size = 5;


        //when
        ResultActions actions = mockMvc.perform(
                get("/api/v1/qna")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("size", size.toString())
        );


        //then
        actions
                .andDo(print())
                .andExpect(jsonPath("$.customCode").value(StatusCode.QNA_FIND_SUCCESS.getCustomCode()))
                .andExpect(jsonPath("$.customMessage").value(StatusCode.QNA_FIND_SUCCESS.getCustomMessage()))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.pageable.pageSize").value(5))
                .andExpect(jsonPath("$.data.content[0].question").value("질문 100"))
                .andExpect(jsonPath("$.data.content[0].answer").value("답변 100"));


    }

    @Test
    @DisplayName("사용자 Q&A 조회 - 커서의 다음에 해당하는 Q&A부터 응답되어야 함")
    @WithMockCustomUser
    void findQnaWithCursor() throws Exception {

        //given

        Long cursorId = 90L;
        Integer size = 5;


        //when
        ResultActions actions = mockMvc.perform(
                get("/api/v1/qna")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("cursorId", cursorId.toString())
                        .param("size", size.toString())
        );


        //then
        actions
                .andDo(print())
                .andExpect(jsonPath("$.customCode").value(StatusCode.QNA_FIND_SUCCESS.getCustomCode()))
                .andExpect(jsonPath("$.customMessage").value(StatusCode.QNA_FIND_SUCCESS.getCustomMessage()))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.pageable.pageSize").value(5))
                .andExpect(jsonPath("$.data.content[0].question").value("질문 89"))
                .andExpect(jsonPath("$.data.content[0].answer").value("답변 89"));


    }

    @Test
    @DisplayName("사용자 Q&A 조회 - 마지막 슬라이스에 해당하는 Q&A가 정상적으로 응답되어야 함")
    @WithMockCustomUser
    void findNoticeLast() throws Exception {

        //given

        Long cursorId = 3L;
        Integer size = 5;


        //when
        ResultActions actions = mockMvc.perform(
                get("/api/v1/qna")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("cursorId", cursorId.toString())
                        .param("size", size.toString())
        );


        //then
        actions
                .andDo(print())
                .andExpect(jsonPath("$.customCode").value(StatusCode.QNA_FIND_SUCCESS.getCustomCode()))
                .andExpect(jsonPath("$.customMessage").value(StatusCode.QNA_FIND_SUCCESS.getCustomMessage()))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.pageable.pageSize").value(5))
                .andExpect(jsonPath("$.data.last").value(true))
                .andExpect(jsonPath("$.data.content[0].question").value("질문 2"))
                .andExpect(jsonPath("$.data.content[0].answer").value("답변 2"));


    }
}
