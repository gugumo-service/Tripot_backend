package com.junior.domain.member;


import com.junior.domain.like.Like;
import com.junior.dto.member.ActivateMemberDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @Builder.Default
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likeStories = new ArrayList<>();

    public void activateMember(ActivateMemberDto activateMemberDto) {
        nickname = activateMemberDto.nickname();
        isAgreeTermsUse = activateMemberDto.isAgreeTermsUse();
        isAgreeCollectingUsingPersonalInformation = activateMemberDto.isAgreeCollectingUsingPersonalInformation();
        isAgreeMarketing = activateMemberDto.isAgreeMarketing();
        recommendLocation= activateMemberDto.recommendLocation();
        status = MemberStatus.ACTIVE;
    }

    public void deleteMember() {
        status = MemberStatus.DELETE;

        this.nickname = null;
        this.username = null;
        this.password = null;
        this.profileImage = null;
        this.role = null;
        this.isAgreeTermsUse = null;
        this.isAgreeCollectingUsingPersonalInformation = null;
        this.isAgreeMarketing = null;

        this.signUpType = null;
        this.recommendLocation = null;
    }
}
