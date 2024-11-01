package com.junior.strategy.oauth2;

import com.junior.dto.oauth2.OAuth2UserInfo;
import org.springframework.stereotype.Component;

@Component
public class OAuth2UserGenerator {
    private OAuth2MemberStrategy oAuth2MemberStrategy;

    public OAuth2UserInfo signUpOAuth2(String code) {
        return oAuth2MemberStrategy.signUpOauth2(code);
    }

    public void setOAuth2MemberStrategy(OAuth2MemberStrategy oAuth2MemberStrategy) {
        this.oAuth2MemberStrategy = oAuth2MemberStrategy;
    }
}
