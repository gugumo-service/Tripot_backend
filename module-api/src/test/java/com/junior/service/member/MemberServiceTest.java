package com.junior.service.member;

import com.junior.domain.member.Member;
import com.junior.domain.member.MemberRole;
import com.junior.domain.member.MemberStatus;
import com.junior.domain.member.SignUpType;
import com.junior.dto.member.ActivateMemberDto;
import com.junior.exception.NotValidMemberException;
import com.junior.exception.StatusCode;
import com.junior.repository.member.MemberRepository;
import com.junior.security.UserPrincipal;
import com.junior.service.s3.S3Service;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    S3Service s3Service;

    @InjectMocks
    MemberService memberService;

//    @BeforeEach
//    void init() {
//        Member preactiveTestMember = createPreactiveTestMember();
//    }


    @Test
    @DisplayName("회원 활성화 시 회원 정보 업데이트 및 ACTIVE 상태가 되어야 함")
    void activateMember_success() {

        //given
        ActivateMemberDto activateMemberDto = new ActivateMemberDto("updatenick", "강원");

        //비활성화된 회원 생성 및 저장
        Member testMember = createPreactiveTestMember();
        UserPrincipal principal = new UserPrincipal(testMember);
        given(memberRepository.findById(1L)).willReturn(Optional.ofNullable(testMember));


        //when
        memberService.activateMember(principal, activateMemberDto);

        //then

        Member modifiedMember = memberRepository.findById(1L).get();

        //닉네임이 updatenick으로 변경되어야 함
        Assertions.assertThat(modifiedMember.getNickname()).isEqualTo("updatenick");
        //recommendLocation이 강원이어야 함
        Assertions.assertThat(modifiedMember.getRecommendLocation()).isEqualTo("강원");
        //testMember의 status가 ACTIVE여야 함
        Assertions.assertThat(modifiedMember.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    @DisplayName("이미 ACTIVE 상태인 회원은 활성화를 할 수 없음")
    void activateMember_fail() {


        //given
        ActivateMemberDto activateMemberDto = new ActivateMemberDto("updatenick", "강원");

        //비활성화된 회원 생성 및 저장
        Member testMember = createActiveTestMember();
        UserPrincipal principal = new UserPrincipal(testMember);
        given(memberRepository.findById(2L)).willReturn(Optional.ofNullable(testMember));



        //when, then
        Assertions.assertThatThrownBy(() -> memberService.activateMember(principal, activateMemberDto)).isInstanceOf(NotValidMemberException.class);


    }

    @Test
    @DisplayName("중복된 닉네임이라면 True, 아니면 False를 반환")
    void checkDuplicateNickname() {

        given(memberRepository.existsByNickname("테스트닉")).willReturn(true);          //테스트닉이라는 닉네임을 가진 사용자가 존재할 때

        Assertions.assertThat(memberService.checkDuplicateNickname("테스트닉")).isTrue();
        Assertions.assertThat(memberService.checkDuplicateNickname("테스트")).isFalse();

    }

    @Test
    void updateProfileImage() {
    }

    @Test
    void getMemberInfo() {
    }

    @Test
    void updateNickname() {
    }

    @Test
    @DisplayName("삭제된 회원은 DELETE 상태가 되어야 함")
    void deleteMember() {

        //given
        Member testMember = createActiveTestMember();
        UserPrincipal principal = new UserPrincipal(testMember);
        given(memberRepository.findById(2L)).willReturn(Optional.ofNullable(testMember));

        //when
        memberService.deleteMember(principal);

        //then

        Member deletedMember = memberRepository.findById(2L).get();

        Assertions.assertThat(deletedMember.getStatus()).isEqualTo(MemberStatus.DELETE);

    }

    @Test
    @DisplayName("활성화되지 않은 회원에 대한 탈퇴는 예외를 발생시킴")
    void deleteMember_fail() {


        //given

        //비활성화된 회원 생성 및 저장
        Member testMember = createPreactiveTestMember();
        UserPrincipal principal = new UserPrincipal(testMember);
        given(memberRepository.findById(1L)).willReturn(Optional.ofNullable(testMember));
        

        //when, then
        Assertions.assertThatThrownBy(() -> memberService.deleteMember(principal)).isInstanceOf(NotValidMemberException.class);


    }

    Member createPreactiveTestMember() {
        return Member.builder()
                .id(1L)
                .nickname("테스트닉")
                .username("KAKAO 3748293466")
                .role(MemberRole.USER)
                .signUpType(SignUpType.KAKAO)
                .recommendLocation("서울")
                .build();
    }

    Member createActiveTestMember() {
        return Member.builder()
                .id(2L)
                .nickname("테스트닉")
                .username("KAKAO 3748293465")
                .role(MemberRole.USER)
                .signUpType(SignUpType.KAKAO)
                .recommendLocation("서울")
                .status(MemberStatus.ACTIVE)
                .build();
    }
}