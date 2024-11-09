package com.junior.config;

import com.junior.security.exceptionhandler.CustomAuthenticationEntryPoint;
import com.junior.security.JwtUtil;
import com.junior.security.filter.JwtValidExceptionHandlerFilter;
import com.junior.security.filter.JWTFilter;
import com.junior.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)


                .sessionManagement((session) -> session.
                        sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                //filter
                .addFilterAfter(new JWTFilter(jwtUtil, userDetailsService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtValidExceptionHandlerFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)

                //403 예외 처리
                .exceptionHandling((authenticationManager) -> authenticationManager
                        .authenticationEntryPoint(customAuthenticationEntryPoint))

                //uri 권한 설정
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/v1/login/**").permitAll()
                        .requestMatchers("/api/v1/reissue").permitAll()
                        .requestMatchers("/api/v1/story/list").permitAll()
                        //닉네임 중복 여부 확인
                        .requestMatchers("/api/v1/members/nicknames/check-valid").permitAll()
                        .anyRequest().authenticated());


        return httpSecurity.build();

    }


}
