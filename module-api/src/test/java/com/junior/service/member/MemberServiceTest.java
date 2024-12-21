package com.junior.service.member;

import com.junior.domain.member.Member;
import com.junior.domain.member.MemberRole;
import com.junior.domain.member.MemberStatus;
import com.junior.domain.member.SignUpType;
import com.junior.dto.member.ActivateMemberDto;
import com.junior.dto.member.MemberInfoDto;
import com.junior.dto.member.UpdateNicknameDto;
import com.junior.exception.NotValidMemberException;
import com.junior.repository.member.MemberRepository;
import com.junior.security.UserPrincipal;
import com.junior.service.s3.S3Service;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
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
        assertThat(modifiedMember.getNickname()).isEqualTo("updatenick");
        //recommendLocation이 강원이어야 함
        assertThat(modifiedMember.getRecommendLocation()).isEqualTo("강원");
        //testMember의 status가 ACTIVE여야 함
        assertThat(modifiedMember.getStatus()).isEqualTo(MemberStatus.ACTIVE);
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

        assertThat(memberService.checkDuplicateNickname("테스트닉")).isTrue();
        assertThat(memberService.checkDuplicateNickname("테스트")).isFalse();

    }

    @Test
    @DisplayName("프로필 사진 변경이 정상적으로 이루어져야 함")
    void updateProfileImage_success() {

        //given
        Member testMember = createActiveTestMember();
        UserPrincipal principal = new UserPrincipal(testMember);
        MultipartFile profileImage = createMockMultipartFile();

        given(memberRepository.findById(2L)).willReturn(Optional.ofNullable(testMember));
        given(s3Service.saveProfileImage(profileImage)).willReturn("s3.com/newProfile");

        //when
        memberService.updateProfileImage(principal, profileImage);

        //then
        Member updatedMember = memberRepository.findById(2L).get();
        assertThat(updatedMember.getProfileImage()).isEqualTo("s3.com/newProfile");
    }

    @Test
    @DisplayName("ACTIVE 상태가 아닌 회원은 정보 조회를 할 수 없음")
    void updateProfileImage_fail() {


        //given
        Member testMember = createPreactiveTestMember();
        UserPrincipal principal = new UserPrincipal(testMember);
        given(memberRepository.findById(1L)).willReturn(Optional.ofNullable(testMember));
        MultipartFile profileImage = createMockMultipartFile();


        //when, then
        Assertions.assertThatThrownBy(() -> memberService.updateProfileImage(principal, profileImage)).isInstanceOf(NotValidMemberException.class);


    }

    @Test
    @DisplayName("회원정보 조회 기능에서 해당 회원의 닉네임과 프로필 사진 url을 정상적으로 불러올 것")
    void getMemberInfo_success() {

        //given
        Member testMember = createActiveTestMember();

        given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(testMember));
        UserPrincipal principal = new UserPrincipal(testMember);

        //when
        MemberInfoDto memberInfo = memberService.getMemberInfo(principal);

        //then
        assertThat(memberInfo.nickname()).isEqualTo("테스트닉");
        assertThat(memberInfo.profileImageUrl()).isEqualTo("s3.com/testProfile");


    }

    @Test
    @DisplayName("ACTIVE 상태가 아닌 회원은 정보 조회를 할 수 없음")
    void getMemberInfo_fail() {


        //given
        Member testMember = createPreactiveTestMember();
        UserPrincipal principal = new UserPrincipal(testMember);
        given(memberRepository.findById(1L)).willReturn(Optional.ofNullable(testMember));


        //when, then
        Assertions.assertThatThrownBy(() -> memberService.getMemberInfo(principal)).isInstanceOf(NotValidMemberException.class);


    }

    @Test
    @DisplayName("회원의 닉네임 수정이 정상적으로 이루어져야 함")
    void updateNickname_success() {

        //given
        Member testMember = createActiveTestMember();

        given(memberRepository.findById(2L)).willReturn(Optional.ofNullable(testMember));
        UserPrincipal principal = new UserPrincipal(testMember);

        UpdateNicknameDto updateNicknameDto = new UpdateNicknameDto("updatenick");

        //when
        memberService.updateNickname(principal, updateNicknameDto);

        //then
        Member updatedMember = memberRepository.findById(2L).get();

        assertThat(updatedMember.getNickname()).isEqualTo("updatenick");

    }

    @Test
    @DisplayName("ACTIVE 상태가 아닌 회원은 닉네임 수정을 할 수 없음")
    void updateNickname_fail() {


        //given
        Member testMember = createPreactiveTestMember();
        UserPrincipal principal = new UserPrincipal(testMember);
        given(memberRepository.findById(1L)).willReturn(Optional.ofNullable(testMember));

        UpdateNicknameDto updateNicknameDto = new UpdateNicknameDto("updatenick");


        //when, then
        Assertions.assertThatThrownBy(() -> memberService.updateNickname(principal, updateNicknameDto)).isInstanceOf(NotValidMemberException.class);


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

        assertThat(deletedMember.getStatus()).isEqualTo(MemberStatus.DELETE);

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
                .profileImage("s3.com/testProfile")
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
                .profileImage("s3.com/testProfile")
                .recommendLocation("서울")
                .status(MemberStatus.ACTIVE)
                .build();
    }

    MockMultipartFile createMockMultipartFile() {
        MockMultipartFile profileImg = new MockMultipartFile(
                "프로필 사진",
                "profiles.png",
                MediaType.IMAGE_PNG_VALUE,
                "thumbnail".getBytes()
        );

        return profileImg;

    }
}