package com.junior.domain.member;


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

    @Column(length = 100)
    private String username;
    private String password;

    //이미지 저장 방식에 따라 내용이 달라질 수 있음
    private String profileImage;

    @Column(length = 15)
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Column(length = 15)
    @Enumerated(EnumType.STRING)
    private MemberStatus status;

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

}
