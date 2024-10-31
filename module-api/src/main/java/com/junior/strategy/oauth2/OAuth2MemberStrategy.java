package com.junior.strategy.oauth2;

import com.junior.dto.oauth2.OAuth2UserInfo;

public interface OAuth2MemberStrategy {

    OAuth2UserInfo signUpOauth2(String code);
}
