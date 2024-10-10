package com.junior.dto;

import lombok.Getter;
import lombok.Setter;


public record CheckActiveMemberDto (
        String nickname,
        Boolean hasToAdd

)
{


}
