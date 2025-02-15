package com.junior.strategy.oauth2;

import com.junior.dto.oauth2.OAuth2LoginDto;
import com.junior.dto.oauth2.OAuth2Provider;
import com.junior.dto.oauth2.OAuth2UserInfo;

public interface OAuth2MemberStrategy {

    OAuth2UserInfo getOAuth2UserInfo(OAuth2LoginDto oAuth2LoginDto);

    boolean isTarget(OAuth2Provider oAuth2Provider);
}
