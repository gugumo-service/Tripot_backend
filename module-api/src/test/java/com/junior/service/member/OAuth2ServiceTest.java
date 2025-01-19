package com.junior.service.member;

import com.junior.domain.member.Member;
import com.junior.domain.member.MemberRole;
import com.junior.domain.member.MemberStatus;
import com.junior.domain.member.SignUpType;
import com.junior.dto.jwt.LoginCreateJwtDto;
import com.junior.dto.jwt.RefreshTokenDto;
import com.junior.dto.member.CheckActiveMemberDto;
import com.junior.dto.oauth2.OAuth2LoginDto;
import com.junior.dto.oauth2.OAuth2Provider;
import com.junior.dto.oauth2.OAuth2UserInfo;
import com.junior.exception.JwtErrorException;
import com.junior.repository.member.MemberRepository;
import com.junior.security.JwtUtil;
import com.junior.strategy.oauth2.KakaoOAuth2LoginStrategy;
import com.junior.strategy.oauth2.OAuth2UserGenerator;
import com.junior.util.RedisUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OAuth2ServiceTest {

    @Mock
    private OAuth2UserGenerator oAuth2UserGenerator;
    @Mock
    private KakaoOAuth2LoginStrategy kakaoOAuth2LoginStrategy;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private RedisUtil redisUtil;

    @InjectMocks
    OAuth2Service oAuth2Service;

    @Test
    @DisplayName("카카오 로그인 시 관련 기능들의 정상 동작 및 해당 dto의 성공적 반환, 새 회원")
    @Disabled
    void oauth2Login_kakao_new_member() {

        //given
        MockHttpServletResponse response = new MockHttpServletResponse();
        String code = "code";
        OAuth2Provider kakaoProvider = OAuth2Provider.KAKAO;

        String sampleAccess = "sample_access_token";
        String sampleRefresh = "sample_refresh_token";

        given(oAuth2UserGenerator.signUpOAuth2(code)).willReturn(new OAuth2UserInfo(1375L, "nickname", OAuth2Provider.KAKAO));
        given(jwtUtil.createJwt(any(LoginCreateJwtDto.class), eq("access"))).willReturn(sampleAccess);
        given(jwtUtil.createJwt(any(LoginCreateJwtDto.class), eq("refresh"))).willReturn(sampleRefresh);
        given(memberRepository.existsByUsername(anyString())).willReturn(false);


        //when
        CheckActiveMemberDto result = oAuth2Service.oauth2Login(response, code, kakaoProvider);

        //then

        //토큰이 헤더에 정상적으로 들어가야 함
        assertThat(response.getHeader("Authorization")).isEqualTo("Bearer " + sampleAccess);
        assertThat(response.getHeader("refresh_token")).isEqualTo("Bearer " + sampleRefresh);

        //새 회원이므로 isActivate는 false여야 함
        assertThat(result.nickname()).isEqualTo("nickname");
        assertThat(result.isActivate()).isFalse();
    }

    @Test
    @DisplayName("카카오 로그인 시 관련 기능들의 정상 동작 및 해당 dto의 성공적 반환, 추가정보 미기입 회원에 관해 리턴")
    @Disabled
    void oauth2Login_kakao_preactive() {

        //given
        MockHttpServletResponse response = new MockHttpServletResponse();
        String code = "code";
        OAuth2Provider kakaoProvider = OAuth2Provider.KAKAO;

        String sampleAccess = "sample_access_token";
        String sampleRefresh = "sample_refresh_token";

        given(oAuth2UserGenerator.signUpOAuth2(code)).willReturn(new OAuth2UserInfo(1375L, "nickname", OAuth2Provider.KAKAO));
        given(jwtUtil.createJwt(any(LoginCreateJwtDto.class), eq("access"))).willReturn(sampleAccess);
        given(jwtUtil.createJwt(any(LoginCreateJwtDto.class), eq("refresh"))).willReturn(sampleRefresh);
        given(memberRepository.existsByUsername(anyString())).willReturn(true);
        given(memberRepository.findByUsername(anyString())).willReturn(Optional.ofNullable(Member.builder()
                .nickname("nickname")
                .username("username")
                .status(MemberStatus.PREACTIVE)
                .role(MemberRole.USER)
                .signUpType(SignUpType.KAKAO)
                .build()));


        //when
        CheckActiveMemberDto result = oAuth2Service.oauth2Login(response, code, kakaoProvider);

        //then

        //토큰이 헤더에 정상적으로 들어가야 함
        assertThat(response.getHeader("Authorization")).isEqualTo("Bearer " + sampleAccess);
        assertThat(response.getHeader("refresh_token")).isEqualTo("Bearer " + sampleRefresh);

        //새 회원이므로 isActivate는 false여야 함
        assertThat(result.nickname()).isEqualTo("nickname");
        assertThat(result.isActivate()).isFalse();
    }

    @Test
    @DisplayName("카카오 로그인 시 관련 기능들의 정상 동작 및 해당 dto의 성공적 반환, 추가정보 기입 회원에 관해 리턴")
    @Disabled
    void oauth2Login_kakao_active() {

        //given
        MockHttpServletResponse response = new MockHttpServletResponse();
        String code = "code";
        OAuth2Provider kakaoProvider = OAuth2Provider.KAKAO;

        String sampleAccess = "sample_access_token";
        String sampleRefresh = "sample_refresh_token";

        given(oAuth2UserGenerator.signUpOAuth2(code)).willReturn(new OAuth2UserInfo(1375L, "nickname", OAuth2Provider.KAKAO));
        given(jwtUtil.createJwt(any(LoginCreateJwtDto.class), eq("access"))).willReturn(sampleAccess);
        given(jwtUtil.createJwt(any(LoginCreateJwtDto.class), eq("refresh"))).willReturn(sampleRefresh);
        given(memberRepository.existsByUsername(anyString())).willReturn(true);
        given(memberRepository.findByUsername(anyString())).willReturn(Optional.ofNullable(Member.builder()
                .nickname("nickname")
                .username("username")
                .status(MemberStatus.ACTIVE)
                .role(MemberRole.USER)
                .signUpType(SignUpType.KAKAO)
                .build()));


        //when
        CheckActiveMemberDto result = oAuth2Service.oauth2Login(response, code, kakaoProvider);

        //then

        //토큰이 헤더에 정상적으로 들어가야 함
        assertThat(response.getHeader("Authorization")).isEqualTo("Bearer " + sampleAccess);
        assertThat(response.getHeader("refresh_token")).isEqualTo("Bearer " + sampleRefresh);

        //새 회원이므로 isActivate는 false여야 함
        assertThat(result.nickname()).isEqualTo("nickname");
        assertThat(result.isActivate()).isTrue();
    }

    @Test
    @DisplayName("카카오 로그인 시 관련 기능들의 정상 동작 및 해당 dto의 성공적 반환, 새 회원")
    void oauth2LoginV2_kakao_new_member() {

        //given
        MockHttpServletResponse response = new MockHttpServletResponse();
        OAuth2LoginDto oAuth2LoginDto = OAuth2LoginDto.builder()
                .id(1234L)
                .nickname("sample_nickname")
                .build();
        OAuth2Provider kakaoProvider = OAuth2Provider.KAKAO;

        String sampleAccess = "sample_access_token";
        String sampleRefresh = "sample_refresh_token";


        given(jwtUtil.createJwt(any(LoginCreateJwtDto.class), eq("access"))).willReturn(sampleAccess);
        given(jwtUtil.createJwt(any(LoginCreateJwtDto.class), eq("refresh"))).willReturn(sampleRefresh);
        given(memberRepository.existsByUsername(anyString())).willReturn(false);


        //when
        CheckActiveMemberDto result = oAuth2Service.oauth2Login(response, oAuth2LoginDto, kakaoProvider);

        //then

        //토큰이 헤더에 정상적으로 들어가야 함
        assertThat(response.getHeader("Authorization")).isEqualTo("Bearer " + sampleAccess);
        assertThat(response.getHeader("refresh_token")).isEqualTo("Bearer " + sampleRefresh);

        //새 회원이므로 isActivate는 false여야 함
        assertThat(result.nickname()).isEqualTo("sample_nickname");
        assertThat(result.isActivate()).isFalse();
    }

    @Test
    @DisplayName("카카오 로그인 시 관련 기능들의 정상 동작 및 해당 dto의 성공적 반환, 추가정보 미기입 회원에 관해 리턴")
    void oauth2LoginV2_kakao_preactive() {

        //given
        MockHttpServletResponse response = new MockHttpServletResponse();
        OAuth2LoginDto oAuth2LoginDto = OAuth2LoginDto.builder()
                .id(1234L)
                .nickname("nickname")
                .build();
        OAuth2Provider kakaoProvider = OAuth2Provider.KAKAO;

        String sampleAccess = "sample_access_token";
        String sampleRefresh = "sample_refresh_token";

        given(jwtUtil.createJwt(any(LoginCreateJwtDto.class), eq("access"))).willReturn(sampleAccess);
        given(jwtUtil.createJwt(any(LoginCreateJwtDto.class), eq("refresh"))).willReturn(sampleRefresh);
        given(memberRepository.existsByUsername(anyString())).willReturn(true);
        given(memberRepository.findByUsername(anyString())).willReturn(Optional.ofNullable(Member.builder()
                .nickname("nickname")
                .username("username")
                .status(MemberStatus.PREACTIVE)
                .role(MemberRole.USER)
                .signUpType(SignUpType.KAKAO)
                .build()));


        //when
        CheckActiveMemberDto result = oAuth2Service.oauth2Login(response, oAuth2LoginDto, kakaoProvider);

        //then

        //토큰이 헤더에 정상적으로 들어가야 함
        assertThat(response.getHeader("Authorization")).isEqualTo("Bearer " + sampleAccess);
        assertThat(response.getHeader("refresh_token")).isEqualTo("Bearer " + sampleRefresh);

        //새 회원이므로 isActivate는 false여야 함
        assertThat(result.nickname()).isEqualTo("nickname");
        assertThat(result.isActivate()).isFalse();
    }

    @Test
    @DisplayName("카카오 로그인 시 관련 기능들의 정상 동작 및 해당 dto의 성공적 반환, 추가정보 기입 회원에 관해 리턴")
    void oauth2LoginV2_kakao_active() {

        //given
        MockHttpServletResponse response = new MockHttpServletResponse();
        OAuth2LoginDto oAuth2LoginDto = OAuth2LoginDto.builder()
                .id(1234L)
                .nickname("nickname")
                .build();
        OAuth2Provider kakaoProvider = OAuth2Provider.KAKAO;

        String sampleAccess = "sample_access_token";
        String sampleRefresh = "sample_refresh_token";


        given(jwtUtil.createJwt(any(LoginCreateJwtDto.class), eq("access"))).willReturn(sampleAccess);
        given(jwtUtil.createJwt(any(LoginCreateJwtDto.class), eq("refresh"))).willReturn(sampleRefresh);
        given(memberRepository.existsByUsername(anyString())).willReturn(true);
        given(memberRepository.findByUsername(anyString())).willReturn(Optional.ofNullable(Member.builder()
                .nickname("nickname")
                .username("username")
                .status(MemberStatus.ACTIVE)
                .role(MemberRole.USER)
                .signUpType(SignUpType.KAKAO)
                .build()));


        //when
        CheckActiveMemberDto result = oAuth2Service.oauth2Login(response, oAuth2LoginDto, kakaoProvider);

        //then

        //토큰이 헤더에 정상적으로 들어가야 함
        assertThat(response.getHeader("Authorization")).isEqualTo("Bearer " + sampleAccess);
        assertThat(response.getHeader("refresh_token")).isEqualTo("Bearer " + sampleRefresh);

        //새 회원이므로 isActivate는 false여야 함
        assertThat(result.nickname()).isEqualTo("nickname");
        assertThat(result.isActivate()).isTrue();
    }


    @Test
    @DisplayName("로그아웃의 모든 과정이 성공적으로 진행되어야 함")
    void logout_success() {

        //given

        //해당 토큰의 유효기간은 테스트 메서드 작동으로부터 6개월
        String refreshToken = "Bearer test_refresh";
        String realRefresh = refreshToken.split(" ")[1];

        RefreshTokenDto refreshTokenDto = new RefreshTokenDto(refreshToken);

        given(jwtUtil.getCategory(realRefresh)).willReturn("refresh");

        //when
        oAuth2Service.logout(refreshTokenDto);

        //then
        verify(redisUtil).deleteData(realRefresh);     //해당 메서드가 동작하였는지 검증
    }

    @Test
    @DisplayName("로그아웃 중 유효한 토큰이 아닐 경우(RFC 7235를 지키지 않음) 관련 예외처리를 해야 함")
    void logout_not_valid() {

        //given

        //해당 토큰의 유효기간은 테스트 메서드 작동으로부터 6개월
        String refreshToken = "Beareer test_refresh";
        String realRefresh = refreshToken.split(" ")[1];

        RefreshTokenDto refreshTokenDto = new RefreshTokenDto(refreshToken);


        //when, then
        assertThatThrownBy(() -> oAuth2Service.logout(refreshTokenDto))
                .isInstanceOf(JwtErrorException.class)
                .hasMessageContaining("유효하지 않은 토큰");


    }

    @Test
    @DisplayName("로그아웃 중 refresh 토큰이 아닐 경우 관련 예외처리를 해야 함")
    void logout_not_refresh() {

        //given

        //해당 토큰의 유효기간은 테스트 메서드 작동으로부터 6개월
        String accessToken = "Bearer test_access";
        String realAccess = accessToken.split(" ")[1];

        RefreshTokenDto refreshTokenDto = new RefreshTokenDto(accessToken);

        given(jwtUtil.getCategory(realAccess)).willReturn("access");

        //when, then
        assertThatThrownBy(() -> oAuth2Service.logout(refreshTokenDto))
                .isInstanceOf(JwtErrorException.class)
                .hasMessageContaining("Refresh token이 아님");


    }

    @Test
    @DisplayName("로그아웃 중 토큰이 만료되었을 경우 관련 예외처리를 해야 함")
    void logout_expired() {


        //given

        //해당 토큰의 유효기간은 테스트 메서드 작동으로부터 6개월
        String refreshToken = "Bearer test_refresh";
        String realRefresh = refreshToken.split(" ")[1];

        RefreshTokenDto refreshTokenDto = new RefreshTokenDto(refreshToken);

        given(jwtUtil.getCategory(realRefresh)).willThrow(ExpiredJwtException.class);


        //when, then
        assertThatThrownBy(() -> oAuth2Service.logout(refreshTokenDto))
                .isInstanceOf(JwtErrorException.class)
                .hasMessageContaining("만료된 Refresh 토큰");


    }

    @Test
    @DisplayName("로그아웃 중 유효하지 않은 토큰에 대해 관련 예외처리를 해야 함")
    void logout_jwt_exception() {


        //given

        //해당 토큰의 유효기간은 테스트 메서드 작동으로부터 6개월
        String refreshToken = "Bearer test_refresh";
        String realRefresh = refreshToken.split(" ")[1];

        RefreshTokenDto refreshTokenDto = new RefreshTokenDto(refreshToken);

        given(jwtUtil.getCategory(realRefresh)).willThrow(JwtException.class);


        //when, then
        assertThatThrownBy(() -> oAuth2Service.logout(refreshTokenDto))
                .isInstanceOf(JwtErrorException.class)
                .hasMessageContaining("유효하지 않은 토큰");


    }

}