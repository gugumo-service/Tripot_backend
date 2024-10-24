package com.junior.security.filter;

import com.junior.service.CustomOAuth2UserService;
import com.junior.service.UserDetailsServiceImpl;
import com.junior.security.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 헤더에서 access키에 담긴 토큰을 꺼냄
        String preAccessToken = request.getHeader("Authorization");

        // 토큰이 없거나 유효하지 않다면 다음 필터로 넘김
        if (preAccessToken == null || !preAccessToken.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);

            return;
        }

        String accessToken = preAccessToken.split(" ")[1];

        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");

            //TODO: access 토큰 만료 시의 약속을 프론트와 지정하기 -> 프론트에서 만료가 확인되면 reissue
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 토큰이 access인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals("access")) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // username, role 값을 획득
        Long id = jwtUtil.getId(accessToken);
        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);


        //username->member->userDetails(userPrincipal)으로, userDetailsService에서 꺼내도 무방
        //attributes 내용들은 회원가입 시점에 저장되므로 신경 안써도 될듯?
        UserDetails customUserDetails = userDetailsService.loadUserByUsername(id.toString());

        //JWT 방식이므로 여기서 1회용 회원 세션 저장
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

    }
}
