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
    @Column(length = 25)
    private String password;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private MemberStatus status;

}
