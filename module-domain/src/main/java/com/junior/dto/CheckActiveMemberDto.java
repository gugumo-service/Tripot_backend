package com.junior.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public record CheckActiveMemberDto (
        String nickname,
        Boolean hasToAdd

)
{


}
