package com.junior.strategy.oauth2;

import com.junior.dto.oauth2.KakaoTokenResponseDto;
import com.junior.dto.oauth2.KakaoUserInfoResponseDto;
import com.junior.dto.oauth2.OAuth2UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.junior.dto.oauth2.OAuth2Provider.*;

@Slf4j
public class KakaoOAuth2LoginStrategy implements OAuth2MemberStrategy {

    @Value("${kakao.client-id}")
    private String clientId;
    @Value("${kakao.client-secret}")
    private String secretKey;
    private final String KAKAO_REDIRECT_URI = "http://54.180.139.123:8080/api/v1/login/oauth2/kakao";
    private final String KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com";
    private final String KAUTH_USER_URL_HOST = "https://kapi.kakao.com";

    @Override
    public OAuth2UserInfo signUpOauth2(String code) {
        KakaoUserInfoResponseDto userInfo = getUserInfo(getAccessTokenFromKakao(code));

        return OAuth2UserInfo.builder().
                id(userInfo.getId())
                .nickname(userInfo.getKakaoAccount().getProfile().getNickName())
                .provider(KAKAO)
                .build();
    }



    private String getAccessTokenFromKakao(String code) {

        log.info("[{}] get access token from kakao", Thread.currentThread().getStackTrace()[1].getMethodName());

        KakaoTokenResponseDto kakaoTokenResponseDto = WebClient.create(KAUTH_TOKEN_URL_HOST).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("redirect_uri", KAKAO_REDIRECT_URI)
                        .queryParam("code", code)
                        .queryParam("client_secret", secretKey)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .retrieve()
//                .onStatus(HttpStatusCode::is4xxClientError, ClientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
//                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KakaoTokenResponseDto.class)
                .block();

        log.info(" [{}] Access Token ------> {}", Thread.currentThread().getStackTrace()[1].getClassName(), kakaoTokenResponseDto.getAccessToken());
        log.info(" [{}] Refresh Token ------> {}", Thread.currentThread().getStackTrace()[1].getClassName(), kakaoTokenResponseDto.getRefreshToken());
        //제공 조건: OpenID Connect가 활성화 된 앱의 토큰 발급 요청인 경우 또는 scope에 openid를 포함한 추가 항목 동의 받기 요청을 거친 토큰 발급 요청인 경우
        log.info(" [{}] Id Token ------> {}", Thread.currentThread().getStackTrace()[1].getClassName(), kakaoTokenResponseDto.getIdToken());
        log.info(" [{}] Scope ------> {}", Thread.currentThread().getStackTrace()[1].getClassName(), kakaoTokenResponseDto.getScope());

        return kakaoTokenResponseDto.getAccessToken();
    }

    private KakaoUserInfoResponseDto getUserInfo(String accessToken) {
        log.info("[{}] get user info from kakao", Thread.currentThread().getStackTrace()[1].getMethodName());

        KakaoUserInfoResponseDto userInfo = WebClient.create(KAUTH_USER_URL_HOST)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // access token 인가
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .retrieve()
//                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
//                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KakaoUserInfoResponseDto.class)
                .block();

        log.info("[{}] Auth ID ---> {} ", Thread.currentThread().getStackTrace()[1].getClassName(), userInfo.getId());
        log.info("[{}] NickName ---> {} ", Thread.currentThread().getStackTrace()[1].getClassName(), userInfo.getKakaoAccount().getProfile().getNickName());

        return userInfo;
    }
}
