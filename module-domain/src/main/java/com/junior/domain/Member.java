package com.junior.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
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

    @Column(length = 40)
    private String loginId;
    private String password;

    //이미지 저장 방식에 따라 내용이 달라질 수 있음
    private String profileImagePath;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private MemberStatus status;

}
