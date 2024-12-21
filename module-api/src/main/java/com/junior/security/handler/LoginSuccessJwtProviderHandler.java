package com.junior.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junior.domain.member.Member;
import com.junior.dto.jwt.LoginCreateJwtDto;
import com.junior.exception.StatusCode;
import com.junior.response.CommonResponse;
import com.junior.security.JwtUtil;
import com.junior.security.UserPrincipal;
import com.junior.util.RedisUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessJwtProviderHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        setResponse(response, StatusCode.ADMIN_LOGIN_SUCCESS);
        addToken(response, principal);

    }


    private void addToken(HttpServletResponse response, UserPrincipal principal) {


        Member member = principal.getMember();

        LoginCreateJwtDto loginCreateJwtDto = LoginCreateJwtDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .role(member.getRole().toString())
                .requestTimeMs(LocalDateTime.now())
                .build();

        String accessToken = jwtUtil.createJwt(loginCreateJwtDto, "access");
        String refreshToken = jwtUtil.createJwt(loginCreateJwtDto, "refresh");
        log.info("[{}} JWT 토큰 생성 access: {}, refresh: {}", Thread.currentThread().getStackTrace()[1].getClassName(), accessToken, refreshToken);

        //redis에 refreshToken 저장하기((key, value): (token, username))
        //Bearer을 포함하지 않음
        redisUtil.setDataExpire(refreshToken, member.getUsername(), 15778800);


        //응답에 JWT 추가
        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addHeader("Refresh_token", "Bearer " + refreshToken);
        log.info("[{}} 응답 헤더에 토큰 담기", Thread.currentThread().getStackTrace()[1].getClassName());

    }

    private void setResponse(
            HttpServletResponse response,
            StatusCode statusCode
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(statusCode.getHttpCode());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        CommonResponse errorResponse = CommonResponse.success(statusCode, null);
        try {
            log.info("[{}] admin login success, code: {}", Thread.currentThread().getStackTrace()[1].getMethodName(), statusCode);
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
