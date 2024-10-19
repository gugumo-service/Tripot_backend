package com.junior.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junior.domain.member.Member;
import com.junior.domain.member.MemberStatus;
import com.junior.dto.CheckActiveMemberDto;
import com.junior.dto.LoginCreateJwtDto;
import com.junior.security.UserPrincipal;
import com.junior.security.JwtUtil;
import com.junior.util.RedisUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final RedisUtil redisUtil;

    //oauth 로그인 성공 시 로직
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //인증된 사용자 정보 가져오기
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        log.info("[{}} 사용자 정보 로딩: {}", getClass().getName(), principal);

        Member member = principal.getMember();
        String username = principal.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        //JWT 생성
        LoginCreateJwtDto loginCreateJwtDto = LoginCreateJwtDto.builder()
                .id(member.getId())
                .username(username)
                .role(role)
                .requestTimeMs(LocalDateTime.now())
                .build();

        String accessToken = jwtUtil.createJwt(loginCreateJwtDto, "access");
        String refreshToken = jwtUtil.createJwt(loginCreateJwtDto, "refresh");
        log.info("[{}} JWT 토큰 생성 access: {}, refresh: {}", getClass().getName(), accessToken, refreshToken);


        //응답에 JWT 추가
        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addHeader("Refresh_token", "Bearer " + refreshToken);
        log.info("[{}} 응답 헤더에 토큰 담기", getClass().getName());

        //응답에 해당 회원의 추가정보 기입 여부 추가
        log.info("[{}} 응답에 해당 회원의 추가정보 기입 여부 추가", getClass().getName());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        CheckActiveMemberDto checkActiveMemberDto;

        if (member.getStatus() == MemberStatus.PREACTIVE) {
            checkActiveMemberDto = new CheckActiveMemberDto(member.getNickname(), false);
        }else{
            checkActiveMemberDto = new CheckActiveMemberDto(member.getNickname(), true);
        }

        response.getWriter().write(objectMapper.writeValueAsString(checkActiveMemberDto));

        //redis에 refreshToken 저장하기((key, value): (token, username))
        //Bearer을 포함하지 않음
        redisUtil.setDataExpire(refreshToken, username, 8640_0000L);

    }



}

