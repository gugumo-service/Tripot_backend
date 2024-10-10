package com.junior.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class CheckActiveMemberDto {
    private String nickname;
    @Setter
    private Boolean hasToAdd;

    public CheckActiveMemberDto(String nickname) {
        this.nickname = nickname;
    }

}
