package com.junior.integration.story;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junior.domain.member.Member;
import com.junior.domain.story.Story;
import com.junior.integration.BaseIntegrationTest;
import com.junior.repository.member.MemberRepository;
import com.junior.repository.story.StoryRepository;
import com.junior.security.WithMockCustomAdmin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminStoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StoryRepository storyRepository;

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

        for (int i = 1; i <= 18; i++) {
            Story story = createStory(activeTestMember);

            storyRepository.save(story);
        }
    }

    @Test
    @DisplayName("관리자 스토리 페이지 조회 기능이 정상 동작해야함")
    @WithMockCustomAdmin
    void findStory() throws Exception {

        //given


        //when
        ResultActions actions = mockMvc.perform(
                get("/api/v1/admin/stories")
                        .param("page", "2")
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customCode").value("STORY-SUCCESS-0001"))
                .andExpect(jsonPath("$.customMessage").value("스토리 불러오기 성공"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.pageable.number").value(2))
                .andExpect(jsonPath("$.data.content[0].id").value(3))
                .andExpect(jsonPath("$.data.content[0].isDeleted").value(false))
                .andExpect(jsonPath("$.data.content[0].createdUsername").value("테스트사용자유저네임"));


    }

    @Test
    @DisplayName("관리자 스토리 상세조회 기능이 정상 동작해야함")
    @WithMockCustomAdmin
    void findStoryDetail() throws Exception {

        //given
        String q = "12";


        //when
        ResultActions actions = mockMvc.perform(
                get("/api/v1/admin/stories/{story_id}", 10)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customCode").value("STORY-SUCCESS-0001"))
                .andExpect(jsonPath("$.customMessage").value("스토리 불러오기 성공"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.id").value(10))
                .andExpect(jsonPath("$.data.isDeleted").value(false));


    }


}
