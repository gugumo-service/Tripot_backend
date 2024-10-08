package com.junior.domain.member;


import com.junior.dto.ActivateMemberDto;
import com.junior.dto.OAuth2UserInfo;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(length = 25)
    private String nickname;

    private String username;
    private String password;

    //이미지 저장 방식에 따라 내용이 달라질 수 있음
    private String profileImage;

    @Column(length = 15)
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    //추가정보 입력 후 ACTIVE로 변경
    @Column(length = 15)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MemberStatus status = MemberStatus.PREACTIVE;

    // 서비스 이용 약관 동의 여부
    private Boolean isAgreeTermsUse;
    // 개인정보 수집 및 이용 동의
    private Boolean isAgreeCollectingUsingPersonalInformation;
    // 마케팅 수신 동의
    private Boolean isAgreeMarketing;

    @Column(length = 15)
    @Enumerated(EnumType.STRING)
    private SignUpType signUpType;

    //추천 여행 지역 -> 추후 추가예정
    private String recommendLocation;

    //sns에서 프로필 사진 업데이트 시 마다 해주어야 하나? 자체 서비스에서 관리하게 되면 오히려 불편할 수도 있을거 같음
    public void updateOAuth2Member(OAuth2UserInfo oAuth2UserInfo) {
        profileImage = oAuth2UserInfo.getProfileImageUrl();
    }

    public void activateMember(ActivateMemberDto activateMemberDto) {
        nickname = activateMemberDto.getNickname();
        isAgreeTermsUse = activateMemberDto.getIsAgreeTermsUse();
        isAgreeCollectingUsingPersonalInformation = activateMemberDto.getIsAgreeCollectingUsingPersonalInformation();
        isAgreeMarketing = activateMemberDto.getIsAgreeMarketing();
        status = MemberStatus.ACTIVE;
    }

}
