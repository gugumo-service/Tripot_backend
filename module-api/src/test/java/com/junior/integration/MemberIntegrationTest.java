package com.junior.integration;

import com.amazonaws.services.s3.AmazonS3Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junior.controller.MemberController;
import com.junior.domain.member.Member;
import com.junior.domain.member.MemberStatus;
import com.junior.dto.member.ActivateMemberDto;
import com.junior.dto.member.MemberInfoDto;
import com.junior.dto.member.UpdateNicknameDto;
import com.junior.repository.member.MemberRepository;
import com.junior.security.UserPrincipal;
import com.junior.security.WithMockCustomPreactiveUser;
import com.junior.security.WithMockCustomUser;
import com.junior.service.s3.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class MemberIntegrationTest extends IntegrationControllerTest {

    @Autowired
    private MemberController memberController;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;

    //aws 테스트는 요금이 발생할 수 있으므로 해당 객체를 mock 처리
    @MockBean
    private AmazonS3Client amazonS3Client;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void init() throws MalformedURLException {
        Member preactiveTestMember = createPreactiveTestMember();
        Member activeTestMember = createActiveTestMember();

        memberRepository.save(preactiveTestMember);
        memberRepository.save(activeTestMember);

        given(amazonS3Client.getUrl(any(), any())).willReturn(new URL("https://aws.com/new-url"));
    }

    @Test
    @DisplayName("회원 활성화가 정상적으로 이루어져야 함")
    @WithMockCustomPreactiveUser
    public void activeMember_preactive() throws Exception {
        //given
        ActivateMemberDto activateMemberDto = new ActivateMemberDto("updatenick", "강원");
        String content = objectMapper.writeValueAsString(activateMemberDto);

        //when
        ResultActions actions = mockMvc.perform(
                patch("/api/v1/members/activate")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );


        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customCode").value("MEMBER-SUCCESS-001"))
                .andExpect(jsonPath("$.customMessage").value("회원 활성화 성공"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data").value(nullValue()));

        //회원 활성화가 정상 작동해야 함
        Member targetMember = memberRepository.findById(1L).orElseThrow(RuntimeException::new);

        assertThat(targetMember.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(targetMember.getNickname()).isEqualTo("updatenick");
        assertThat(targetMember.getRecommendLocation()).isEqualTo("강원");


    }

    @Test
    @DisplayName("닉네임 사용가능 여부를 반환해야 함")
    void checkNicknameValid() throws Exception {

        //given
        String trueNickname = "nickname";
        String falseNickname = "테스트사용자닉네임";


        //when
        ResultActions actionsTrue = mockMvc.perform(
                get("/api/v1/members/nicknames/check-valid")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("nickname", trueNickname)
        );

        //when
        ResultActions actionsFalse = mockMvc.perform(
                get("/api/v1/members/nicknames/check-valid")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("nickname", falseNickname)
        );

        //then
        actionsTrue
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customCode").value("MEMBER-SUCCESS-002"))
                .andExpect(jsonPath("$.customMessage").value("닉네임 사용가능 여부"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data").value(true));



        //then
        actionsFalse
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customCode").value("MEMBER-SUCCESS-002"))
                .andExpect(jsonPath("$.customMessage").value("닉네임 사용가능 여부"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data").value(false));

    }

    @Test
    @DisplayName("응답에 조회한 회원 정보가 정상적으로 들어가야 함")
    @WithMockCustomUser
    void getMemberInfo() throws Exception {

        //given


        //when
        ResultActions actions = mockMvc.perform(
                get("/api/v1/members")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customCode").value("MEMBER-SUCCESS-007"))
                .andExpect(jsonPath("$.customMessage").value("회원 정보 조회 성공"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.nickname").value("테스트사용자닉네임"))
                .andExpect(jsonPath("$.data.profileImageUrl").value("s3.com/testProfile"));


    }

    @Test
    @DisplayName("닉네임 변경이 정상적으로 진행되어야 함")
    @WithMockCustomUser
    void changeNickname() throws Exception {
        //given
        UpdateNicknameDto updateNicknameDto = new UpdateNicknameDto("테스트수정닉네임");
        String content = objectMapper.writeValueAsString(updateNicknameDto);

        //when
        ResultActions actions = mockMvc.perform(
                patch("/api/v1/members/nicknames")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customCode").value("MEMBER-SUCCESS-005"))
                .andExpect(jsonPath("$.customMessage").value("회원 닉네임 변경 성공"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data").value(nullValue()));

        Member member = memberRepository.findById(2L).get();
        assertThat(member.getNickname()).isEqualTo("테스트수정닉네임");


    }

    @Test
    @WithMockCustomUser
    @DisplayName("프로필 사진 수정 응답이 정상적으로 반환되어야 함")
    void changeProfileImage() throws Exception {

        //given
        MockMultipartFile profileImg = createMockMultipartFile();

        //s3에 저장하는 과정이 생략되어야 함 -> AmazonS3Client mock 처리


        //when
        ResultActions actions = mockMvc.perform(
                multipart(HttpMethod.PATCH, "/api/v1/members/profile-images")
                        .file(profileImg)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customCode").value("MEMBER-SUCCESS-006"))
                .andExpect(jsonPath("$.customMessage").value("회원 프로필 사진 변경 성공"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data").value(nullValue()));

        //회원 정보에 변경된 url이 저장되어야 함
        Member member = memberRepository.findById(2L).get();
        assertThat(member.getProfileImage()).isEqualTo("https://aws.com/new-url");

    }


}
