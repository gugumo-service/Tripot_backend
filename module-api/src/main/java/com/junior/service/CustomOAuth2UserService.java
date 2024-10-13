package com.junior.service;

import com.junior.domain.member.Member;
import com.junior.domain.member.MemberRole;
import com.junior.domain.member.MemberStatus;
import com.junior.domain.member.SignUpType;
import com.junior.dto.KakaoOAuth2UserInfo;
import com.junior.dto.OAuth2UserInfo;
import com.junior.repository.MemberRepository;
import com.junior.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.debug("[CustomOAuth2UserService] loadUser 호출");

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.debug("[CustomOAuth2UserService] 소셜로그인 등록ID: {}", registrationId);
        OAuth2UserInfo oAuth2UserInfo;
        if (registrationId.equals("kakao")) {
            oAuth2UserInfo = new KakaoOAuth2UserInfo(userRequest.getAccessToken().getTokenValue(), oAuth2User.getAttributes());
        } else {
            return null;
        }

        String username = oAuth2UserInfo.getProvider() + " " + oAuth2UserInfo.getId();




        //새 회원이면 추가 정보 입력 후 1차 회원가입, 기존 회원이면 정보만 업데이트
        boolean existMember = memberRepository.existsByUsername(username);

        Member member;

        if (!existMember) {
            //PREACTIVE 상태 회원 생성

            log.debug("[CustomOAuth2UserService] 신규 회원 생성 username: {}, status: {}", username, MemberStatus.PREACTIVE);

            member = Member.builder()
                    .nickname(oAuth2UserInfo.getNickname())         //일단 전송 후 수정하는 방식
                    .username(username)
                    .role(MemberRole.USER)
                    //사용자 동의 정보: activeMember 기능에 추가
                    .signUpType(SignUpType.valueOf(registrationId.toUpperCase()))
                    .build();

            memberRepository.save(member);

        } else {
            //조건문에서 있는지 검증했음
            member = memberRepository.findByUsername(username).get();
            log.debug("[CustomOAuth2UserService] 기존 회원 username: {}, status: {}", username, member.getStatus());
        }


        return new UserPrincipal(member, oAuth2UserInfo);
    }


}
