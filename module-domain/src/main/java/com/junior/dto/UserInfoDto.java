package com.junior.dto;

import com.junior.domain.member.MemberRole;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfoDto {
    private String username;
    private String password;
    private MemberRole role;


    @Builder
    public UserInfoDto(String username, MemberRole role) {
        this.username = username;
        this.password = "";
        this.role = role;
    }
}
