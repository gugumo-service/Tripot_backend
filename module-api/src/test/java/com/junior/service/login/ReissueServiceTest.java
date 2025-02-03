package com.junior.service.login;

import com.junior.dto.jwt.LoginCreateJwtDto;
import com.junior.dto.jwt.RefreshTokenDto;
import com.junior.exception.JwtErrorException;
import com.junior.security.JwtUtil;
import com.junior.util.RedisUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReissueServiceTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RedisUtil redisUtil;

    @InjectMocks
    private ReissueService reissueService;

    @Test
    @DisplayName("reissue가 정상적으로 동작해야 함")
    void reissue_success() {

        //given
        MockHttpServletResponse response = new MockHttpServletResponse();

        String oldRefreshToken = "Bearer testRefresh";

        RefreshTokenDto refreshTokenDto = new RefreshTokenDto(oldRefreshToken);


        String sampleAccess = "sample_access_token";
        String sampleRefresh = "sample_refresh_token";

        given(jwtUtil.isExpired(anyString())).willReturn(false);
        given(jwtUtil.getCategory(anyString())).willReturn("refresh");
        given(redisUtil.existsByKey(anyString())).willReturn(true);
        given(jwtUtil.createJwt(any(LoginCreateJwtDto.class), eq("access"))).willReturn(sampleAccess);
        given(jwtUtil.createJwt(any(LoginCreateJwtDto.class), eq("refresh"))).willReturn(sampleRefresh);
        given(jwtUtil.getId(anyString())).willReturn(1L);
        given(jwtUtil.getUsername(anyString())).willReturn("username");
        given(jwtUtil.getRole(anyString())).willReturn("USER");


        //when
        reissueService.reissue(refreshTokenDto, response);

        //then

        verify(redisUtil).deleteData(oldRefreshToken.split(" ")[1]);
        verify(redisUtil).setDataExpire(sampleRefresh, "username", 15778800);

        //토큰이 헤더에 정상적으로 들어가야 함
        assertThat(response.getHeader("Authorization")).isEqualTo("Bearer " + sampleAccess);
        assertThat(response.getHeader("refresh_token")).isEqualTo("Bearer " + sampleRefresh);


    }

    @Test
    @DisplayName("만료된 토큰에 대한 예외 처리를 해야 함")
    void reissue_expired_token() {

        //given
        MockHttpServletResponse response = new MockHttpServletResponse();

        String oldRefreshToken = "Bearer testRefresh";

        RefreshTokenDto refreshTokenDto = new RefreshTokenDto(oldRefreshToken);


        given(jwtUtil.isExpired(anyString())).willReturn(true);


        //when, then
        assertThatThrownBy(() -> reissueService.reissue(refreshTokenDto, response))
                .isInstanceOf(JwtErrorException.class)
                .hasMessageContaining("만료된 Refresh 토큰");


    }

    @Test
    @DisplayName("refresh_token이 아닌 것에 대한 예외 처리가 되어야 함")
    void reissue_not_refresh() {

        //given
        MockHttpServletResponse response = new MockHttpServletResponse();

        String oldRefreshToken = "Bearer testRefresh";

        RefreshTokenDto refreshTokenDto = new RefreshTokenDto(oldRefreshToken);


        given(jwtUtil.isExpired(anyString())).willReturn(false);
        given(jwtUtil.getCategory(anyString())).willReturn("access");


        //when, then
        assertThatThrownBy(() -> reissueService.reissue(refreshTokenDto, response))
                .isInstanceOf(JwtErrorException.class)
                .hasMessageContaining("Refresh token이 아님");


    }

    @Test
    @DisplayName("redis에 없는 key에 대해 예외 처리를 해야 함")
    void reissue_token_not_exist() {

        //given
        MockHttpServletResponse response = new MockHttpServletResponse();

        String oldRefreshToken = "Bearer testRefresh";

        RefreshTokenDto refreshTokenDto = new RefreshTokenDto(oldRefreshToken);

        given(jwtUtil.isExpired(anyString())).willReturn(false);
        given(jwtUtil.getCategory(anyString())).willReturn("refresh");
        given(redisUtil.existsByKey(anyString())).willReturn(false);


        //when, then
        assertThatThrownBy(() -> reissueService.reissue(refreshTokenDto, response))
                .isInstanceOf(JwtErrorException.class)
                .hasMessageContaining("존재하지 않는 토큰");


    }
}