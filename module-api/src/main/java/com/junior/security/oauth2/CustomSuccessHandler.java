package com.junior.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junior.domain.member.Member;
import com.junior.domain.member.MemberStatus;
import com.junior.dto.CheckAdditionMemberInfoDto;
import com.junior.dto.LoginCreateJwtDto;
import com.junior.security.UserPrincipal;
import com.junior.security.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    //oauth 로그인 성공 시 로직
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        //인증된 사용자 정보 가져오기
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

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

        String accessToken = jwtUtil.createJwt(loginCreateJwtDto, "access", 60 * 60 * 60L);
        String refreshToken = jwtUtil.createJwt(loginCreateJwtDto, "refresh", 8640_0000L);

        //응답에 JWT 추가
        response.addHeader("Authorization", accessToken);
        response.addHeader("refresh_token", refreshToken);


        //응답에 해당 회원의 추가정보 기입 여부 추가
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        CheckAdditionMemberInfoDto checkAdditionMemberInfoDto = new CheckAdditionMemberInfoDto(member.getNickname());

        if (member.getStatus() == MemberStatus.PREACTIVE) {
            checkAdditionMemberInfoDto.setHasToAdd(true);
        }else{
            checkAdditionMemberInfoDto.setHasToAdd(false);
        }

        response.getWriter().write(objectMapper.writeValueAsString(checkAdditionMemberInfoDto));

        //TODO: redis에 refreshToken 저장하기

    }



}

