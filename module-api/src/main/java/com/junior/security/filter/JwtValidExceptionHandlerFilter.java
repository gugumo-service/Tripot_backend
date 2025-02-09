package com.junior.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junior.exception.StatusCode;
import com.junior.response.CommonResponse;
import com.junior.security.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
public class JwtValidExceptionHandlerFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 헤더에서 access키에 담긴 토큰을 꺼냄
        String preAccessToken = request.getHeader("Authorization");

        // 토큰이 없거나 유효하지 않다면 다음 필터로 넘김
        if (preAccessToken == null || !preAccessToken.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = preAccessToken.split(" ")[1];


        try {
            // 토큰이 access인지 확인 (발급시 페이로드에 명시)
            String category = jwtUtil.getCategory(accessToken);

            if (!category.equals("access")) {
                setErrorResponse(response, StatusCode.NOT_ACCESS_TOKEN);
                return;
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            // 토큰 만료 여부 확인
            setErrorResponse(response, StatusCode.EXPIRED_ACCESS_TOKEN);
        } catch (JwtException | IllegalArgumentException e) {
            //유효하지 않은 토큰
            setErrorResponse(response, StatusCode.INVALID_TOKEN);
        }


    }

    private void setErrorResponse(
            HttpServletResponse response,
            StatusCode statusCode
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(statusCode.getHttpCode());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        CommonResponse errorResponse = CommonResponse.fail(statusCode);
        try {
            log.error("[{}] JWT fail, code: {}", Thread.currentThread().getStackTrace()[1].getMethodName(), statusCode);
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
