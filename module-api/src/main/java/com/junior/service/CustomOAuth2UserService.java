package com.junior.service;

import com.junior.domain.member.Member;
import com.junior.domain.member.MemberRole;
import com.junior.domain.member.SignUpType;
import com.junior.dto.KakaoOAuth2UserInfo;
import com.junior.dto.OAuth2UserInfo;
import com.junior.dto.UserInfoDto;
import com.junior.repository.MemberRepository;
import com.junior.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo = null;
        if (registrationId.equals("kakao")) {
            oAuth2UserInfo = new KakaoOAuth2UserInfo(userRequest.getAccessToken().getTokenValue(), oAuth2User.getAttributes());
        }
        else {
            return null;
        }

        String username = oAuth2UserInfo.getProvider() + " " + oAuth2UserInfo.getId();

        boolean existMember = memberRepository.existsByUsername(username);

        if (!existMember) {
            Member member = Member.builder()
                    .nickname(oAuth2UserInfo.getNickname())
                    .username(username)
                    .profileImage(oAuth2UserInfo.getProfileImageUrl())
                    .role(MemberRole.USER)
                    .isAgreeMarketing(true)
                    .isAgreeCollectingUsingPersonalInformation(true)
                    .isAgreeCollectingUsingPersonalInformation(true)
                    .signUpType(SignUpType.valueOf(registrationId.toUpperCase()))
                    .build();

            memberRepository.save(member);
        } else {

            Member updateMember = memberRepository.findByUsername(username);
            updateMember.updateOAuth2Member(oAuth2UserInfo);

        }

        UserInfoDto userInfoDto = UserInfoDto.builder()
                .username(username)
                .role(MemberRole.USER)
                .build();

        return new CustomUserDetails(userInfoDto);
    }


}
