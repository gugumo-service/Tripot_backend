package com.junior.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class CheckAdditionMemberInfoDto {
    private String nickname;
    @Setter
    private Boolean hasToAdd;

    public CheckAdditionMemberInfoDto(String nickname) {
        this.nickname = nickname;
    }

}
